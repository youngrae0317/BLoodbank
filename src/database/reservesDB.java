package database;

import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class reservesDB {

    // 헌혈 데이터 조회 메서드
    public static List<Object[]> getBloodReserves(String donationType) {
        List<Object[]> results = new ArrayList<>();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            // 데이터베이스 연결
            conn = DBConnect.connect();

            // 프로시저 호출 준비
            String sql = "{ call 혈액형별_헌혈종류별_보유혈액량_조회(?, ?) }";
            cstmt = conn.prepareCall(sql);

            // 헌혈종류 전달
            cstmt.setString(1, donationType); // '전체' 또는 특정 헌혈종류
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);

            // 프로시저 실행
            cstmt.execute();

            // 결과 커서를 가져오기
            rs = (ResultSet) cstmt.getObject(2);

            // 결과 처리
            while (rs.next()) {
                String bloodType = rs.getString("혈액형");
                String type = rs.getString("헌혈종류");
                int totalAmount = rs.getInt("총보유량");

                // 결과 리스트에 추가
                results.add(new Object[]{bloodType, type, totalAmount});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                if (conn != null) DBConnect.disconnect(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
    public static void main(String[] args) {
        List<Object[]> data = reservesDB.getBloodReserves("전체");

        if (data.isEmpty()) {
            System.out.println("데이터가 없습니다.");
        } else {
            for (Object[] row : data) {
                System.out.println("혈액형: " + row[0] + ", 헌혈종류: " + row[1] + ", 총 보유량: " + row[2]);
            }
        }
    }
}
