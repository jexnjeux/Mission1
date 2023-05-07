<%@ page import="com.example.mission1.repository.PublicWifiService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        div{
            display: flex;
            flex-direction: column;
            align-items: center;
        }
    </style>
</head>
<body>
<%
    PublicWifiService publicWifiService = new PublicWifiService();
    int totalCnt = publicWifiService.insertWifiInfo();
%>
<div>
<h1><%=totalCnt%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
<a href="index.jsp">홈 으로 가기</a>
</div>
</body>
</html>
