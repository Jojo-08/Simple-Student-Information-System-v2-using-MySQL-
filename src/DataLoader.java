import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataLoader {

    public static void main(String[] args) {
        loadColleges("src/colleges.csv");
        loadPrograms("src/programs.csv");
        loadStudents("src/students.csv");
    }

    private static void loadColleges(String filePath) {
        String sql = "INSERT INTO college (college_code, college_name) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                pstmt.setString(1, data[0].trim());
                pstmt.setString(2, data[1].trim());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Colleges loaded successfully.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadPrograms(String filePath) {
        String sql = "INSERT INTO program (program_code, program_name, college_code) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                pstmt.setString(1, data[0].trim());
                pstmt.setString(2, data[1].trim());
                pstmt.setString(3, data[2].trim());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Programs loaded successfully.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadStudents(String filePath) {
        String sql = "INSERT INTO student (student_id, first_name, last_name, year_level, gender, program_code) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                pstmt.setString(1, data[0].trim());
                pstmt.setString(2, data[1].trim());
                pstmt.setString(3, data[2].trim());
                pstmt.setInt(4, Integer.parseInt(data[3].trim()));
                pstmt.setString(5, data[4].trim());
                pstmt.setString(6, data[5].trim());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Students loaded successfully.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}