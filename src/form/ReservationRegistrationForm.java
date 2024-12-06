package form;

import database.ReservationInsertDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException; // SQLException 임포트 추가
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReservationRegistrationForm extends JFrame {
    private JTextField memberIdField;
    private JTextField reservationNameField;
    private JTextField staffIdField;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> hourComboBox;
    private JComboBox<String> minuteComboBox;
    private JButton registerButton;

    public ReservationRegistrationForm() {
        setTitle("예약 등록");
        setSize(600, 400); // 폼 크기 확장
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 회원 ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("회원 ID:"), gbc);

        memberIdField = new JTextField();
        memberIdField.setPreferredSize(new Dimension(200, 25)); // 텍스트박스 크기 조정
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(memberIdField, gbc);

        // 예약 이름
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("예약 이름:"), gbc);

        reservationNameField = new JTextField();
        reservationNameField.setPreferredSize(new Dimension(200, 25)); // 텍스트박스 크기 조정
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(reservationNameField, gbc);

        // 담당 직원 ID -> 담당 직원으로 수정
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("담당 직원:"), gbc); // 수정된 레이블

        staffIdField = new JTextField();
        staffIdField.setPreferredSize(new Dimension(200, 25)); // 텍스트박스 크기 조정
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(staffIdField, gbc);

        // 예약 일시
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(new JLabel("예약 일시:"), gbc);

        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        String[] years = { "2024", "2025", "2026" };
        yearComboBox = new JComboBox<>(years);

        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) months[i - 1] = String.valueOf(i);
        monthComboBox = new JComboBox<>(months);

        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = String.valueOf(i);
        dayComboBox = new JComboBox<>(days);

        String[] hours = { "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18" };
        hourComboBox = new JComboBox<>(hours);

        String[] minutes = { "00", "10", "20", "30", "40", "50" };
        minuteComboBox = new JComboBox<>(minutes); // 예약 시간은 10분 단위로 제한

        dateTimePanel.add(new JLabel("년"));
        dateTimePanel.add(yearComboBox);
        dateTimePanel.add(new JLabel("월"));
        dateTimePanel.add(monthComboBox);
        dateTimePanel.add(new JLabel("일"));
        dateTimePanel.add(dayComboBox);
        dateTimePanel.add(new JLabel("시간"));
        dateTimePanel.add(hourComboBox);
        dateTimePanel.add(new JLabel("시"));
        dateTimePanel.add(new JLabel("분"));
        dateTimePanel.add(minuteComboBox);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // 날짜/시간 필드를 더 넓게 확장
        add(dateTimePanel, gbc);

        // 등록 버튼
        registerButton = new JButton("등록");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(registerButton, gbc);

        // 버튼 스타일 적용
        styleButton(registerButton);

        // 버튼 이벤트 핸들러
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText().trim();
                String reservationName = reservationNameField.getText().trim();
                String staffId = staffIdField.getText().trim();

                String year = (String) yearComboBox.getSelectedItem();
                String month = (String) monthComboBox.getSelectedItem();
                String day = (String) dayComboBox.getSelectedItem();
                String hour = (String) hourComboBox.getSelectedItem();
                String minute = (String) minuteComboBox.getSelectedItem();

                // 유효성 검사
                if (memberId.isEmpty() || reservationName.isEmpty() || staffId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "모든 필드를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String dateTimeStr = String.format("%04d-%02d-%02d %02d:%s:00",
                        Integer.parseInt(year),
                        Integer.parseInt(month),
                        Integer.parseInt(day),
                        Integer.parseInt(hour),
                        minute
                );

                // 날짜 형식 검증
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setLenient(false);
                try {
                    sdf.parse(dateTimeStr);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "유효한 날짜와 시간을 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Timestamp reservationTimestamp = Timestamp.valueOf(dateTimeStr);

                // 데이터베이스에 예약 삽입
                try {
                    ReservationInsertDatabase.insertReservation(memberId, reservationTimestamp, reservationName, staffId);
                    JOptionPane.showMessageDialog(null, "예약이 성공적으로 등록되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // 폼 닫기
                } catch (SQLException ex) {
                    // 오류 처리
                    String errorMessage = ex.getMessage();
                    if (ex.getErrorCode() == 20001) { // 해당 회원...
                        JOptionPane.showMessageDialog(null, "해당 회원은 이미 오늘 예약이 존재합니다.", "예약 오류", JOptionPane.ERROR_MESSAGE);
                    } else if (ex.getErrorCode() == 20002) { // 예약 초과
                        JOptionPane.showMessageDialog(null, "해당 시간대의 예약 인원이 이미 6명을 초과했습니다.", "예약 오류", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "예약 등록에 실패했습니다.\n오류: " + errorMessage, "오류", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "JDBC 드라이버를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    // 버튼 스타일 적용 메서드
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 179, 113)); // 초록색
        button.setForeground(Color.WHITE); // 흰색 텍스트
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
    }
}

