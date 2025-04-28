public class College {

    private String collegeCode;
    private String collegeName;

   public College(String collegeCode, String collegeName)
   {
    this.collegeCode = collegeCode;
    this.collegeName = collegeName;
   }

   public String getCollegeCode()
   {
    return collegeCode;
   }

   public String getCollegeName()
   {
    return collegeName;
   }

   public void setCollegeCode(String newCode)
   {
    this.collegeCode = newCode;
   }

   public void setCollegeName (String newName)
   {
    this.collegeName = newName;
   }

   public void displayInfo()
   {
    System.out.println("College Code: " + collegeCode);
    System.out.println("College Name: " + collegeName);
   }
}
