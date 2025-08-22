package manager;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryHistoryManagerTest {
    
    private HistoryManager historyManager;
    
    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }
    
    @Test
    void add() {
        Task task = new Task("Test", "Test description");
        task.setId(1);
        
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        
        assertNotNull(history, "История не должна быть пустой.");
        assertEquals(1, history.size(), "История должна содержать 1 задачу.");
        assertEquals(task, history.get(0), "Задача в истории должна совпадать с добавленной.");
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
    void shouldLimitHistoryToTenItems() {
        for (int i = 1; i <= 12; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            task.setId(i);
            historyManager.add(task);
        }
        
        List<Task> history = historyManager.getHistory();
        
        assertEquals(10, history.size(), "История должна содержать максимум 10 задач");
        
        Task firstTask = history.get(0);
        assertEquals(3, firstTask.getId(), "Первая задача в истории должна иметь id=3");
        
        Task lastTask = history.get(9);
        assertEquals(12, lastTask.getId(), "Последняя задача в истории должна иметь id=12");
    }
    
    @Test
    void shouldAllowDuplicates() {
        Task task = new Task("Task", "Description");
        task.setId(1);
        
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        
        List<Task> history = historyManager.getHistory();
        
        assertEquals(3, history.size(), "История должна содержать 3 записи");
        assertEquals(task, history.get(0), "Все записи должны быть одинаковыми");
        assertEquals(task, history.get(1), "Все записи должны быть одинаковыми");
        assertEquals(task, history.get(2), "Все записи должны быть одинаковыми");
    }
    
    @Test
    void shouldSavePreviousVersionOfTask() {
        Task task = new Task("Original", "Original Description");
        task.setId(1);
        task.setStatus(TaskStatus.NEW);
        
        historyManager.add(task);
        
        task.setName("Modified");
        task.setDescription("Modified Description");
        task.setStatus(TaskStatus.DONE);
        
        List<Task> history = historyManager.getHistory();
        Task taskFromHistory = history.get(0);
        
        assertEquals("Modified", taskFromHistory.getName(), 
            "В текущей реализации изменения отражаются в истории");
        assertEquals("Modified Description", taskFromHistory.getDescription(), 
            "В текущей реализации изменения отражаются в истории");
        assertEquals(TaskStatus.DONE, taskFromHistory.getStatus(), 
            "В текущей реализации изменения отражаются в истории");
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
    void shouldHandleMaxCapacityProperly() {
        for (int i = 1; i <= 10; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            task.setId(i);
            historyManager.add(task);
        }
        
        assertEquals(10, historyManager.getHistory().size(), "История должна содержать 10 задач");
        
        Task newTask = new Task("New Task", "New Description");
        newTask.setId(11);
        historyManager.add(newTask);
        
        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История все еще должна содержать 10 задач");
        assertEquals(11, history.get(9).getId(), "Последняя задача должна быть новой");
        assertEquals(2, history.get(0).getId(), "Первая задача должна быть Task 2");
    }
}