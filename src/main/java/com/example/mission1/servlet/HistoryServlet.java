package com.example.mission1.servlet;

import com.example.mission1.repository.HistoryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name="history", value = "/history-delete")
    public class HistoryServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            HistoryService historyService = new HistoryService();
            response.setCharacterEncoding("UTF-8");
            String id = request.getParameter("id");

            if (id == null || id.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            response.setContentType("text/html");
            response.getWriter().println("ok");
            response.setStatus(HttpServletResponse.SC_OK);
            historyService.deleteHistory(id);
        }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
