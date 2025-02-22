package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailedInfoDatabase {

    // 데이터베이스 연결을 DBConnect에서 가져옴
    private static Connection getConnection() {
        return DBConnect.connect();  // DBConnect 클래스의 연결 메서드 사용
    }

    /**
     * 회원 ID로 헌혈자 정보를 조회하는 메서드
     *
     * @param memberId 회원 ID
     * @return 헌혈자 정보 (HashMap<String, Object>)
     */
    public static HashMap<String, Object> getDonorInfo(String memberId) {
        HashMap<String, Object> donorInfo = new HashMap<>();
        String query = "SELECT 회원_ID, 이름, 생년월일, 성별, 혈액형, 휴대폰번호, 주소, 헌혈횟수, 마지막헌혈일, 헌혈가능일 " +
                "FROM BLOODBANK.헌혈자 WHERE 회원_ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, memberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donorInfo;
    }

    /**
     * 특정 회원 ID와 관련된 헌혈 기록을 조회하는 메서드
     *
     * @param memberId 회원 ID
     * @return 헌혈 기록 리스트
     */
    public static Object[][] getDonationRecords(String memberId) {
        ArrayList<Object[]> records = new ArrayList<>();
        String query = "SELECT 헌혈기록번호, 회원_ID, 담당직원_ID, 헌혈종류, 헌혈량, " +
                "TO_CHAR(헌혈일자, 'YYYY-MM-DD') AS 헌혈일자, " +
                "TO_CHAR(보관유효기간, 'YYYY-MM-DD') AS 보관유효기간, " +
                "CASE WHEN 헌혈릴레이_ID IS NOT NULL THEN '참여' ELSE '미참여' END AS 헌혈릴레이, " +
                "상태 " +
                "FROM BLOODBANK.헌혈기록 WHERE 회원_ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, memberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    records.add(new Object[]{
                            resultSet.getInt("헌혈기록번호"),
                            resultSet.getString("회원_ID"),
                            resultSet.getString("담당직원_ID"),
                            resultSet.getString("헌혈종류"),
                            resultSet.getString("헌혈량") + "ml",
                            resultSet.getString("헌혈일자"),
                            resultSet.getString("헌혈릴레이"),
                            resultSet.getString("보관유효기간"),
                            resultSet.getString("상태")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 결과를 2차원 배열로 변환
        return records.toArray(new Object[0][0]);
    }
}
