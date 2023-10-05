package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class doctor_name_servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection conn = null;
        Statement stmt = null;
        PrintWriter pw = res.getWriter();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "admin");

            stmt = conn.createStatement();
            String sql = "SELECT name FROM doc_reg";
            ResultSet rs = stmt.executeQuery(sql);

            List<String> doctorNames = new ArrayList<>();
            while (rs.next()) {
                String doctorName = rs.getString("name");
                doctorNames.add(doctorName);
                System.out.println("Doctor Name: " + doctorName); // Add this line for logging
            }

            // Convert the list of doctor names to JSON and send it in the response
            res.setContentType("application/json");
            pw.write("[");
            for (int i = 0; i < doctorNames.size(); i++) {
                pw.write("\"" + doctorNames.get(i) + "\"");
                if (i < doctorNames.size() - 1) {
                    pw.write(",");
                }
            }
            pw.write("]");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            pw.close();
        }
    }
}
