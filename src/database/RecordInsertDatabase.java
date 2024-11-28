package database;

import java.sql.*;
import java.util.Vector;

public class ReservationDatabase {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // DB URL
    private static final String USER = "BLOODBANK"; // 사용자 이름
    private static final String PASSWORD = "1234"; // 비밀번호

    public static Vector<Vector<Object>> getFilteredReservations(String startDateTime, String endDateTime, String staffId) {
        Vector<Vector<Object>> data = new Vector<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            StringBuilder query = new StringBuilder("SELECT 예약번호, 회원_ID, 예약일시, 예약이름, 상태, 담당예약직원_ID FROM BLOODBANK.예약내역 WHERE 1=1");

            if (startDateTime != null && endDateTime != null) {
                query.append(" AND 예약일시 BETWEEN TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')");
            }

            if (staffId != null && !staffId.isEmpty()) {
                query.append(" AND 담당예약직원_ID = ?");
            }

            preparedStatement = connection.prepareStatement(query.toString());

            int index = 1;
            if (startDateTime != null && endDateTime != null) {
                preparedStatement.setString(index++, startDateTime);
                preparedStatement.setString(index++, endDateTime);
            }
            if (staffId != null && !staffId.isEmpty()) {
                preparedStatement.setString(index++, staffId);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("예약번호"));
                row.add(resultSet.getString("회원_ID"));
                row.add(resultSet.getTimestamp("예약일시"));
                row.add(resultSet.getString("예약이름"));
                row.add(resultSet.getString("상태"));
                row.add(resultSet.getString("담당예약직원_ID"));
                data.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
