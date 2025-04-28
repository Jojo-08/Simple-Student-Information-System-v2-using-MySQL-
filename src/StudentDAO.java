
import java.sql.*;
import java.util.*;


public class StudentDAO {
    /* 
    public void insertStudent(Student student)
    {
        String sql = "INSERT INTO student (student_id, first_name, last_name, year_level, gender,program_code) VALUES (?, ?, ?, ?, ?, ?)";

        try (  Connection conn = DatabaseConnection.getConnection();
               PreparedStatement pstmt = conn.prepareStatement(sql))
        {
                pstmt.setString(1, student.getStudentId());
                pstmt.setString(2, student.getFirstName());
                pstmt.setString(3, student.getLastName());
                pstmt.setInt(4, student.getYearLevel());
                pstmt.setString(5,student.getGender());
                pstmt.setString(6, student.getProgramCode());

                int rowsInserted = pstmt.executeUpdate();
                if(rowsInserted > 0)
                {
                    System.out.println("A new student was inserted successfully!");
                }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

     
    }
    */
    public List<Student> getStudentsByPage(int pageNumber, int pageSize)
    {
        List<Student> students = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize; // the student number where the list would start

        String sql = "SELECT * FROM student ORDER BY student_id LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
                pstmt.setInt(1, pageSize);
                pstmt.setInt(2, offset);

                ResultSet rs = pstmt.executeQuery();

                while (rs.next())
                {
                    Student s = new Student(
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("year_level"),
                        rs.getString("gender"),
                        rs.getString("program_code")
                    );

                    students.add(s);
                }
        }
        
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return students;
    }

    /* 
    public void markStudentUnenrolled(String programCode)
    {
        String sql = "UPDATE student SET program_code = 'UNENROLLED' WHERE PROGRAM_CODE = ?";

        try ( Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, programCode);

            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " students marked as Unenrolled.");
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    */

   
    public List<Student> getAllStudents()
    {
        List<Student> students = new ArrayList<>();

        String sql = "Select student_id, first_name, last_name, year_level, gender, program_code FROM student";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery())
        {   
            while(rs.next())
            {
                Student s = new Student(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("year_level"),
                    rs.getString("gender"),
                    rs.getString("program_code")
                );

                students.add(s);
            }
        }

        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return students;
    }

    public void addStudent( Student student)
    {
        String sql ="INSERT INTO student (student_id, first_name, last_name, year_level, gender, program_code) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setInt(4, student.getYearLevel());
            pstmt.setString(5, student.getGender());
            if (student.getProgramCode() == null)
            {
                pstmt.setNull(6, java.sql.Types.VARCHAR);

            }
            else 
            {
                pstmt.setString(6, student.getProgramCode());
            }

            pstmt.executeUpdate();
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public void deleteStudent(String idNumber)
    {
        String sql = "DELETE FROM student WHERE student_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, idNumber);
                pstmt.executeUpdate();
            }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Student getStudentById( String idNumber)
    {
        Student student = null;

        String sql = "SELECT * FROM student WHERE student_id = ?";

        try( Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, idNumber);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if(rs.next())
                {
                    student = new Student( rs.getString("student_id"),
                                           rs.getString("first_name"),
                                           rs.getString("last_name"),
                                           rs.getInt("year_level"),
                                           rs.getString("gender"),
                                           rs.getString("program_code"));

                }
            }
        }

        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return student;
    }

    public void updateStudent(Student student, String idNumber)
    {
        String sql = "UPDATE student SET student_id = ?, first_name = ?, last_name = ?, year_level = ?, gender = ?, program_code = ? WHERE student_id = ? ";
        
        try( Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setInt(4, student.getYearLevel()); 
            pstmt.setString(5, student.getGender());

            if(student.getProgramCode()==null)
            {
                pstmt.setNull(6,java.sql.Types.VARCHAR);
            }
            else
            {
                pstmt.setString(6, student.getProgramCode());
            }
           
            pstmt.setString(7, idNumber);   
            
            pstmt.executeUpdate();
            System.out.println("student " + idNumber + " updated");
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Student> searchStudentsByPage(String keyWord, int pageNumber, int pageSize)
    {   
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE student_id LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR CAST(year_level AS CHAR) LIKE ? OR gender LIKE ? OR program_code LIKE ? LIMIT ?, ?";

        try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyWord + "%";

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5,searchPattern);
            pstmt.setString(6,searchPattern);
            pstmt.setInt(7, (pageNumber -1)*pageSize);
            pstmt.setInt(8,pageSize);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Student s = new Student(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("year_level"),
                    rs.getString("gender"),
                    rs.getString("program_code")
                );
                
                students.add(s);
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return students;
    }
    
}   
