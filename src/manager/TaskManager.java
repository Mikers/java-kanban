package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager implements TaskManagerInterface {
    private int nextId = 1;
    
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubtaskIds(new ArrayList<>());
            updateEpicStatus(epic);
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public int createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        int id = assignIdAndStore(task);
        tasks.put(id, task);
        return id;
    }

    public int createEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Epic cannot be null");
        }
        int id = assignIdAndStore(epic);
        epic.setStatus(TaskStatus.NEW);
        epics.put(id, epic);
        return id;
    }

    public int createSubtask(Subtask subtask) {
        if (subtask == null) {
            throw new IllegalArgumentException("Subtask cannot be null");
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

    public void updateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (!tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("Task with id " + task.getId() + " not found");
        }
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Epic cannot be null");
        }
        if (!epics.containsKey(epic.getId())) {
            throw new IllegalArgumentException("Epic with id " + epic.getId() + " not found");
        }
        Epic existingEpic = epics.get(epic.getId());
        existingEpic.setName(epic.getName());
        existingEpic.setDescription(epic.getDescription());
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            throw new IllegalArgumentException("Subtask cannot be null");
        }
        if (!subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Subtask with id " + subtask.getId() + " not found");
        }
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

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

    private int generateId() {
        return nextId++;
    }

    private int assignIdAndStore(Task task) {
        int id = generateId();
        task.setId(id);
        return id;
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