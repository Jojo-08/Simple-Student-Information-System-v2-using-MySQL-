import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CollegeDAO {

    public List<College> getCollegesByPage(int pageNumber, int pageSize)
    {
        
        List<College> colleges = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize; // the college where the list should start
        
        String sql = " SELECT * FROM college LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next())
            {
                College c = new College(
                    rs.getString("college_code"),
                    rs.getString("college_name")         
                );

                colleges.add(c);
            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return colleges;
    }
    
    public void addCollege(College college)
    {
        String sql = "INSERT INTO college(college_code, college_name) VALUES(?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, college.getCollegeCode());
            pstmt.setString(2,college.getCollegeName());

            pstmt.executeUpdate();
            System.out.println("college: " + college.getCollegeCode() + " added");
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateCollege(College college, String collegeCode)
    {
        String sql = "UPDATE college SET college_code = ?, college_name = ? WHERE college_code = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,college.getCollegeCode());
            pstmt.setString(2,college.getCollegeName());
            pstmt.setString(3,collegeCode);

            
            pstmt.executeUpdate();
            System.out.println("college: " + collegeCode + " updated");
        }

        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteCollege(String collegeCode)
    {
        String sql = "DELETE FROM college WHERE college_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setString(1, collegeCode);

            int rowsDeleted = pstmt.executeUpdate();

            System.out.println(rowsDeleted + " college(s) deleted (:<).");
        }
        
        catch(SQLException e)
        {
            e.printStackTrace();
        }


    }

    public List<College> getAllColleges()
    {
        List<College> colleges = new ArrayList<>();
        String sql = "SELECT * FROM college";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery())
        {
            while(rs.next())
            {
                College college = new College(
                    rs.getString("college_code"),
                    rs.getString("college_name")
                );

                colleges.add(college);
                System.out.println("fetched all colleges");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return colleges;
    }

    public College getCollegeByCode( String collegeCode)
    {
        String sql = "SELECT * FROM college WHERE college_code = ?";
        College college = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {

                pstmt.setString(1, collegeCode);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                     college = new College(
                    rs.getString("college_code"),
                    rs.getString("college_name")
                );
                }
                
                System.out.println("fetched colleges by college code");
        } 

        catch (SQLException e) 
        {
            e.printStackTrace();
        }

         return college;
    }

    public List<College> searchCollegesByPage(String keyWord, int pageNumber, int pageSize)
    {   
        List<College> colleges = new ArrayList<>();
        String sql = "SELECT * FROM college WHERE college_code LIKE ? OR college_name LIKE ? LIMIT ?, ?";

        try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyWord + "%";

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setInt(3, (pageNumber -1)*pageSize);
            pstmt.setInt(4,pageSize);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                College c = new College(
                    rs.getString("college_code"),
                    rs.getString("college_name")
                
                );

                colleges.add(c);
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return colleges;
    }
}

