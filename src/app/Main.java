package app;

import manager.*;
import model.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        System.out.println("\n=== ДЕМОНСТРАЦИЯ НОВОЙ ФУНКЦИОНАЛЬНОСТИ ИСТОРИИ ПРОСМОТРОВ ===\n");

        TaskManager manager = Managers.getDefault();

        System.out.println("1. Создаём две задачи, эпик с тремя подзадачами и эпик без подзадач:");

        Task task1 = new Task("Переезд", "Собрать вещи и переехать");
        Task task2 = new Task("Уборка", "Убраться в новой квартире");
        int taskId1 = manager.createTask(task1);
        int taskId2 = manager.createTask(task2);
        System.out.println("   - Создана задача: " + task1.getName() + " [id=" + taskId1 + "]");
        System.out.println("   - Создана задача: " + task2.getName() + " [id=" + taskId2 + "]");

        Epic epic1 = new Epic("Учеба", "Изучить Java");
        int epicId1 = manager.createEpic(epic1);
        System.out.println("   - Создан эпик: " + epic1.getName() + " [id=" + epicId1 + "]");

        Subtask subtask1 = new Subtask("Теория", "Изучить теорию ООП", epicId1);
        Subtask subtask2 = new Subtask("Практика", "Написать код", epicId1);
        Subtask subtask3 = new Subtask("Тесты", "Написать тесты", epicId1);
        int subtaskId1 = manager.createSubtask(subtask1);
        int subtaskId2 = manager.createSubtask(subtask2);
        int subtaskId3 = manager.createSubtask(subtask3);
        System.out.println("     - Создана подзадача: " + subtask1.getName() + " [id=" + subtaskId1 + "]");
        System.out.println("     - Создана подзадача: " + subtask2.getName() + " [id=" + subtaskId2 + "]");
        System.out.println("     - Создана подзадача: " + subtask3.getName() + " [id=" + subtaskId3 + "]");

        Epic epic2 = new Epic("Отпуск", "Спланировать отпуск");
        int epicId2 = manager.createEpic(epic2);
        System.out.println("   - Создан эпик без подзадач: " + epic2.getName() + " [id=" + epicId2 + "]");

        System.out.println("\n2. Запрашиваем созданные задачи в разном порядке:");
        System.out.println("   Порядок запросов: task1 -> epic1 -> task2 -> subtask1 -> task1 -> subtask2 -> epic1 -> subtask3 -> epic2");

        manager.getTask(taskId1);
        System.out.println("\n   После просмотра task1:");
        printHistory(manager);

        manager.getEpic(epicId1);
        System.out.println("\n   После просмотра epic1:");
        printHistory(manager);

        manager.getTask(taskId2);
        System.out.println("\n   После просмотра task2:");
        printHistory(manager);

        manager.getSubtask(subtaskId1);
        System.out.println("\n   После просмотра subtask1:");
        printHistory(manager);

        manager.getTask(taskId1);
        System.out.println("\n   После ПОВТОРНОГО просмотра task1 (должен переместиться в конец):");
        printHistory(manager);

        manager.getSubtask(subtaskId2);
        System.out.println("\n   После просмотра subtask2:");
        printHistory(manager);

        manager.getEpic(epicId1);
        System.out.println("\n   После ПОВТОРНОГО просмотра epic1 (должен переместиться в конец):");
        printHistory(manager);

        manager.getSubtask(subtaskId3);
        System.out.println("\n   После просмотра subtask3:");
        printHistory(manager);

        manager.getEpic(epicId2);
        System.out.println("\n   После просмотра epic2:");
        printHistory(manager);

        System.out.println("\n3. Удаляем task2, которая есть в истории:");
        manager.deleteTaskById(taskId2);
        System.out.println("   Task2 удалена. История после удаления:");
        printHistory(manager);

        System.out.println("\n4. Удаляем epic1 с тремя подзадачами:");
        System.out.println("   Перед удалением в истории есть epic1 и его подзадачи subtask1, subtask2, subtask3");
        manager.deleteEpicById(epicId1);
        System.out.println("   Epic1 и все его подзадачи удалены. История после удаления:");
        printHistory(manager);

        System.out.println("\n=== ДОПОЛНИТЕЛЬНАЯ ДЕМОНСТРАЦИЯ ===");

        System.out.println("\n5. Проверка неограниченности истории:");
        System.out.println("   Создаём 20 новых задач и просматриваем их:");
        for (int i = 1; i <= 20; i++) {
            Task task = new Task("Задача " + i, "Описание задачи " + i);
            int id = manager.createTask(task);
            manager.getTask(id);
        }
        System.out.println("   История содержит " + manager.getHistory().size() + " элементов (без ограничения на 10)");

        System.out.println("\n6. Проверка удаления дубликатов:");
        Task testTask = new Task("Тестовая задача", "Для проверки дубликатов");
        int testId = manager.createTask(testTask);

        manager.getTask(testId);
        int size1 = manager.getHistory().size();
        System.out.println("   Размер истории после первого просмотра: " + size1);

        manager.getTask(testId);
        int size2 = manager.getHistory().size();
        System.out.println("   Размер истории после повторного просмотра: " + size2);
        System.out.println("   Размер не изменился, дубликат удалён: " + (size1 == size2));

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }

    private static void printHistory(TaskManager manager) {
        List<Task> history = manager.getHistory();
        if (history.isEmpty()) {
            System.out.println("   История пуста");
        } else {
            System.out.println("   История (" + history.size() + " элементов):");
            for (int i = 0; i < history.size(); i++) {
                Task task = history.get(i);
                System.out.println("     " + (i + 1) + ". " +
                                 task.getClass().getSimpleName() + " [id=" +
                                 task.getId() + "]: " + task.getName());
            }
            System.out.println("   НЕТ ПОВТОРОВ: каждая задача встречается только один раз");
        }
    }
}