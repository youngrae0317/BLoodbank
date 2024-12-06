package database;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class BloodRecordsDatabase {

    // DBConnect 클래스를 이용한 DB 연결 재사용
    private DBConnect dbConnect;

    public BloodRecordsDatabase() {
        this.dbConnect = new DBConnect();  // DBConnect 객체 생성
    }

    // 헌혈 기록 데이터를 가져오는 메서드
    public List<List<String>> getBloodRecords() {
        List<List<String>> records = new ArrayList<>();
        String query = "SELECT 헌혈기록번호, 회원_ID, 담당직원_ID, 헌혈종류, 헌혈량, TO_CHAR(헌혈일자, 'YYYY-MM-DD') AS 헌혈일자 " +
                "FROM BLOODBANK.헌혈기록 ORDER BY 헌혈기록번호";

        try (Connection conn = dbConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // ResultSet에서 데이터를 읽어와 List에 저장
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString("헌혈기록번호"));
                row.add(rs.getString("회원_ID"));
                row.add(rs.getString("담당직원_ID"));
                row.add(rs.getString("헌혈종류"));
                row.add(rs.getString("헌혈량") + "ml");
                row.add(rs.getString("헌혈일자"));
                records.add(row);
            }

        } catch (SQLException e) {
            // 예외 로깅
            System.err.println("SQL 오류 발생: " + e.getMessage());
        }

        return records;
    }
}
