package database;

import java.sql.*;
import java.util.Vector;

public class BloodRecordsDatabase {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "BLOODBANK";
    private static final String DB_PASSWORD = "1234";

    // 헌혈 기록 데이터를 가져오는 메서드
    public Vector<Vector<String>> getBloodRecords() {
        Vector<Vector<String>> records = new Vector<>();

        String query = "SELECT 헌혈기록번호, 회원_ID, 담당직원_ID, 헌혈종류, 헌혈량, TO_CHAR(헌혈일자, 'YYYY-MM-DD') AS 헌혈일자 " +
                "FROM BLOODBANK.헌혈기록 ORDER BY 헌혈기록번호";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // ResultSet에서 데이터를 읽어와 Vector에 저장
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("헌혈기록번호"));
                row.add(rs.getString("회원_ID"));
                row.add(rs.getString("담당직원_ID"));
                row.add(rs.getString("헌혈종류"));
                row.add(rs.getString("헌혈량") + "ml");
                row.add(rs.getString("헌혈일자"));
                records.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }
}
