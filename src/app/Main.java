package app;

import manager.*;
import model.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Переезд", "Собрать вещи и переехать");
        Task task2 = new Task("Уборка", "Убраться в новой квартире");
        int taskId1 = manager.createTask(task1);
        int taskId2 = manager.createTask(task2);

        Epic epic1 = new Epic("Учеба", "Изучить Java");
        int epicId1 = manager.createEpic(epic1);
        
        Subtask subtask1 = new Subtask("Теория", "Изучить теорию ООП", epicId1);
        Subtask subtask2 = new Subtask("Практика", "Написать код", epicId1);
        Subtask subtask3 = new Subtask("Тесты", "Написать тесты", epicId1);
        int subtaskId1 = manager.createSubtask(subtask1);
        int subtaskId2 = manager.createSubtask(subtask2);
        int subtaskId3 = manager.createSubtask(subtask3);
        

        Epic epic2 = new Epic("Отпуск", "Спланировать отпуск");
        int epicId2 = manager.createEpic(epic2);
        
        System.out.println("\n=== Начальное состояние ===");
        printAllTasks(manager);
        

        Task updateTask1 = manager.getTask(taskId1);
        updateTask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(updateTask1);
        
        Subtask updateSubtask1 = manager.getSubtask(subtaskId1);
        updateSubtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(updateSubtask1);
        
        Subtask updateSubtask2 = manager.getSubtask(subtaskId2);
        updateSubtask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(updateSubtask2);
        
        System.out.println("\n=== После обновления статусов ===");
        printAllTasks(manager);

        System.out.println("\n=== Тестируем историю просмотров ===");

        manager.getTask(taskId1);
        manager.getEpic(epicId1);
        manager.getSubtask(subtaskId1);
        manager.getTask(taskId2);
        manager.getEpic(epicId2);
        manager.getSubtask(subtaskId2);
        manager.getSubtask(subtaskId3);
        
        System.out.println("\nИстория после просмотров:");
        printHistory(manager);

        manager.getTask(taskId1);
        manager.getTask(taskId2);
        manager.getEpic(epicId1);
        manager.getEpic(epicId2);
        
        System.out.println("\nИстория после дополнительных просмотров (макс 10 элементов):");
        printHistory(manager);

        System.out.println("\n=== Удаление задачи ===");
        manager.deleteTaskById(taskId2);
        System.out.println("Удалена задача с id=" + taskId2);
        printAllTasks(manager);

        System.out.println("\n=== Удаление эпика с подзадачами ===");
        manager.deleteEpicById(epicId1);
        System.out.println("Удален эпик с id=" + epicId1 + " и все его подзадачи");
        printAllTasks(manager);

        System.out.println("\n=== Финальная история просмотров ===");
        printHistory(manager);
    }
    
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        
        System.out.println("\nЭпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);
            
            for (Task subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + subtask);
            }
        }
        
        System.out.println("\nПодзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
    }
    
    private static void printHistory(TaskManager manager) {
        List<Task> history = manager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История пуста");
        } else {
            System.out.println("История (всего " + history.size() + " элементов):");
            for (Task task : history) {
                System.out.println("  " + task.getClass().getSimpleName() + " [id=" + 
                               task.getId() + "]: " + task.getName());
            }
        }
    }
}