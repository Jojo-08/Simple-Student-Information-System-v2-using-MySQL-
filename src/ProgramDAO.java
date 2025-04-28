import java.sql.*;
import java.util.*;

public class ProgramDAO {

    public List<Program> getProgramsByPage (int pageNumber, int pageSize)
    {
        List<Program> programs = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize; // the program where the list should start
        
        String sql = " SELECT * FROM program LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next())
            {
                Program p = new Program(
                    rs.getString("program_code"),
                    rs.getString("program_name"),
                    rs.getString("college_code")
                );

                programs.add(p);
            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return programs;
    }
  
    public List<Program> getAllPrograms()
    {
        List<Program> programs = new ArrayList<>();

        String sql = "SELECT * FROM program";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery())
        {   
            while(rs.next())
            {
                Program p = new Program(
                    rs.getString("program_code"),
                    rs.getString("program_name"),
                    rs.getString("college_code")
                   
                );

                programs.add(p);
            }
        }

        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return programs;
    }

    public void addProgram(Program program)
    {
        String sql = "INSERT INTO program(program_code, program_name, college_code) VALUES(?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, program.getProgramCode());
            pstmt.setString(2, program.getProgramName());
            pstmt.setString(3, program.getCollegeCode());

            pstmt.executeUpdate();
        }

        catch(SQLException e)
        {
            e.printStackTrace();
        }

        System.out.println("program: " + program.getProgramCode() + " added");
    }

    public void deleteProgram (String programCode)
    {
        String sql = "DELETE FROM program WHERE program_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, programCode);
            pstmt.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        System.out.println("Program: " + programCode + " deleted");
    }

    public Program getProgramByProgCode (String programCode)
    {
        Program program = null;

        String sql = "SELECT * FROM program WHERE program_code = ?";

        try( Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, programCode);

            try(ResultSet rs = pstmt.executeQuery())
            {
                if(rs.next())
                {
                    program = new Program(
                        rs.getString("program_code"),
                        rs.getString("program_name"),
                        rs.getString("college_code")
                    );
                }
            }
        }

        catch( SQLException e)
        {
            e.printStackTrace();
        }

        System.out.println("Program: " + programCode + " fetched");
        return program;
    }

    public void updateProgram(Program program, String progCode) //there is a progCode parameter, if ever the program code is edited
    {
        String sql = "UPDATE program SET program_code = ?, program_name = ?, college_code = ? WHERE program_code = ? ";

         try( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, program.getProgramCode());
            pstmt.setString(2, program.getProgramName());
            pstmt.setString(3, program.getCollegeCode());
            pstmt.setString(4, progCode); 
            
            
            
            pstmt.executeUpdate();
            System.out.println("program: " + progCode + " updated");
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
  
    public List<Program> searchProgramsByPage(String keyWord, int pageNumber, int pageSize)
    {   
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT * FROM program WHERE program_code LIKE ? OR program_name LIKE ? OR college_code LIKE ? LIMIT ?, ?";

        try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyWord + "%";

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setInt(4, (pageNumber -1)*pageSize);
            pstmt.setInt(5,pageSize);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Program p = new Program(
                    rs.getString("program_code"),
                    rs.getString("program_name"),
                    rs.getString("college_code")
                   
                );

                programs.add(p);
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return programs;
    }

}
