import java.sql.*;
import java.util.*;

public class Database {

    private static final String URL = "jdbc:sqlite:tasks.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                priority TEXT,
                due_date TEXT,
                status TEXT
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public static void insertTask(String title, String description,
                                  String priority, String dueDate, String status) {
        String sql = "INSERT INTO tasks(title, description, priority, due_date, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, priority);
            pstmt.setString(4, dueDate);
            pstmt.setString(5, status);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting task: " + e.getMessage());
        }
    }

    public static List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error getting tasks: " + e.getMessage());
        }

        return tasks;
    }

    public static void updateTask(int id, String title, String description,
                                  String priority, String dueDate, String status) {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, due_date = ?, status = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, priority);
            pstmt.setString(4, dueDate);
            pstmt.setString(5, status);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    public static void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }

    public static List<Task> getTasksByStatus(String status) {
        String sql = "SELECT * FROM tasks WHERE status = ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error filtering by status: " + e.getMessage());
        }

        return tasks;
    }

    public static List<Task> getTasksByPriority(String priority) {
        String sql = "SELECT * FROM tasks WHERE priority = ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, priority);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error filtering by priority: " + e.getMessage());
        }

        return tasks;
    }

    public static List<Task> getTasksSortedByDueDate() {
        String sql = "SELECT * FROM tasks ORDER BY due_date ASC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error sorting by due date: " + e.getMessage());
        }

        return tasks;
    }

    public static List<Task> searchTasks(String keyword) {
        String sql = "SELECT * FROM tasks WHERE title LIKE ? OR description LIKE ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error searching tasks: " + e.getMessage());
        }

        return tasks;
    }

    private static Task mapRowToTask(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String priority = rs.getString("priority");
        String dueDate = rs.getString("due_date");
        String status = rs.getString("status");

        return new Task(id, title, description, priority, dueDate, status);
    }
}