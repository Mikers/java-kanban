package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        Task task = new Task("Test", "Test description");
        task.setId(1);

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не должна быть пустой.");
        assertEquals(1, history.size(), "История должна содержать 1 задачу.");
        assertEquals(task, history.getFirst(), "Задача в истории должна совпадать с добавленной.");
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoTasks() {
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не должна быть null");
        assertTrue(history.isEmpty(), "История должна быть пустой");
    }

    @Test
    void shouldAddNullTask() {
        historyManager.add(null);
        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "История должна быть пустой при добавлении null");
    }

    @Test
    void shouldMaintainOrderOfAddition() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task("Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать 3 задачи");
        assertEquals(task1, history.get(0), "Первая задача должна быть task1");
        assertEquals(task2, history.get(1), "Вторая задача должна быть task2");
        assertEquals(task3, history.get(2), "Третья задача должна быть task3");
    }

    @Test
    void shouldNotHaveHistoryLimit() {
        for (int i = 1; i <= 100; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();

        assertEquals(100, history.size(), "История должна содержать все 100 задач");
        assertEquals(1, history.getFirst().getId(), "Первая задача должна иметь id=1");
        assertEquals(100, history.get(99).getId(), "Последняя задача должна иметь id=100");
    }

    @Test
    void shouldRemoveDuplicatesKeepingLastView() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task("Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать 3 уникальные задачи");
        assertEquals(task2, history.get(0), "Первая задача должна быть task2");
        assertEquals(task3, history.get(1), "Вторая задача должна быть task3");
        assertEquals(task1, history.get(2), "Третья задача должна быть task1 (последний просмотр)");
    }

    @Test
    void shouldRemoveFromHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task("Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 задачи после удаления");
        assertEquals(task1, history.get(0), "Первая задача должна быть task1");
        assertEquals(task3, history.get(1), "Вторая задача должна быть task3");
    }

    @Test
    void shouldRemoveFirstTask() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task("Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertEquals(task2, history.get(0), "Первая задача должна быть task2");
        assertEquals(task3, history.get(1), "Вторая задача должна быть task3");
    }

    @Test
    void shouldRemoveLastTask() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task("Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(3);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertEquals(task1, history.get(0), "Первая задача должна быть task1");
        assertEquals(task2, history.get(1), "Вторая задача должна быть task2");
    }

    @Test
    void shouldHandleRemoveFromEmptyHistory() {
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна остаться пустой");
    }

    @Test
    void shouldHandleRemoveNonExistentTask() {
        Task task = new Task("Task", "Description");
        task.setId(1);

        historyManager.add(task);
        historyManager.remove(999);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна остаться неизменной");
        assertEquals(task, history.getFirst(), "Задача должна остаться в истории");
    }

    @Test
    void shouldHandleSingleTaskHistory() {
        Task task = new Task("Task", "Description");
        task.setId(1);

        historyManager.add(task);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления единственной задачи");
    }

    @Test
    void shouldReturnDefensiveCopyOfHistory() {
        Task task = new Task("Task", "Description");
        task.setId(1);
        historyManager.add(task);

        List<Task> history1 = historyManager.getHistory();
        List<Task> history2 = historyManager.getHistory();

        assertNotSame(history1, history2,
            "Метод getHistory() должен возвращать копию списка, а не ссылку на внутренний список");

        history1.clear();
        List<Task> history3 = historyManager.getHistory();

        assertEquals(1, history3.size(),
            "Изменение возвращенного списка не должно влиять на внутреннее состояние");
    }

    @Test
    void shouldWorkWithDifferentTaskTypes() {
        Task task = new Task("Task", "Task description");
        task.setId(1);

        Epic epic = new Epic("Epic", "Epic description");
        epic.setId(2);

        Subtask subtask = new Subtask("Subtask", "Subtask description", 2);
        subtask.setId(3);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать 3 задачи разных типов");
        assertEquals(task, history.get(0), "Первая задача должна быть обычной задачей");
        assertEquals(epic, history.get(1), "Вторая задача должна быть эпиком");
        assertEquals(subtask, history.get(2), "Третья задача должна быть подзадачей");
    }

    @Test
    void shouldHandleComplexDuplicateScenario() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task("Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        historyManager.add(task3);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать 3 уникальные задачи");
        assertEquals(task3, history.get(0), "Первая задача должна быть task3");
        assertEquals(task2, history.get(1), "Вторая задача должна быть task2");
        assertEquals(task1, history.get(2), "Третья задача должна быть task1 (последний просмотр)");
    }
}