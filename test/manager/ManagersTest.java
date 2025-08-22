package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    
    @Test
    void shouldReturnInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        
        assertNotNull(manager, "Менеджер задач не должен быть null");
        assertInstanceOf(InMemoryTaskManager.class, manager, "Должен возвращать экземпляр InMemoryTaskManager");
    }
    
    @Test
    void shouldReturnInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        
        assertNotNull(historyManager, "Менеджер истории не должен быть null");
        assertInstanceOf(InMemoryHistoryManager.class, historyManager, "Должен возвращать экземпляр InMemoryHistoryManager");
    }
    
    @Test
    void shouldReturnNewInstancesEachTime() {
        TaskManager manager1 = Managers.getDefault();
        TaskManager manager2 = Managers.getDefault();
        
        assertNotSame(manager1, manager2, 
            "Каждый вызов getDefault() должен возвращать новый экземпляр TaskManager");
        
        HistoryManager history1 = Managers.getDefaultHistory();
        HistoryManager history2 = Managers.getDefaultHistory();
        
        assertNotSame(history1, history2, 
            "Каждый вызов getDefaultHistory() должен возвращать новый экземпляр HistoryManager");
    }
    
    @Test
    void shouldReturnWorkingTaskManager() {
        TaskManager manager = Managers.getDefault();

        assertNotNull(manager.getTasks(), "Список задач должен быть доступен");
        assertNotNull(manager.getEpics(), "Список эпиков должен быть доступен");
        assertNotNull(manager.getSubtasks(), "Список подзадач должен быть доступен");
        assertNotNull(manager.getHistory(), "История должна быть доступна");

        assertTrue(manager.getTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(manager.getEpics().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач должен быть пустым");
        assertTrue(manager.getHistory().isEmpty(), "История должна быть пустой");
    }
    
    @Test
    void shouldReturnWorkingHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager.getHistory(), "История должна быть доступна");
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой при инициализации");

        model.Task task = new model.Task("Test", "Test description");
        task.setId(1);
        historyManager.add(task);
        
        assertEquals(1, historyManager.getHistory().size(), 
            "После добавления задачи история должна содержать 1 элемент");
    }
}