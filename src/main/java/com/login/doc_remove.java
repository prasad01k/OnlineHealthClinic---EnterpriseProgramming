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

public class doc_remove extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        PrintWriter pw = res.getWriter();

        // Get selected doctor name from the form
        String selectedDoctor = req.getParameter("doctorName");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "admin");

            // Check if the selected doctor exists in the database
            String checkSql = "SELECT * FROM doc_reg WHERE name = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setString(1, selectedDoctor);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Doctor exists in the database, proceed to remove
                String removeSql = "DELETE FROM doc_reg WHERE name = ?";
                ps = conn.prepareStatement(removeSql);
                ps.setString(1, selectedDoctor);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    // Removal successful
                    pw.write("<html><body>");
                    pw.write("<h2>Doctor removal successful!</h2>");
                    pw.write("</body></html>");
                } else {
                    // Removal failed
                    pw.write("<html><body>");
                    pw.write("<h2>Doctor removal failed.</h2>");
                    pw.write("</body></html>");
                }
            } else {
                // Doctor does not exist in the database
                pw.write("<html><body>");
                pw.write("<h2>Doctor not found.</h2>");
                pw.write("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Removal failed
            pw.write("<html><body>");
            pw.write("<h2>Doctor removal failed.</h2>");
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
