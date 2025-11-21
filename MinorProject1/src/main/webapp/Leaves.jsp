<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Employee Leaves Page</title>

  <style>
    body {
      font-family: 'Poppins', sans-serif;
      background-color: #f4f6f8;
      margin: 40px; /* ✅ space from browser border */
    }

    .container {
      width: 80%;
      margin: 0 auto; /* ✅ center container */
      background: white;
      border-radius: 15px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      padding: 30px 50px;
    }

    h2, h3 {
      text-align: center;
      color: #333;
    }

    .leaves-info {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin: 30px 0;
    }

    .card-box {
      background: #ffb347;
      color: white;
      padding: 20px;
      border-radius: 10px;
      text-align: center;
      font-size: 18px;
      font-weight: bold;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
    }

    .apply-section {
      text-align: center;
      margin-top: 20px;
    }

    select, input[type="date"], input[type="number"], button {
      padding: 10px;
      margin: 10px;
      border-radius: 8px;
      border: 1px solid #ccc;
      font-size: 16px;
    }

    button, input[type="submit"] {
      background-color: orange;
      color: white;
      cursor: pointer;
      font-weight: bold;
      border: none;
      transition: 0.3s;
      padding: 10px 20px;
      border-radius: 8px;
    }

    button:hover, input[type="submit"]:hover {
      background-color: white;
      color: orange;
      border: 1px solid orange;
    }

    .remaining {
      text-align: center;
      margin-top: 30px;
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }
  </style>
</head>
<body>

<%
String empid = (String) session.getAttribute("empid");
String deptid = (String) session.getAttribute("deptid");
String empname = (String) session.getAttribute("empname");
%>

<div class="container">
  <h2>Welcome, <%= empname %></h2>
  <h3>Emp ID: <%= empid %> | Dept ID: <%= deptid %></h3>
  <hr>

  <h2>Leave Management</h2>

  <div class="leaves-info">
    <div class="card-box">Paid Leaves: <span id="paidLeaves">10</span></div>
    <div class="card-box">Sick Leaves: <span id="sickLeaves">10</span></div>
    <div class="card-box">Paternity Leaves: <span id="paternityLeaves">5</span></div>
    <div class="card-box">Maternity Leaves: <span id="maternityLeaves">6</span></div>
  </div>

  <div class="apply-section">
    <h3>Apply for Leave</h3>
    <form id="leaveForm" onsubmit="applyLeave(event)">
      <label for="start_date">Start Date:</label>
      <input type="date" id="start_date" name="start_date" required><br><br>

      <label for="end_date">End Date:</label>
      <input type="date" id="end_date" name="end_date" required><br><br>

      <label for="leave_type">Leave Type:</label>
      <select id="leave_type" name="leave_type" required>
        <option value="">--Select Type--</option>
        <option value="paid">Paid Leave</option>
        <option value="sick">Sick Leave</option>
        <option value="paternity">Paternity Leave</option>
        <option value="maternity">Maternity Leave</option>
      </select><br><br>

      <label for="num_leaves">Number of Leaves:</label>
      <input type="number" id="num_leaves" name="num_leaves" min="1" required><br><br>

      <input type="submit" value="Apply Leave">
    </form>
  </div>
</div>

<script>
let paidLeaves = 10;
let sickLeaves = 10;
let paternityLeaves = 5;
let maternityLeaves = 6;

function applyLeave(event) {
  event.preventDefault();

  const type = document.getElementById("leave_type").value;
  const from = document.getElementById("start_date").value;
  const to = document.getElementById("end_date").value;
  const num = document.getElementById("num_leaves").value;

  if (!type || !from || !to || !num) {
    alert("Please fill all fields!");
    return;
  }

  const fromDate = new Date(from);
  const toDate = new Date(to);
  const diffDays = (toDate - fromDate) / (1000 * 60 * 60 * 24) + 1;

  if (diffDays <= 0) {
    alert("Invalid date range!");
    return;
  }

  // Update frontend
  if (type === "paid" && paidLeaves >= diffDays) paidLeaves -= diffDays;
  else if (type === "sick" && sickLeaves >= diffDays) sickLeaves -= diffDays;
  else if (type === "paternity" && paternityLeaves >= diffDays) paternityLeaves -= diffDays;
  else if (type === "maternity" && maternityLeaves >= diffDays) maternityLeaves -= diffDays;
  else {
    alert("Not enough remaining leaves!");
    return;
  }

  // Display remaining leave counts
  document.getElementById("paidLeaves").textContent = paidLeaves;
  document.getElementById("sickLeaves").textContent = sickLeaves;
  document.getElementById("paternityLeaves").textContent = paternityLeaves;
  document.getElementById("maternityLeaves").textContent = maternityLeaves;

  // Send data to backend
  fetch("<%= request.getContextPath() %>/leaveservlet", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      leave_type: type,
      start_date: from,
      end_date: to,
      num_leaves: diffDays
    })
  })
  .then(response => response.text())
  .then(data => {
    console.log("Response:", data);
    alert("✅ Leave applied successfully!");
  })
  .catch(error => console.error("Error:", error));
}
</script>
</body>
</html>
