

public class Program {
    
    private String programCode;
    private String programName;
    private String collegeCode;

    public Program(String programCode, String programName, String collegeCode)
    {
        this.programCode = programCode;
        this.programName = programName;
        this.collegeCode = collegeCode;
    }

    public String getProgramCode()
    {
        return programCode;
    }

    public String getProgramName()
    {
        return programName;
    }

    public String getCollegeCode()
    {
        return collegeCode;
    }

    public void setProgramCode(String newCode)
    {
        this.programCode = newCode;
    }

    public void setProgramName(String newName)
    {
        this.programName = newName;
    }

    public void setCollegeCode(String newCode)
    {
        this.collegeCode = newCode;
    }

    public void displayInfo()
    {
        System.out.println("Program Code: " + programCode);
        System.out.println("Program Name: " + programName );
        System.out.println("College Code: " + collegeCode);
    }
}
