<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.mission1.repository.HistoryService" %>
<%@ page import="com.example.mission1.repository.HistoryInfo" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        h1 {
            margin: 0;
        }

        ul {
            margin-bottom: 12px;
            padding: 0;
            list-style: none;
        }

        li {
            display: inline-block;
        }

        li:after {
            content: "|";
            margin-left: 4px;
        }

        li:last-child::after {
            content: none;
        }

        table {
            margin-top: 12px;
            width: 100%;
            border-collapse: collapse;
        }

        table, td {
            border: 1px solid lightgray;
        }

        tr:nth-child(even) {
            background-color: #ededed;
        }

        th {
            padding: 12px;
            background-color: green;
            font-size: 13px;
            color: white;
        }

        td {
            padding: 8px;
            text-align: center;
            font-size: 12px;
        }

        form {
            margin: 0;
        }

        input {
            cursor: pointer;
        }

        button{
            cursor: pointer;
        }

        .td-empty {
            padding: 20px;
            border: 1px solid lightgray;
            border-top: none;
        }
    </style>
</head>
<body>
<%
    HistoryService historyService = new HistoryService();
    List<HistoryInfo> historyList = historyService.getHistoryList();
%>
<h1>위치 히스토리 목록</h1>
<ul>
    <li><a href="index.jsp">홈</a></li>
    <li><a href="history.jsp">위치 히스토리 목록</a></li>
    <li><a href="load-wifi.jsp" onclick="getWifiInfo()">open Api 와이파이 정보 가져오기</a></li>
</ul>
<table>
    <thead>
    <tr>
        <th>id</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>조회일자</th>
        <th>비고</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (historyList.size() > 0) {
            for (HistoryInfo history : historyList) {
    %>
    <tr>
        <td><%=history.getId()%>
        </td>
        <td><%=history.getLat()%>
        </td>
        <td><%=history.getLnt()%>
        </td>
        <td><%=history.getDate()%>
        </td>
        <td>
            <button onclick="handleHistoryDelete(<%=history.getId()%>)"> 삭제
            </button>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="5" class="td-empty">저장된 히스토리가 없습니다.</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
    function handleHistoryDelete(id) {
        $.ajax({
            url: "history-delete",
            type: "GET",
            data: {
                id,
            },
            success: (response) => {
                if (response) {
                    alert('삭제되었습니다.');
                    window.location.reload();
                } else {
                    alert("삭제 실패");
                }
            },
            error: () => {
                alert('에러!');
            }
        })
    }

</script>
</body>
</html>