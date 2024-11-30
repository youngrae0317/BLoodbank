package database;

import model.TestResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestResultDAO {
    public static TestResult getTestResultByRecordId(int recordId) {
        String query = "SELECT " +
                "    헌혈기록.헌혈기록번호, " +
                "    헌혈기록.헌혈일자, " +
                "    헌혈기록.회원_ID, " +
                "    혈액검사결과.B형간염, " +
                "    혈액검사결과.C형간염, " +
                "    혈액검사결과.매독, " +
                "    혈액검사결과.비예기항체, " +
                "    혈액검사결과.혈액형아형 " +
                "FROM BLOODBANK.헌혈기록 " +
                "JOIN BLOODBANK.혈액검사결과 ON 헌혈기록.헌혈기록번호 = 혈액검사결과.헌혈기록번호 " +
                "WHERE 헌혈기록.헌혈기록번호 = ?";

        try (Connection conn = DBConnect.connect()) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, recordId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        TestResult testResult = new TestResult();
                        testResult.setRecordId(rs.getInt("헌혈기록번호"));
                        testResult.setDonationDate(rs.getDate("헌혈일자"));
                        testResult.setMemberId(rs.getString("회원_ID"));
                        testResult.setHepatitisB(rs.getString("B형간염"));
                        testResult.setHepatitisC(rs.getString("C형간염"));
                        testResult.setSyphilis(rs.getString("매독"));
                        testResult.setUnexpectedAntibody(rs.getString("비예기항체"));
                        testResult.setBloodSubtype(rs.getString("혈액형아형"));

                        conn.commit(); // 트랜잭션 커밋
                        return testResult;
                    } else {
                        conn.rollback(); // 데이터가 없으면 롤백
                        return null;
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); // 오류 발생 시 롤백
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean insertTestResult(int recordId, String hepatitisB, String hepatitisC, String syphilis, String unexpectedAntibody, String bloodSubtype) {
        String query = "INSERT INTO BLOODBANK.혈액검사결과 (검사결과ID, 헌혈기록번호, B형간염, C형간염, 매독, 비예기항체, 혈액형아형) " +
                "VALUES (BLOODBANK.혈액검사결과_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.connect()) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, recordId);
                pstmt.setString(2, hepatitisB);
                pstmt.setString(3, hepatitisC);
                pstmt.setString(4, syphilis);
                pstmt.setString(5, unexpectedAntibody);
                pstmt.setString(6, bloodSubtype);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit(); // 트랜잭션 커밋
                    return true;
                } else {
                    conn.rollback(); // 영향받은 행이 없으면 롤백
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback(); // 오류 발생 시 롤백
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
