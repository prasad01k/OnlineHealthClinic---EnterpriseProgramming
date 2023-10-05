package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoctorLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        PrintWriter pw = res.getWriter();

        // Get the submitted doctor's name and password from the form
        String doctorName = req.getParameter("doctorName");
        String password = req.getParameter("password");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "admin");

            // Check if the submitted doctor's name and password match in the database
            String checkSql = "SELECT * FROM doc_reg WHERE name = ? AND password = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setString(1, doctorName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Doctor's name and password match, login successful
				/*
				 * pw.write("<html><body>"); pw.write("<h2>Login successful! Welcome, " +
				 * doctorName + "!</h2>"); pw.write("</body></html>");
				 */
            	res.sendRedirect("doctor_home.html");
            } else {
                // Doctor's name and password do not match, login failed
                pw.write("<html><body>");
                pw.write("<h2>Login failed. Invalid credentials.</h2>");
                pw.write("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Login failed due to an error
            pw.write("<html><body>");
            pw.write("<h2>Login failed due to an error.</h2>");
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
