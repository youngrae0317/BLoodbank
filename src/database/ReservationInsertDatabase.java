package database;

import java.sql.*;

public class ReservationInsertDatabase {
    /**
     * 예약 삽입 메서드
     * @param memberId 회원 ID
     * @param reservationDateTime 예약 일시
     * @param reservationName 예약 이름
     * @param staffId 담당 직원 ID
     * @throws SQLException 데이터베이스 관련 예외
     * @throws ClassNotFoundException JDBC 드라이버 로드 실패 예외
     */
    public static void insertReservation(String memberId, Timestamp reservationDateTime, String reservationName, String staffId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            // 데이터베이스 연결
            connection = DBConnect.connect();
            connection.setAutoCommit(false); // 트랜잭션 시작

            // 저장 프로시저 호출
            String call = "{call BLOODBANK.예약삽입_프로시저(?, ?, ?, ?)}";
            callableStatement = connection.prepareCall(call);

            // 매개변수 설정
            callableStatement.setString(1, memberId);
            callableStatement.setTimestamp(2, reservationDateTime);
            callableStatement.setString(3, reservationName);
            callableStatement.setString(4, staffId);

            // 프로시저 실행
            callableStatement.executeUpdate();

            // 트랜잭션 커밋
            connection.commit();
        } catch (SQLException e) {
            // 예외 발생 시 롤백
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            // 예외를 호출자에게 전달
            throw e;
        } finally {
            // 리소스 정리
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    DBConnect.disconnect(connection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
