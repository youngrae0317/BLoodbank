package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RecordInsertDatabase {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "BLOODBANK";
    private static final String DB_PASSWORD = "1234";

    public boolean insertRecord(String memberId, String staffId, String donationDate,
                                String donationType, int donationAmount,
                                int storageValidity, String giftType) {
        String sql = "INSERT INTO BLOODBANK.헌혈기록 " +
                "(헌혈기록번호, 회원_ID, 담당직원_ID, 헌혈일자, 헌혈종류, 헌혈량, 보관유효기간, 증정품종류, 상태) " +
                "VALUES ((SELECT NVL(MAX(헌혈기록번호), 0) + 1 FROM BLOODBANK.헌혈기록), ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, SYSDATE + ?, ?, '검사 중')";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 연결 생성
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 트랜잭션 시작
            conn.setAutoCommit(false); // 자동 커밋 비활성화

            // SQL 준비 및 실행
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.setString(2, staffId);
            pstmt.setString(3, donationDate);
            pstmt.setString(4, donationType);
            pstmt.setInt(5, donationAmount);
            pstmt.setInt(6, storageValidity);
            pstmt.setString(7, giftType);

            int rowsAffected = pstmt.executeUpdate();

            // 트랜잭션 커밋
            conn.commit();

            return rowsAffected > 0;

        } catch (Exception e) {
            // 예외 발생 시 롤백
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // 리소스 정리
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

