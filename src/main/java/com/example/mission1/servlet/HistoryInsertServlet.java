package com.example.mission1.servlet;

import com.example.mission1.repository.HistoryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "history-insert", value = "/history-insert")
public class HistoryInsertServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lat = request.getParameter("lat");
        String lnt = request.getParameter("lnt");
        HistoryService historyService = new HistoryService();
        response.getWriter().println("ok");
        try {
            historyService.insertHistory(lat, lnt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
