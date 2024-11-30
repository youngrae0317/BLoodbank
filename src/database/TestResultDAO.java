package database;

import model.TestResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

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
        String selectQuery = "SELECT 헌혈기록번호 FROM BLOODBANK.혈액검사결과 WHERE 헌혈기록번호 = ? FOR UPDATE";
        String insertQuery = "INSERT INTO BLOODBANK.혈액검사결과 (검사결과ID, 헌혈기록번호, B형간염, C형간염, 매독, 비예기항체, 혈액형아형) " +
                "VALUES (BLOODBANK.혈액검사결과_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.connect()) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 트랜잭션 격리 수준 설정
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, recordId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        // 이미 검사 결과가 존재함
                        conn.rollback();
                        JOptionPane.showMessageDialog(null, "해당 헌혈기록에 대한 검사 결과가 이미 존재합니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    } else {
                        // 검사 결과 삽입
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, recordId);
                            insertStmt.setString(2, hepatitisB);
                            insertStmt.setString(3, hepatitisC);
                            insertStmt.setString(4, syphilis);
                            insertStmt.setString(5, unexpectedAntibody);
                            insertStmt.setString(6, bloodSubtype);

                            int rowsAffected = insertStmt.executeUpdate();

                            if (rowsAffected > 0) {
                                conn.commit(); // 트랜잭션 커밋
                                return true;
                            } else {
                                conn.rollback(); // 영향받은 행이 없으면 롤백
                                return false;
                            }
                        }
                    }
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

    // 검사 결과 존재 여부 확인 메서드
    public static boolean isTestResultExists(int recordId) {
        String query = "SELECT COUNT(*) FROM BLOODBANK.혈액검사결과 WHERE 헌혈기록번호 = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, recordId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
