package com.example.mission1.repository;

import com.google.gson.*;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicWifiService {
    static final String DB_URL = "jdbc:mariadb://localhost:3306/mission1db";
    static final String DB_USER = "mission1user";
    static final String DB_PASSWORD = "zerobase";
    static final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";

    String service = "TbPublicWifiInfo";

    public int getTotalCnt() throws MalformedURLException {
        int listTotalCount = 0;

        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/").append(URLEncoder.encode("484b4a65636272693831796d6c4d4e", StandardCharsets.UTF_8));
        urlBuilder.append("/").append(URLEncoder.encode("json", StandardCharsets.UTF_8));
        urlBuilder.append("/").append(URLEncoder.encode(service, StandardCharsets.UTF_8));
        urlBuilder.append("/").append(URLEncoder.encode("1", StandardCharsets.UTF_8));
        urlBuilder.append("/").append(URLEncoder.encode("1", StandardCharsets.UTF_8));

        OkHttpClient client = new OkHttpClient();
        com.google.gson.Gson gson = new Gson();
        URL url = new URL(urlBuilder.toString());
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            JsonObject jsonObject = gson.fromJson(res, JsonObject.class);
            listTotalCount = jsonObject.getAsJsonObject(service).getAsJsonPrimitive("list_total_count").getAsInt();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listTotalCount;
    }

    public void deleteWifiInfo() {
        try {
            Class.forName(DB_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = " delete from wifi_info ";
            preparedStatement = connection.prepareStatement(sql);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제할 데이터 없음");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public int insertWifiInfo() throws MalformedURLException, UnsupportedEncodingException {

        int totalCnt = getTotalCnt();

        deleteWifiInfo();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        for (int i = 0; i < totalCnt / 1000; i++) {
            int sIndex = i * 1000 + 1;
            int eIndex = Math.min((i + 1) * 1000, totalCnt);

            StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
            urlBuilder.append("/").append(URLEncoder.encode("484b4a65636272693831796d6c4d4e", StandardCharsets.UTF_8));
            urlBuilder.append("/").append(URLEncoder.encode("json", StandardCharsets.UTF_8));
            urlBuilder.append("/").append(URLEncoder.encode(service, StandardCharsets.UTF_8));
            urlBuilder.append("/").append(URLEncoder.encode(Integer.toString(sIndex), StandardCharsets.UTF_8));
            urlBuilder.append("/").append(URLEncoder.encode(Integer.toString(eIndex), StandardCharsets.UTF_8));

            OkHttpClient client = new OkHttpClient();
            com.google.gson.Gson gson = new Gson();
            URL url = new URL(urlBuilder.toString());
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                String res = response.body().string();
                JsonObject jsonObject = gson.fromJson(res, JsonObject.class);
                JsonArray rows = jsonObject.getAsJsonObject("TbPublicWifiInfo").getAsJsonArray("row");

                Class.forName(DB_DRIVER_CLASS);


                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);


                String sql = "insert into wifi_info (mgr_no, wrdofc, main_nm, address1, address2, floor, type, organization, service_type, network_type, year, indoor_outdoor, access_environment, lat, lnt, dttm)" +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE wrdofc = VALUES(wrdofc)";

                preparedStatement = connection.prepareStatement(sql);
                int affectedRows = 0;

                for (int l = 0; l < rows.size(); l++) {
                    JsonObject row = rows.get(l).getAsJsonObject();
                    preparedStatement.setString(1, row.get("X_SWIFI_MGR_NO").getAsString().replaceAll("-", ""));
                    preparedStatement.setString(2, row.get("X_SWIFI_WRDOFC").getAsString());
                    preparedStatement.setString(3, row.get("X_SWIFI_MAIN_NM").getAsString());
                    preparedStatement.setString(4, row.get("X_SWIFI_ADRES1").getAsString());
                    preparedStatement.setString(5, row.get("X_SWIFI_ADRES2").getAsString());
                    preparedStatement.setString(6, row.get("X_SWIFI_INSTL_FLOOR").getAsString());
                    preparedStatement.setString(7, row.get("X_SWIFI_INSTL_TY").getAsString());
                    preparedStatement.setString(8, row.get("X_SWIFI_INSTL_MBY").getAsString());
                    preparedStatement.setString(9, row.get("X_SWIFI_SVC_SE").getAsString());
                    preparedStatement.setString(10, row.get("X_SWIFI_CMCWR").getAsString());
                    preparedStatement.setString(11, row.get("X_SWIFI_CNSTC_YEAR").getAsString());
                    preparedStatement.setString(12, row.get("X_SWIFI_INOUT_DOOR").getAsString());
                    preparedStatement.setString(13, row.get("X_SWIFI_REMARS3").getAsString());
                    preparedStatement.setDouble(14, Math.max(row.get("LAT").getAsDouble(), row.get("LNT").getAsDouble()));
                    preparedStatement.setDouble(15, Math.min(row.get("LAT").getAsDouble(), row.get("LNT").getAsDouble()));
                    preparedStatement.setString(16, row.get("WORK_DTTM").getAsString());
                    affectedRows += preparedStatement.executeUpdate();

                }

                if (affectedRows > 0) {
                    System.out.println("저장 성공" + i);
                } else {
                    System.out.println("저장 실패");
                }


            } catch (IOException | ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (rs != null && !rs.isClosed()) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (preparedStatement != null && !preparedStatement.isClosed()) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        return totalCnt;
    }

    public List<PublicWifiInfo> getWifiList(Double latitude, Double longitude) {
        List<PublicWifiInfo> wifiList = new ArrayList<>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);


            String sql = "SELECT                           mgr_no," +
                    "                           wrdofc," +
                    "                    main_nm," +
                    "                       address1," +
                    "                          mgr_no," +
                    "                          wrdofc," +
                    "                           main_nm," +
                    "                           address1," +
                    "                           address2," +
                    "                           floor," +
                    "                           type," +
                    "                           organization," +
                    "                           service_type," +
                    "                           network_type," +
                    "                           year," +
                    "                           indoor_outdoor," +
                    "                           access_environment," +
                    "                           lnt," +
                    "                           lat," +
                    "                           dttm," +
                    "                           ST_Distance_Sphere(Point(?, ?), POINT(wifi_info.lat, wifi_info.lnt)) AS distance" +
                    "                     FROM wifi_info" +
                    "                    HAVING distance <= 2000" +
                    "                     order by distance" +
                    "                       limit 20";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, longitude);
            preparedStatement.setDouble(2, latitude);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String mgrNo = rs.getString("mgr_no");
                String wrdofc = rs.getString("wrdofc");
                String mainNm = rs.getString("main_nm");
                String address1 = rs.getString("address1");
                String address2 = rs.getString("address2");
                String floor = rs.getString("floor");
                String type = rs.getString("type");
                String organization = rs.getString("organization");
                String serviceType = rs.getString("service_type");
                String networkType = rs.getString("network_type");
                String year = rs.getString("year");
                String indoorOutdoor = rs.getString("indoor_outdoor");
                String accessEnvironment = rs.getString("access_environment");
                Double lnt = rs.getDouble("lnt");
                Double lat = rs.getDouble("lat");
                String dttm = rs.getString("dttm");
                Double distance = Double.parseDouble(String.format("%.3f", rs.getDouble("distance") / 1000.0));

                PublicWifiInfo wifiInfo = new PublicWifiInfo();

                wifiInfo.setMgrNo(mgrNo);
                wifiInfo.setWrdofc(wrdofc);
                wifiInfo.setMainNm(mainNm);
                wifiInfo.setAddress1(address1);
                wifiInfo.setAddress2(address2);
                wifiInfo.setFloor(floor);
                wifiInfo.setType(type);
                wifiInfo.setOrganization(organization);
                wifiInfo.setServiceType(serviceType);
                wifiInfo.setNetworkType(networkType);
                wifiInfo.setYear(year);
                wifiInfo.setIndoorOutdoor(indoorOutdoor);
                wifiInfo.setAccessEnvironment(accessEnvironment);
                wifiInfo.setLnt(lnt);
                wifiInfo.setLat(lat);
                wifiInfo.setDttm(dttm);
                wifiInfo.setDistance(distance);

                wifiList.add(wifiInfo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return wifiList;
    }
}