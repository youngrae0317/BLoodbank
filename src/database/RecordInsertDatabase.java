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

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, memberId);
            pstmt.setString(2, staffId);
            pstmt.setString(3, donationDate);
            pstmt.setString(4, donationType);
            pstmt.setInt(5, donationAmount);
            pstmt.setInt(6, storageValidity);
            pstmt.setString(7, giftType);

            int rowsAffected = pstmt.executeUpdate(); // 실행된 행의 개수 반환
            return rowsAffected > 0; // 성공적으로 실행된 경우 true 반환

        } catch (Exception e) {
            e.printStackTrace();
            return false; // 오류 발생 시 false 반환
        }
    }
}
