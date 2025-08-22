package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

class EpicTest {
    
    @Test
    void shouldBeEqualWhenIdIsEqual() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        epic1.setId(1);
        Epic epic2 = new Epic("Epic 2", "Description 2");
        epic2.setId(1);
        
        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }
    
    @Test
    void shouldNotBeEqualWhenIdIsDifferent() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        epic1.setId(1);
        Epic epic2 = new Epic("Epic 2", "Description 2");
        epic2.setId(2);
        
        assertNotEquals(epic1, epic2, "Эпики с разными id не должны быть равны");
    }
    
    @Test
    void shouldNotAddItselfAsSubtask() {
        Epic epic = new Epic("Epic", "Description");
        epic.setId(1);
        
        assertThrows(IllegalArgumentException.class,
                () -> epic.addSubtaskId(1),
                "Должно выбрасываться исключение при попытке добавить Epic в самого себя как подзадачу");
    }
    
    @Test
    void shouldInitializeWithEmptySubtaskList() {
        Epic epic = new Epic("Epic", "Description");
        List<Integer> subtasks = epic.getSubtaskIds();
        
        assertNotNull(subtasks, "Список подзадач не должен быть null");
        assertTrue(subtasks.isEmpty(), "Новый эпик должен иметь пустой список подзадач");
    }
    
    @Test
    void shouldAddSubtaskId() {
        Epic epic = new Epic("Epic", "Description");
        epic.addSubtaskId(2);
        epic.addSubtaskId(3);
        
        List<Integer> subtasks = epic.getSubtaskIds();
        assertEquals(2, subtasks.size(), "Должно быть добавлено 2 подзадачи");
        assertTrue(subtasks.contains(2), "Должна содержаться подзадача с id=2");
        assertTrue(subtasks.contains(3), "Должна содержаться подзадача с id=3");
    }
    
    @Test
    void shouldRemoveSubtaskId() {
        Epic epic = new Epic("Epic", "Description");
        epic.addSubtaskId(2);
        epic.addSubtaskId(3);
        epic.removeSubtaskId(2);
        
        List<Integer> subtasks = epic.getSubtaskIds();
        assertEquals(1, subtasks.size(), "Должна остаться 1 подзадача");
        assertFalse(subtasks.contains(2), "Подзадача с id=2 должна быть удалена");
        assertTrue(subtasks.contains(3), "Подзадача с id=3 должна остаться");
    }
    
    @Test
    void shouldSetSubtaskIds() {
        Epic epic = new Epic("Epic", "Description");
        List<Integer> newSubtasks = new ArrayList<>();
        newSubtasks.add(4);
        newSubtasks.add(5);
        
        epic.setSubtaskIds(newSubtasks);
        
        List<Integer> subtasks = epic.getSubtaskIds();
        assertEquals(2, subtasks.size(), "Должно быть 2 подзадачи");
        assertTrue(subtasks.contains(4), "Должна содержаться подзадача с id=4");
        assertTrue(subtasks.contains(5), "Должна содержаться подзадача с id=5");
    }
    
    @Test
    void shouldHandleNullSubtaskIds() {
        Epic epic = new Epic("Epic", "Description");
        epic.addSubtaskId(1);
        epic.setSubtaskIds(null);
        
        List<Integer> subtasks = epic.getSubtaskIds();
        assertNotNull(subtasks, "Список подзадач не должен быть null");
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пустым после установки null");
    }
    
    @Test
    void shouldReturnDefensiveCopyOfSubtaskIds() {
        Epic epic = new Epic("Epic", "Description");
        epic.addSubtaskId(1);
        
        List<Integer> subtasks1 = epic.getSubtaskIds();
        List<Integer> subtasks2 = epic.getSubtaskIds();
        
        assertNotSame(subtasks1, subtasks2, 
            "Метод должен возвращать новый список, а не ссылку на внутренний");
    }
}