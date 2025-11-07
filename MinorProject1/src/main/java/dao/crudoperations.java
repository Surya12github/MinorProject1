package dao;

import java.sql.*;
import entity.Encap;

public class crudoperations implements Myinterface {

    Connection con = null;

    public crudoperations() {
        try {
            con = Myinstance.getConnection();
            if (con == null || con.isClosed()) {
                System.out.println("Reconnecting to database...");
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Surya", "root", "root");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Create method — inserts a new leave record for every leave request
    public boolean create(Encap e) {
        try {
            int totalLeaves = 20;
            int remainingLeaves = getRemainingLeaves(e); // get remaining before applying

            if (remainingLeaves == -1) {
                // First time applying
                remainingLeaves = totalLeaves;
            }

            remainingLeaves -= e.getNumLeaves();

            if (remainingLeaves < 0)
                remainingLeaves = 0;

            // ✅ Insert each new leave entry into the same table (to preserve history)
            String insertSql = "INSERT INTO employee_leaves (empid, deptid, empname, leave_type, start_date, leave_date, num_leaves, remaining_leaves) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(insertSql);
            ps.setString(1, e.getEmpid());
            ps.setString(2, e.getDeptid());
            ps.setString(3, e.getEmpname());
            ps.setString(4, e.getLeaveType());
            ps.setString(5, e.getStartDate());
            ps.setString(6, e.getEndDate());
            ps.setInt(7, e.getNumLeaves());
            ps.setInt(8, remainingLeaves);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ✅ Get remaining leaves based on previous leave history
    public int getRemainingLeaves(Encap e) {
        int remaining = -1;
        try {
            // Check total leaves used by the employee
            String sql = "SELECT SUM(num_leaves) AS used_leaves FROM employee_leaves WHERE empid=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, e.getEmpid());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int used = rs.getInt("used_leaves");
                int total = 20; // Default total leaves
                remaining = total - used;
                if (remaining < 0)
                    remaining = 0;
            } else {
                remaining = 20; // New employee, full leaves available
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return remaining;
    }
}
