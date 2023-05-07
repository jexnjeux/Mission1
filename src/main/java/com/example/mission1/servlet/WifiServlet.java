package com.example.mission1.servlet;

import com.example.mission1.repository.PublicWifiInfo;
import com.example.mission1.repository.PublicWifiService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

@WebServlet(name = "wifi", value="/wifi")
public class WifiServlet extends HttpServlet {
    List<PublicWifiInfo> wifiList = new ArrayList<PublicWifiInfo>();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder res = new StringBuilder();
        String lat = request.getParameter("lat");
        String lnt = request.getParameter("lnt");
        PublicWifiService publicWifiService = new PublicWifiService();
        wifiList = publicWifiService.getWifiList(lat, lnt);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        for (int i = 0; i < wifiList.size(); i++) {
            res.append("<tr>");
            res.append("<td>"+wifiList.get(i).getDistance()+"</td>");
            res.append("<td>"+wifiList.get(i).getMgrNo()+"</td>");
            res.append("<td>"+wifiList.get(i).getWrdofc()+"</td>");
            res.append("<td>"+wifiList.get(i).getMainNm()+"</td>");
            res.append("<td>"+wifiList.get(i).getAddress1()+"</td>");
            res.append("<td>"+wifiList.get(i).getAddress2()+"</td>");
            res.append("<td>"+wifiList.get(i).getFloor()+"</td>");
            res.append("<td>"+wifiList.get(i).getType()+"</td>");
            res.append("<td>"+wifiList.get(i).getOrganization()+"</td>");
            res.append("<td>"+wifiList.get(i).getServiceType()+"</td>");
            res.append("<td>"+wifiList.get(i).getNetworkType()+"</td>");
            res.append("<td>"+wifiList.get(i).getYear()+"</td>");
            res.append("<td>"+wifiList.get(i).getIndoorOutdoor()+"</td>");
            res.append("<td>"+wifiList.get(i).getAccessEnvironment()+"</td>");
            res.append("<td>"+wifiList.get(i).getLat()+"</td>");
            res.append("<td>"+wifiList.get(i).getLnt()+"</td>");
            res.append("<td>"+wifiList.get(i).getDttm()+"</td>");
        }
        response.getWriter().println(res.toString());

    }

}
