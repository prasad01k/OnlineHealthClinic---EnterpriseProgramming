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

public class admin_log extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String uname = req.getParameter("uname");
        String pwd = req.getParameter("pwd");
        PrintWriter pw = res.getWriter();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "admin");

            ps = conn.prepareStatement("select * from admin_login where uname = ? AND pwd = ?");
            ps.setString(1, uname);
            ps.setString(2, pwd);
            rs = ps.executeQuery();
            if (rs.next()) {
            	res.sendRedirect("admin_home.html");
            } else {
            	pw.write("<html><body>");
                pw.write("<script>alert('Login failed');</script>");
                pw.write("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            pw.write("<html><body>");
            pw.write("<script>alert('Login failed');</script>");
            pw.write("</body></html>");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            pw.close();
        }
    }
}
