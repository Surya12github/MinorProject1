package controller;
import dao.crudoperations;
import entity.Encap;
import dao.Myinstance;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class leaveservlet
 */
@WebServlet("/leaveservlet")
public class leaveservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public leaveservlet() {
        // TODO Auto-generated constructor stub
    	super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		System.out.println("✅ leaveservlet called successfully!");

		

        // ✅ Fetch employee details from session (from LoginServlet)
        //HttpSession session = request.getSession();
        HttpSession session = request.getSession(false);

        if (session == null) {

            response.sendRedirect("emplogin.html");

            return;

        }
        


        String empid = (String) session.getAttribute("empid");
        String deptid = (String) session.getAttribute("deptid");
        String empname = (String) session.getAttribute("empname");
        
        System.out.println(empid+ " "+deptid+" "+empname);
        System.out.println("Faching data");

        // ✅ Fetch leave form data
        String leave_type = request.getParameter("leave_type");
        String start_date = request.getParameter("start_date");
        String end_date = request.getParameter("end_date");
        //int num_leaves = Integer.parseInt(request.getParameter("num_leaves"));
        
        String numLeavesStr = request.getParameter("num_leaves");
        
        System.out.println("leave_type"+leave_type+"start_date" +" "+start_date+" "+end_date);
        System.out.println("Faching data");
        int num_leaves = 0;
        
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        if (numLeavesStr != null && !numLeavesStr.isEmpty()) {
            num_leaves = Integer.parseInt(numLeavesStr);
        } else {
            out.println("<script>alert('⚠️ Please enter number of leaves');</script>");
            return; // stop execution if input missing
        }

        System.out.println("empid = " + empid);
        System.out.println("deptid = " + deptid);
        System.out.println("empname = " + empname);


        // ✅ Prepare entity
        Encap e = new Encap();
        e.setEmpid(empid);
        e.setDeptid(deptid);
        e.setEmpname(empname);
        e.setLeaveType(leave_type);
        e.setStartDate(start_date);
        e.setEndDate(end_date);
        e.setNumLeaves(num_leaves);

        // ✅ Perform database operation
        crudoperations crud = new crudoperations();
        
        

        // Get current remaining leaves before applying
        System.out.println("Before getRemainingLeaves");
        int remaining = crud.getRemainingLeaves(e);
        System.out.println("After getRemainingLeaves, remaining = " + remaining);
        if (remaining == -1) {
            // New employee record — start with default leaves
            remaining = 20; // You can set default total leaves here
        }


        if (remaining >= num_leaves) {
        	e.setNumLeaves(num_leaves);

            boolean inserted = crud.create(e);

            if (inserted) {
                int updatedRemaining = remaining - num_leaves;
                out.println("<script>alert('✅ Leave applied successfully! Remaining Leaves: " + updatedRemaining + "');</script>");
            } else {
                out.println("<script>alert('❌ Failed to apply leave!');</script>");
            }
        } else {
            out.println("<script>alert('❌ Not enough remaining leaves!');</script>");
        }
    }
}
