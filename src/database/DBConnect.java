package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    // 데이터베이스 연결 정보
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // 데이터베이스 URL
    private static final String DB_USER = "BLOODBANK"; // 데이터베이스 사용자명
    private static final String DB_PASSWORD = "1234"; // 데이터베이스 비밀번호

    // 데이터베이스 연결 메서드
    public static Connection connect() {
        Connection connection = null;
        try {
            // Oracle JDBC 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 데이터베이스 연결
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("데이터베이스에 성공적으로 연결되었습니다.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("데이터베이스 연결에 실패했습니다.");
            e.printStackTrace();
        }
        return connection;
    }

    // 데이터베이스 연결 끊기 메서드
    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("데이터베이스 연결이 종료되었습니다.");
            } catch (SQLException e) {
                System.err.println("데이터베이스 연결 종료 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        }
    }
}
