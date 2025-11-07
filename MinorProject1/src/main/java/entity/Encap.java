package entity;

public class Encap {
	
	private String empid;
    private String deptid;
    private String empname;
    private String leaveType;
    private String startDate;
    private String endDate;
    private int numLeaves;

    // Getters and setters
    public String getEmpid() 
    { 
    	return empid;
    }
    public void setEmpid(String empid) 
    { 
    	this.empid = empid; 
    }

    public String getDeptid() { return deptid; }
    public void setDeptid(String deptid) { this.deptid = deptid; }

    public String getEmpname() { return empname; }
    public void setEmpname(String empname) { this.empname = empname; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getNumLeaves() { return numLeaves; }
    public void setNumLeaves(int numLeaves) { this.numLeaves = numLeaves; }
}

