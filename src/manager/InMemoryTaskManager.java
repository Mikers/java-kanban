package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubtaskIds(new ArrayList<>());
            updateEpicStatus(epic);
        }
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    @Override
    public int createTask(Task task) {
        validateNotNull(task, "Task");
        int id = assignIdAndStore(task);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        validateNotNull(epic, "Epic");
        int id = assignIdAndStore(epic);
        epic.setStatus(TaskStatus.NEW);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        validateNotNull(subtask, "Subtask");
        if (subtask.getId() != 0 && subtask.getId() == subtask.getEpicId()) {
            throw new IllegalArgumentException("Epic cannot add itself as subtask");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new IllegalArgumentException("Epic with id " + subtask.getEpicId() + " not found");
        }
        
        int id = assignIdAndStore(subtask);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        validateNotNull(task, "Task");
        validateExists(task.getId(), tasks, "Task");
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        validateNotNull(epic, "Epic");
        validateExists(epic.getId(), epics, "Epic");
        Epic existingEpic = epics.get(epic.getId());
        existingEpic.setName(epic.getName());
        existingEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        validateNotNull(subtask, "Subtask");
        validateExists(subtask.getId(), subtasks, "Subtask");
        
        if (subtask.getId() == subtask.getEpicId()) {
            throw new IllegalArgumentException("Subtask cannot be its own epic");
        }
        
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }


    private int generateId() {
        return nextId++;
    }

    private int assignIdAndStore(Task task) {
        int id = generateId();
        task.setId(id);
        return id;
    }

    private void validateNotNull(Object obj, String typeName) {
        if (obj == null) {
            throw new IllegalArgumentException(typeName + " cannot be null");
        }
    }
    
    private void validateExists(int id, Map<Integer, ?> storage, String typeName) {
        if (!storage.containsKey(id)) {
            throw new IllegalArgumentException(typeName + " with id " + id + " not found");
        }
    }
    
    private void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        
        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                TaskStatus subtaskStatus = subtask.getStatus();
                if (subtaskStatus != TaskStatus.NEW) {
                    allNew = false;
                }
                if (subtaskStatus != TaskStatus.DONE) {
                    allDone = false;
                }
            }
        }

        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}