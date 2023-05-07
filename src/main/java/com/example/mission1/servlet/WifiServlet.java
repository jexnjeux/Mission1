package com.example.mission1.servlet;

import com.example.mission1.repository.PublicWifiInfo;
import com.example.mission1.repository.PublicWifiService;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "wifi", value="/wifi")
public class WifiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strLat = request.getParameter("lat");
        String strLnt = request.getParameter("lnt");

        Double lat = Double.parseDouble(strLat);
        Double lnt = Double.parseDouble(strLnt);

        PublicWifiService publicWifiService = new PublicWifiService();
        List<PublicWifiInfo> wifiList = publicWifiService.getWifiList(lat, lnt);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new Gson().toJson(wifiList));



    }

}
