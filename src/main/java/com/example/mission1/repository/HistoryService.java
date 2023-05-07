package com.example.mission1.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryService {

    static final String DB_URL = "jdbc:mariadb://localhost:3306/mission1db";
    static final String DB_USER = "mission1user";
    static final String DB_PASSWORD = "zerobase";

    public void insertHistory(String latitude, String longitude) throws SQLException {

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
            String sql = "insert into history (lat, lnt, date) values (?, ?, now()); ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, Double.parseDouble(latitude));
            preparedStatement.setDouble(2, Double.parseDouble(longitude));
            int affectedRows = 0;
            affectedRows += preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("히스토리 저장 성공");
            } else {
                System.out.println("히스토리 저장 실패");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


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

    public List<HistoryInfo> getHistoryList() {
        List<HistoryInfo> historyList = new ArrayList<>();

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
            String sql = "select * from history limit 20 ;";
            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                double lat = rs.getDouble("lat");
                double lnt = rs.getDouble("lnt");
                String date = rs.getString("date");

                HistoryInfo historyInfo = new HistoryInfo();
                historyInfo.setId(id);
                historyInfo.setLat(lat);
                historyInfo.setLnt(lnt);
                historyInfo.setDate(date);

                historyList.add(historyInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
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

        return historyList;
    }

    public void deleteHistory(String id) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = " delete from history where id = ? ; ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(id));

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
}
