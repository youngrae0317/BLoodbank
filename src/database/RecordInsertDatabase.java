package database;

import java.sql.*;

public class RecordInsertDatabase {

    /**
     * 헌혈 기록을 삽입하는 메서드
     *
     * @param memberId       회원 ID
     * @param staffId        담당 직원 ID
     * @param donationDate   헌혈 일자 (YYYY-MM-DD 형식)
     * @param donationType   헌혈 종류
     * @param donationAmount 헌혈량
     * @param storageValidity 보관 유효기간 (일수)
     * @param giftType       증정품 종류
     * @return 삽입 성공 여부
     */
    public boolean insertRecord(String memberId, String staffId, String donationDate,
                                String donationType, int donationAmount,
                                int storageValidity, String giftType) {
        String sql = "INSERT INTO BLOODBANK.헌혈기록 " +
                "(헌혈기록번호, 회원_ID, 담당직원_ID, 헌혈일자, 헌혈종류, 헌혈량, 보관유효기간, 증정품종류, 상태) " +
                "VALUES ((SELECT NVL(MAX(헌혈기록번호), 0) + 1 FROM BLOODBANK.헌혈기록), ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, SYSDATE + ?, ?, '검사 중')";

        try (Connection conn = DBConnect.connect(); // DB 연결
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 트랜잭션 시작
            conn.setAutoCommit(false); // 자동 커밋 비활성화

            // SQL 준비 및 파라미터 설정
            pstmt.setString(1, memberId);
            pstmt.setString(2, staffId);
            pstmt.setString(3, donationDate);
            pstmt.setString(4, donationType);
            pstmt.setInt(5, donationAmount);
            pstmt.setInt(6, storageValidity);
            pstmt.setString(7, giftType);

            // 쿼리 실행
            int rowsAffected = pstmt.executeUpdate();

            // 트랜잭션 커밋
            conn.commit();

            // 성공적으로 삽입된 경우
            return rowsAffected > 0;

        } catch (SQLException e) {
            // 예외 발생 시 롤백
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
