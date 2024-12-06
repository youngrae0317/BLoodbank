package database;

import java.sql.*;
import java.util.Vector;

public class ReservationDatabase {

    /**
     * 예약 내역을 필터링하여 조회하는 메서드
     *
     * @param startDateTime 시작 일시 (형식: 'YYYY-MM-DD HH24:MI:SS')
     * @param endDateTime   종료 일시 (형식: 'YYYY-MM-DD HH24:MI:SS')
     * @param staffId       담당 예약 직원 ID
     * @return 필터링된 예약 내역
     */
    public static Vector<Vector<Object>> getFilteredReservations(String startDateTime, String endDateTime, String staffId) {
        Vector<Vector<Object>> data = new Vector<>();

        StringBuilder query = new StringBuilder("SELECT 예약번호, 회원_ID, 예약일시, 예약이름, 상태, 담당예약직원_ID " +
                "FROM BLOODBANK.예약내역 WHERE 1=1");

        // 필터 조건 추가
        if (startDateTime != null && endDateTime != null) {
            query.append(" AND 예약일시 BETWEEN TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')");
        }

        if (staffId != null && !staffId.isEmpty()) {
            query.append(" AND 담당예약직원_ID = ?");
        }

        // 자원 관리 및 DB 연결
        try (Connection connection = DBConnect.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {

            int index = 1;
            // 파라미터 설정
            if (startDateTime != null && endDateTime != null) {
                preparedStatement.setString(index++, startDateTime);
                preparedStatement.setString(index++, endDateTime);
            }
            if (staffId != null && !staffId.isEmpty()) {
                preparedStatement.setString(index++, staffId);
            }

            // 쿼리 실행
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
