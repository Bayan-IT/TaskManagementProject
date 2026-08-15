import java.util.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        Database.createTable(); // إنشاء الجدول

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Task Manager ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Filter Tasks");
            System.out.println("6. Sort Tasks");
            System.out.println("7. Search Tasks");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1:
                    addTask(input);
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    updateTask(input);
                    break;
                case 4:
                    deleteTask(input);
                    break;
                case 5:
                    filterTasks(input);
                    break;
                case 6:
                    sortTasks(input);
                    break;
                case 7:
                    searchTasks(input);
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTask(Scanner input) {
        System.out.print("Title: ");
        String title = input.nextLine();

        System.out.print("Description: ");
        String description = input.nextLine();

        System.out.print("Priority (High/Medium/Low): ");
        String priority = input.nextLine();

        System.out.print("Due Date (YYYY-MM-DD): ");
        String dueDate = input.nextLine();

        System.out.print("Status (To Do/In Progress/Done): ");
        String status = input.nextLine();

        Database.insertTask(title, description, priority, dueDate, status);
        System.out.println("Task added successfully.");
    }

    private static void viewTasks() {
        List<Task> tasks = Database.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        for (Task t : tasks) {
            printTask(t);
        }
    }

    private static void updateTask(Scanner input) {
        System.out.print("Enter Task ID to update: ");
        int id = Integer.parseInt(input.nextLine());

        System.out.print("New Title: ");
        String title = input.nextLine();

        System.out.print("New Description: ");
        String description = input.nextLine();

        System.out.print("New Priority (High/Medium/Low): ");
        String priority = input.nextLine();

        System.out.print("New Due Date (YYYY-MM-DD): ");
        String dueDate = input.nextLine();

        System.out.print("New Status (To Do/In Progress/Done): ");
        String status = input.nextLine();

        Database.updateTask(id, title, description, priority, dueDate, status);
        System.out.println("Task updated successfully.");
    }

    private static void deleteTask(Scanner input) {
        System.out.print("Enter Task ID to delete: ");
        int id = Integer.parseInt(input.nextLine());
        Database.deleteTask(id);
        System.out.println("Task deleted successfully.");
    }

    private static void filterTasks(Scanner input) {
        System.out.println("Filter by:");
        System.out.println("1. Status");
        System.out.println("2. Priority");
        System.out.print("Choose: ");
        int choice = Integer.parseInt(input.nextLine());

        List<Task> tasks = new ArrayList<>();

        if (choice == 1) {
            System.out.print("Enter Status (To Do/In Progress/Done): ");
            String status = input.nextLine();
            tasks = Database.getTasksByStatus(status);
        } else if (choice == 2) {
            System.out.print("Enter Priority (High/Medium/Low): ");
            String priority = input.nextLine();
            tasks = Database.getTasksByPriority(priority);
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task t : tasks) {
            printTask(t);
        }
    }

    private static void sortTasks(Scanner input) {
        System.out.println("Sort by:");
        System.out.println("1. Due Date (ascending)");
        System.out.print("Choose: ");
        int choice = Integer.parseInt(input.nextLine());

        if (choice != 1) {
            System.out.println("Invalid choice.");
            return;
        }

        List<Task> tasks = Database.getTasksSortedByDueDate();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task t : tasks) {
            printTask(t);
        }
    }

    private static void searchTasks(Scanner input) {
        System.out.print("Enter keyword (title/description): ");
        String keyword = input.nextLine();

        List<Task> tasks = Database.searchTasks(keyword);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task t : tasks) {
            printTask(t);
        }
    }

    private static void printTask(Task t) {
        System.out.println("-------------------------");
        System.out.println("ID: " + t.getId());
        System.out.println("Title: " + t.getTitle());
        System.out.println("Description: " + t.getDescription());
        System.out.println("Priority: " + t.getPriority());
        System.out.println("Due Date: " + t.getDueDate());
        System.out.println("Status: " + t.getStatus());
    }
}