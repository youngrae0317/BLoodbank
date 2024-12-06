package database;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class RecordeDB {

    public static Object[][] fetchBloodRecords(String sortColumn, String sortOrder) {
        // 유효한 컬럼인지 확인
        String[] validColumns = {"헌혈기록번호", "회원정보", "직원정보", "헌혈종류", "헌혈량", "헌혈일자"};
        boolean isValidColumn = false;
        for (String col : validColumns) {
            if (col.equals(sortColumn)) {
                isValidColumn = true;
                break;
            }
        }
        if (!isValidColumn) {
            sortColumn = "헌혈기록번호"; // 기본 정렬 컬럼
        }

        // 정렬 방식이 ASC 또는 DESC인지 확인
        if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
            sortOrder = "ASC"; // 기본 정렬 방식
        }

        // JOIN을 사용하여 헌혈자와 직원의 이름과 아이디를 조합
        String query = "SELECT " +
                "    헌혈기록번호, " +
                "    헌혈자.이름 || '(' || 헌혈자.회원_ID || ')' AS 회원정보, " +
                "    직원.이름 || '(' || 직원.직원_ID || ')' AS 직원정보, " +
                "    헌혈종류, 헌혈량, 헌혈일자 " +
                "FROM BLOODBANK.헌혈기록 " +
                "JOIN BLOODBANK.헌혈자 ON 헌혈기록.회원_ID = 헌혈자.회원_ID " +
                "JOIN BLOODBANK.직원 ON 헌혈기록.담당직원_ID = 직원.직원_ID " +
                "ORDER BY " + sortColumn + " " + sortOrder;

        try (Connection connection = DBConnect.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // 데이터를 동적으로 저장할 리스트
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();

            // 결과를 순차적으로 읽어 리스트에 추가
            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = resultSet.getInt("헌혈기록번호");
                row[1] = resultSet.getString("회원정보"); // 회원정보: 이름(아이디)
                row[2] = resultSet.getString("직원정보"); // 직원정보: 이름(아이디)
                row[3] = resultSet.getString("헌혈종류");
                row[4] = resultSet.getInt("헌혈량");      // Integer 타입으로 저장
                row[5] = resultSet.getDate("헌혈일자");   // Date 타입으로 저장
                dataList.add(row);
            }

            // 리스트를 배열로 변환
            return dataList.toArray(new Object[0][0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0]; // 오류 발생 시 빈 배열 반환
        }
    }

    public static void loadBloodRecords(DefaultTableModel tableModel, String sortColumn, String sortOrder) {
        Object[][] data = fetchBloodRecords(sortColumn, sortOrder);
        tableModel.setRowCount(0); // 기존 데이터 삭제
        for (Object[] row : data) {
            // 헌혈량에 "ml" 추가, 헌혈일자는 문자열로 변환
            Object[] displayRow = new Object[] {
                    row[0], // 헌혈기록번호
                    row[1], // 회원정보
                    row[2], // 직원정보
                    row[3], // 헌혈종류
                    row[4] + "ml", // 헌혈량에 "ml" 추가
                    row[5].toString() // 헌혈일자
            };
            tableModel.addRow(displayRow);
        }
    }

    // 회원 ID 존재 여부 확인 메서드
    public static boolean checkMemberExists(String memberId) {
        String query = "SELECT COUNT(*) AS count FROM BLOODBANK.헌혈자 WHERE 회원_ID = ?";
        try (Connection connection = DBConnect.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, memberId); // ID를 쿼리에 바인딩
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count"); // 존재 여부 확인
                    return count > 0; // 0보다 크면 해당 ID가 존재
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 오류 발생 시 또는 데이터가 없으면 false 반환
    }
}
