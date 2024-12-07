package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class discardedbloodDatabase {

    public static class DiscardedBlood {
        public int 폐기_ID;
        public int 헌혈기록번호;
        public Date 폐기일;
        public String 폐기사유;

        public DiscardedBlood(int 폐기_ID, int 헌혈기록번호, Date 폐기일, String 폐기사유) {
            this.폐기_ID = 폐기_ID;
            this.헌혈기록번호 = 헌혈기록번호;
            this.폐기일 = 폐기일;
            this.폐기사유 = 폐기사유;
        }
    }

    // 폐기혈액 데이터 전체 조회 메서드
    public List<DiscardedBlood> getAllDiscardedBlood() {
        List<DiscardedBlood> discardedBloodList = new ArrayList<>();
        String query = "SELECT 폐기_ID, 헌혈기록번호, 폐기일, 폐기사유 FROM 폐기혈액";

        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                discardedBloodList.add(new DiscardedBlood(
                        rs.getInt("폐기_ID"),
                        rs.getInt("헌혈기록번호"),
                        rs.getDate("폐기일"),
                        rs.getString("폐기사유")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discardedBloodList;
    }

    // 헌혈기록번호로 폐기혈액 검색 메서드
    public List<DiscardedBlood> searchDiscardedBloodByRecordNumber(int recordNumber) {
        List<DiscardedBlood> discardedBloodList = new ArrayList<>();
        String query = "SELECT 폐기_ID, 헌혈기록번호, 폐기일, 폐기사유 FROM 폐기혈액 WHERE 헌혈기록번호 = ?";

        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, recordNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    discardedBloodList.add(new DiscardedBlood(
                            rs.getInt("폐기_ID"),
                            rs.getInt("헌혈기록번호"),
                            rs.getDate("폐기일"),
                            rs.getString("폐기사유")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discardedBloodList;
    }
}
