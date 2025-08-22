package manager;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryTaskManagerTest {
    
    private TaskManager taskManager;
    
    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }
    
    @Test
    void testCreateTask() {
        Task task = new Task("Test createTask", "Test createTask description");
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }
    
    @Test
    void shouldGenerateUniqueIds() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        
        int id1 = taskManager.createTask(task1);
        int id2 = taskManager.createTask(task2);
        
        assertNotEquals(id1, id2, "Id должны быть уникальными");
    }
    
    @Test
    void shouldFindTaskById() {
        Task task = new Task("Task", "Description");
        int id = taskManager.createTask(task);
        
        Task found = taskManager.getTask(id);
        assertNotNull(found, "Задача должна быть найдена по id");
        assertEquals(task, found, "Найденная задача должна совпадать с добавленной");
    }
    
    @Test
    void shouldUpdateTask() {
        Task task = new Task("Original", "Original Description");
        int id = taskManager.createTask(task);
        
        Task updatedTask = new Task("Updated", "Updated Description");
        updatedTask.setId(id);
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(updatedTask);
        
        Task found = taskManager.getTask(id);
        assertEquals("Updated", found.getName(), "Имя должно обновиться");
        assertEquals("Updated Description", found.getDescription(), "Описание должно обновиться");
        assertEquals(TaskStatus.IN_PROGRESS, found.getStatus(), "Статус должен обновиться");
    }
    
    @Test
    void shouldDeleteTaskById() {
        Task task = new Task("Task", "Description");
        int id = taskManager.createTask(task);
        
        taskManager.deleteTaskById(id);
        
        Task found = taskManager.getTask(id);
        assertNull(found, "Задача должна быть удалена");
    }
    
    @Test
    void shouldNotAddEpicAsSubtaskToItself() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.createEpic(epic);
        
        Subtask invalidSubtask = new Subtask("Subtask", "Description", epicId);
        invalidSubtask.setId(epicId);
        
        assertThrows(IllegalArgumentException.class, 
            () -> taskManager.createSubtask(invalidSubtask),
            "Должно выбрасываться исключение при попытке добавить эпик как подзадачу самого себя");
    }
    
    @Test
    void shouldNotMakeSubtaskItsOwnEpic() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.createEpic(epic);
        
        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);
        
        Subtask updatedSubtask = new Subtask("Subtask", "Description", subtaskId);
        updatedSubtask.setId(subtaskId);
        
        assertThrows(IllegalArgumentException.class,
            () -> taskManager.updateSubtask(updatedSubtask),
            "Должно выбрасываться исключение при попытке сделать подзадачу своим же эпиком");
    }
    
    @Test
    void shouldAddTasksOfDifferentTypes() {
        Task task = new Task("Task", "Task Description");
        Epic epic = new Epic("Epic", "Epic Description");
        
        int taskId = taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        
        Subtask subtask = new Subtask("Subtask", "Subtask Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);
        
        assertNotNull(taskManager.getTask(taskId), "Обычная задача должна быть добавлена");
        assertNotNull(taskManager.getEpic(epicId), "Эпик должен быть добавлен");
        assertNotNull(taskManager.getSubtask(subtaskId), "Подзадача должна быть добавлена");
    }
    
    @Test
    void shouldFindTasksById() {
        Task task = new Task("Task", "Description");
        Epic epic = new Epic("Epic", "Description");
        
        int taskId = taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        
        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);
        
        assertEquals(task, taskManager.getTask(taskId), "Должна находить задачу по id");
        assertEquals(epic, taskManager.getEpic(epicId), "Должна находить эпик по id");
        assertEquals(subtask, taskManager.getSubtask(subtaskId), "Должна находить подзадачу по id");
    }
    
    @Test
    void shouldMaintainTaskUnchangedWhenAdded() {
        Task originalTask = new Task("Original", "Original Description");
        originalTask.setStatus(TaskStatus.IN_PROGRESS);
        
        int id = taskManager.createTask(originalTask);
        Task savedTask = taskManager.getTask(id);
        
        assertEquals(originalTask.getName(), savedTask.getName(), 
            "Имя должно остаться неизменным");
        assertEquals(originalTask.getDescription(), savedTask.getDescription(), 
            "Описание должно остаться неизменным");
        assertEquals(originalTask.getStatus(), savedTask.getStatus(), 
            "Статус должен остаться неизменным");
    }
    
    @Test
    void shouldUpdateEpicStatusWhenSubtasksChange() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.createEpic(epic);
        
        assertEquals(TaskStatus.NEW, taskManager.getEpic(epicId).getStatus());
        
        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        int subtaskId1 = taskManager.createSubtask(subtask1);
        assertEquals(TaskStatus.NEW, taskManager.getEpic(epicId).getStatus());
        
        subtask1.setId(subtaskId1);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(epicId).getStatus());
        
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtaskId2 = taskManager.createSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(epicId).getStatus());
        
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setId(subtaskId2);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(TaskStatus.DONE, taskManager.getEpic(epicId).getStatus());
    }
    
    @Test
    void shouldRemoveSubtasksWhenEpicDeleted() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.createEpic(epic);
        
        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtaskId1 = taskManager.createSubtask(subtask1);
        int subtaskId2 = taskManager.createSubtask(subtask2);
        
        taskManager.deleteEpicById(epicId);
        
        assertNull(taskManager.getEpic(epicId), "Эпик должен быть удален");
        assertNull(taskManager.getSubtask(subtaskId1), "Подзадача 1 должна быть удалена");
        assertNull(taskManager.getSubtask(subtaskId2), "Подзадача 2 должна быть удалена");
    }
    
    @Test
    void shouldGetEpicSubtasks() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.createEpic(epic);
        
        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        
        List<Subtask> epicSubtasks = taskManager.getEpicSubtasks(epicId);
        
        assertEquals(2, epicSubtasks.size(), "У эпика должно быть 2 подзадачи");
        assertTrue(epicSubtasks.contains(subtask1), "Должна содержаться подзадача 1");
        assertTrue(epicSubtasks.contains(subtask2), "Должна содержаться подзадача 2");
    }
    
    @Test
    void shouldTrackHistory() {
        Task task = new Task("Task", "Description");
        Epic epic = new Epic("Epic", "Description");
        
        int taskId = taskManager.createTask(task);
        int epicId = taskManager.createEpic(epic);
        
        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);
        
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        
        List<Task> history = taskManager.getHistory();
        
        assertEquals(3, history.size(), "В истории должно быть 3 задачи");
        assertEquals(task, history.get(0), "Первой должна быть обычная задача");
        assertEquals(epic, history.get(1), "Второй должен быть эпик");
        assertEquals(subtask, history.get(2), "Третьей должна быть подзадача");
    }
    
    @Test
    void shouldLimitHistoryToTenTasks() {
        for (int i = 1; i <= 12; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            int id = taskManager.createTask(task);
            taskManager.getTask(id);
        }
        
        List<Task> history = taskManager.getHistory();
        
        assertEquals(10, history.size(), "История должна содержать максимум 10 задач");
        assertEquals("Task 3", history.get(0).getName(), 
            "Первой в истории должна быть Task 3 (самая старая из оставшихся)");
        assertEquals("Task 12", history.get(9).getName(), 
            "Последней в истории должна быть Task 12 (самая новая)");
    }
    
    @Test
    void shouldNotConflictBetweenGeneratedAndManualIds() {
        Task task1 = new Task("Task 1", "Description 1");
        int generatedId = taskManager.createTask(task1);
        
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(999);
        int id2 = taskManager.createTask(task2);
        
        Task task3 = new Task("Task 3", "Description 3");
        int generatedId2 = taskManager.createTask(task3);
        
        assertNotEquals(generatedId, id2, "Сгенерированный id не должен конфликтовать с заданным");
        assertNotEquals(generatedId, generatedId2, "Сгенерированные id должны быть уникальными");
        assertNotEquals(id2, generatedId2, "Заданный id не должен конфликтовать со сгенерированным");
        
        assertNotNull(taskManager.getTask(generatedId), "Задача с сгенерированным id должна быть найдена");
        assertNotNull(taskManager.getTask(id2), "Задача с заданным id должна быть найдена");
        assertNotNull(taskManager.getTask(generatedId2), "Вторая задача с сгенерированным id должна быть найдена");
    }
}