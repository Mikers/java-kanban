package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    
    @Test
    void shouldBeEqualWhenIdIsEqual() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2"); 
        task2.setId(1);
        
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }
    
    @Test
    void shouldNotBeEqualWhenIdIsDifferent() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        
        assertNotEquals(task1, task2, "Задачи с разными id не должны быть равны");
    }
    
    @Test
    void shouldHaveSameHashCodeWhenIdIsEqual() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(1);
        
        assertEquals(task1.hashCode(), task2.hashCode(), 
            "Задачи с одинаковым id должны иметь одинаковый hashCode");
    }
    
    @Test
    void shouldNotAcceptNullName() {
        assertThrows(IllegalArgumentException.class, () ->
            new Task(null, "Description")
        , "Должно выбрасываться исключение при null имени");
    }
    
    @Test
    void shouldNotAcceptEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
            new Task("", "Description")
        , "Должно выбрасываться исключение при пустом имени");
    }
    
    @Test
    void shouldNotAcceptNullDescription() {
        assertThrows(IllegalArgumentException.class, () ->
            new Task("Task", null)
        , "Должно выбрасываться исключение при null описании");
    }
    
    @Test
    void shouldInitializeWithNewStatus() {
        Task task = new Task("Task", "Description");
        assertEquals(TaskStatus.NEW, task.getStatus(), 
            "Новая задача должна иметь статус NEW");
    }
    
    @Test
    void shouldUpdateStatus() {
        Task task = new Task("Task", "Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), 
            "Статус задачи должен обновляться");
        
        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus(), 
            "Статус задачи должен обновляться");
    }
    
    @Test
    void shouldUpdateName() {
        Task task = new Task("Original", "Description");
        task.setName("Updated");
        assertEquals("Updated", task.getName(), 
            "Имя задачи должно обновляться");
    }
    
    @Test
    void shouldUpdateDescription() {
        Task task = new Task("Task", "Original");
        task.setDescription("Updated");
        assertEquals("Updated", task.getDescription(), 
            "Описание задачи должно обновляться");
    }
}