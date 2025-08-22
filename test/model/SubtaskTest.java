package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    
    @Test
    void shouldBeEqualWhenIdIsEqual() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 2);
        subtask2.setId(1);
        
        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id должны быть равны");
    }
    
    @Test
    void shouldNotBeEqualWhenIdIsDifferent() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 1);
        subtask2.setId(2);
        
        assertNotEquals(subtask1, subtask2, "Подзадачи с разными id не должны быть равны");
    }
    
    @Test
    void shouldNotMakeItselfAsEpic() {
        Subtask subtask = new Subtask("Subtask", "Description", 10);
        subtask.setId(5);
        
        assertThrows(IllegalArgumentException.class,
                () -> subtask.setEpicId(5),
                "Должно выбрасываться исключение при попытке сделать Subtask своим же эпиком");
    }
    
    @Test
    void shouldStoreEpicId() {
        Subtask subtask = new Subtask("Subtask", "Description", 10);
        assertEquals(10, subtask.getEpicId(), "Должен сохраняться epicId");
    }
    
    @Test
    void shouldUpdateEpicId() {
        Subtask subtask = new Subtask("Subtask", "Description", 10);
        subtask.setEpicId(20);
        assertEquals(20, subtask.getEpicId(), "EpicId должен обновляться");
    }
    
    @Test
    void shouldInitializeWithEpicId() {
        Subtask subtask = new Subtask("Subtask", "Description", 15);
        assertEquals(15, subtask.getEpicId(), 
            "Подзадача должна инициализироваться с указанным epicId");
    }
    
    @Test
    void shouldInheritTaskBehavior() {
        Subtask subtask = new Subtask("Subtask", "Description", 1);
        
        assertEquals("Subtask", subtask.getName());
        assertEquals("Description", subtask.getDescription());
        assertEquals(TaskStatus.NEW, subtask.getStatus());
        
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
    }
    
    @Test
    void shouldCreateWithAllParameters() {
        Subtask subtask = new Subtask(1, "Subtask", "Description", TaskStatus.DONE, 10);
        
        assertEquals(1, subtask.getId());
        assertEquals("Subtask", subtask.getName());
        assertEquals("Description", subtask.getDescription());
        assertEquals(TaskStatus.DONE, subtask.getStatus());
        assertEquals(10, subtask.getEpicId());
    }
    
    @Test
    void shouldHandleEqualityWithDifferentEpicIds() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", 10);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 20);
        subtask2.setId(1);
        
        assertEquals(subtask1, subtask2, 
            "Подзадачи с одинаковым id должны быть равны независимо от epicId");
    }
}