package database;

import java.sql.*;
import java.util.HashMap;

public class DetailedInfoDatabase {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // 데이터베이스 URL
    private static final String DB_USER = "BLOODBANK"; // 데이터베이스 사용자명
    private static final String DB_PASSWORD = "1234"; // 데이터베이스 비밀번호

    /**
     * 회원 ID로 헌혈자 정보를 조회하는 메서드
     *
     * @param memberId 회원 ID
     * @return 헌혈자 정보 (HashMap<String, Object>)
     */
    public static HashMap<String, Object> getDonorInfo(String memberId) {
        HashMap<String, Object> donorInfo = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Oracle JDBC 드라이버 로드
            Class.forName("oracle.jdbc.OracleDriver");

            // DB 연결
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL 쿼리
            String query = "SELECT 회원_ID, 이름, 생년월일, 성별, 혈액형, 휴대폰번호, 주소, 헌혈횟수, 마지막헌혈일, 헌혈가능일 " +
                    "FROM BLOODBANK.헌혈자 WHERE 회원_ID = ?";

            // PreparedStatement 생성
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, memberId);

            // 쿼리 실행
            resultSet = preparedStatement.executeQuery();

            // 결과 처리
            if (resultSet.next()) {
                donorInfo.put("회원_ID", resultSet.getString("회원_ID"));
                donorInfo.put("이름", resultSet.getString("이름"));
                donorInfo.put("생년월일", resultSet.getDate("생년월일"));
                donorInfo.put("성별", resultSet.getString("성별"));
                donorInfo.put("혈액형", resultSet.getString("혈액형"));
                donorInfo.put("휴대폰번호", resultSet.getString("휴대폰번호"));
                donorInfo.put("주소", resultSet.getString("주소"));
                donorInfo.put("헌혈횟수", resultSet.getInt("헌혈횟수"));
                donorInfo.put("마지막헌혈일", resultSet.getDate("마지막헌혈일"));
                donorInfo.put("헌혈가능일", resultSet.getDate("헌혈가능일"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return donorInfo;
    }
}
