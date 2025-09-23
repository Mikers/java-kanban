package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    void deleteAllTasks();

    Task getTaskById(int id);

    int createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    List<Epic> getEpics();

    void deleteAllEpics();

    Epic getEpicById(int id);

    int createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    List<Subtask> getSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int id);

    int createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);
}