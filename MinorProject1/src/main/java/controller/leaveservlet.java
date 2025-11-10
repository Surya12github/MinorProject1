package controller;

import dao.crudoperations;
import entity.Encap;
import dao.Myinstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

@WebServlet("/leaveservlet")
public class leaveservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public leaveservlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("✅ leaveservlet called successfully!");

        // ✅ Step 1: Read JSON body sent from fetch()
        request.setCharacterEncoding("UTF-8");
        String jsonData = new BufferedReader(request.getReader())
                .lines()
                .collect(Collectors.joining());

        System.out.println("📦 Raw JSON from client: " + jsonData);

        // Prevent null / empty JSON
        if (jsonData == null || jsonData.isEmpty()) {
            System.out.println("⚠️ No JSON data received!");
            response.setContentType("text/plain");
            response.getWriter().write("Error: No data received!");
            return;
        }

        // ✅ Step 2: Parse JSON
        JSONObject json = new JSONObject(jsonData);
        String leaveType = json.optString("leave_type", null);
        String startDate = json.optString("start_date", null);
        String endDate = json.optString("end_date", null);
        int numLeaves = json.optInt("num_leaves", 0);

        System.out.println("Leave Type: " + leaveType);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("No. of Leaves: " + numLeaves);

        // ✅ Step 3: Fetch employee details from session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("emplogin.html");
            return;
        }

        String empid = (String) session.getAttribute("empid");
        String deptid = (String) session.getAttribute("deptid");
        String empname = (String) session.getAttribute("empname");

        System.out.println("Session → EmpID: " + empid + ", DeptID: " + deptid + ", EmpName: " + empname);

        // ✅ Step 4: Create entity
        Encap e = new Encap();
        e.setEmpid(empid);
        e.setDeptid(deptid);
        e.setEmpname(empname);
        e.setLeaveType(leaveType);
        e.setStartDate(startDate);
        e.setEndDate(endDate);
        e.setNumLeaves(numLeaves);

        // ✅ Step 5: Database operations
        crudoperations crud = new crudoperations();

        System.out.println("Before getRemainingLeaves()");
        int remaining = crud.getRemainingLeaves(e);
        System.out.println("After getRemainingLeaves(), remaining = " + remaining);

        if (remaining == -1) {
            // New employee record — default total leaves
            remaining = 20;
        }

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        if (remaining >= numLeaves) {
            e.setNumLeaves(numLeaves);
            boolean inserted = crud.create(e);

            if (inserted) {
                int updatedRemaining = remaining - numLeaves;
                System.out.println("✅ Leave applied successfully! Remaining Leaves: " + updatedRemaining);
                out.write("✅ Leave applied successfully! Remaining Leaves: " + updatedRemaining);
            } else {
                System.out.println("❌ Failed to apply leave!");
                out.write("❌ Failed to apply leave!");
            }
        } else {
            System.out.println("❌ Not enough remaining leaves!");
            out.write("❌ Not enough remaining leaves!");
        }
    }
}
