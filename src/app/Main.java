package app;

import manager.TaskManager;
import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Переезд", "Организовать переезд в новую квартиру");
        Task task2 = new Task("Покупки", "Купить продукты на неделю");
        
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        Epic epic1 = new Epic("Ремонт квартиры", "Сделать полный ремонт в квартире");
        int epic1Id = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Покупка материалов", "Купить краску и обои", epic1Id);
        Subtask subtask2 = new Subtask("Покраска стен", "Покрасить все стены в квартире", epic1Id);
        
        int subtask1Id = taskManager.createSubtask(subtask1);
        int subtask2Id = taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Изучение Java", "Освоить программирование на Java");
        int epic2Id = taskManager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Прочитать книгу", "Прочитать 'Effective Java'", epic2Id);
        int subtask3Id = taskManager.createSubtask(subtask3);

        System.out.println("=== Все задачи ===");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\n=== Все эпики ===");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\n=== Все подзадачи ===");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);

        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);

        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);

        subtask3.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask3);

        System.out.println("\n=== После изменения статусов ===");
        System.out.println("Задача 1: " + taskManager.getTaskById(task1Id));
        System.out.println("Эпик 1: " + taskManager.getEpicById(epic1Id));
        System.out.println("Эпик 2: " + taskManager.getEpicById(epic2Id));
        System.out.println("Подзадача 1: " + taskManager.getSubtaskById(subtask1Id));
        System.out.println("Подзадача 2: " + taskManager.getSubtaskById(subtask2Id));
        System.out.println("Подзадача 3: " + taskManager.getSubtaskById(subtask3Id));

        taskManager.deleteTaskById(task2Id);
        taskManager.deleteEpicById(epic1Id);

        System.out.println("\n=== После удаления ===");
        System.out.println("Все задачи: " + taskManager.getAllTasks().size());
        System.out.println("Все эпики: " + taskManager.getAllEpics().size());
        System.out.println("Все подзадачи: " + taskManager.getAllSubtasks().size());

        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }
}
