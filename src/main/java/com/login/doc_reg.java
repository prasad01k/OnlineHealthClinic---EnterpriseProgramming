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

public class doc_reg extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        PrintWriter pw = res.getWriter();

        // Get doctor registration form inputs
        String name = req.getParameter("doctorName");
        String specialization = req.getParameter("specialization");
        String phoneNumber = req.getParameter("phoneNumber");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "admin");

            // Prepare the SQL statement to insert doctor registration values into the database
            String sql = "INSERT INTO doc_reg (name, specialization, phone_number, email, password) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setString(3, phoneNumber);
            ps.setString(4, email);
            ps.setString(5,  password);

            // Execute the SQL statement to insert the values into the database
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Registration successful
                pw.write("<html><body>");
                pw.write("<h2>Doctor registration successful!</h2>");
                pw.write("</body></html>");
            } else {
                // Registration failed
                pw.write("<html><body>");
                pw.write("<h2>Doctor registration failed.</h2>");
                pw.write("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Registration failed
            pw.write("<html><body>");
            pw.write("<h2>Doctor registration failed.</h2>");
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
