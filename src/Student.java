public class Student {

   private  String studentId;
   private  String firstName;
   private  String lastName;
   private int yearLevel;
   private String  gender;
   private String  programCode;

    public Student(String studentId, String firstName, String lastName, int yearLevel,
                    String gender, String programCode)
    {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearLevel = yearLevel;
        this.gender = gender;
        this.programCode = programCode;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getYearLevel()
    {
        return yearLevel;
    }

    public String getGender()
    {
        return gender;
    }

    public String getProgramCode()
    {
        return programCode;
    }

    public void setStudentId(String newId)
    {
        this.studentId = newId;
    }

    public void setFirstName(String newFirstNm)
    {
        this.firstName = newFirstNm;
    }

    public void setLastName(String newLastNm)
    {
        this.lastName = newLastNm;
    }

    public void setYearLevel (int newYrLvl)
    {
        this.yearLevel = newYrLvl;
    }

    public void setGender( String newGndr)
    {
        this.gender = newGndr;
    }

    public void setProgramCode (String newPrgrmCde)
    {
        this.programCode = newPrgrmCde;

    }
    
    public void displayInfo()
    {
        System.out.println("ID: " + studentId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Year: " + yearLevel);
        System.out.println("Gender: " + gender);
        System.out.println("Program Code: " + programCode);
    }
}