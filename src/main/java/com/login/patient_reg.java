package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class patient_reg extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        PrintWriter pw = res.getWriter();

        // Get doctor registration form inputs
        String name = req.getParameter("name");
        String uname = req.getParameter("uname");
        String email = req.getParameter("email");
        String phonenumber = req.getParameter("phonenumber");
        String password = req.getParameter("password");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "admin");

            // Prepare the SQL statement to insert doctor registration values into the database
            String sql = "INSERT INTO patient_reg (name, uname, email, phonenumber, password) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, uname);
            ps.setString(3, email);
            ps.setString(4, phonenumber);
            ps.setString(5,  password);

            // Execute the SQL statement to insert the values into the database
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Registration successful
            	res.sendRedirect("patient_login.html");
				/*
				 * pw.write("<html><body>");
				 * pw.write("<h2>Patient registration successful!</h2>");
				 * pw.write("</body></html>");
				 */
            } else {
                // Registration failed
                pw.write("<html><body>");
                pw.write("<h2>Patient registration failed.</h2>");
                pw.write("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Registration failed
            pw.write("<html><body>");
            pw.write("<h2>Patient registration failed.</h2>");
            pw.write("</body></html>");
        } finally {
            // Close resources
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            pw.close();
        }
    }
}
