package form;

import database.ReservationDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ReservationForm extends JFrame {
    private DefaultTableModel tableModel;

    public ReservationForm() {
        setTitle("예약 조회");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널: 제목
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("예약 조회", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // 중앙 패널: 검색 필터 및 테이블
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 필터 패널
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        // 날짜 선택 필터
        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        String[] years = { "2015", "2016", "2017", "2023", "2024", "2025" };
        String[] months = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
        String[] days = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
        String[] hours = {"전체시간", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00" };

        JComboBox<String> yearComboBox = new JComboBox<>(years);
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        JComboBox<String> dayComboBox = new JComboBox<>(days);
        JComboBox<String> hourComboBox = new JComboBox<>(hours);
        JButton dateSearchButton = new JButton("조회");

        dateFilterPanel.add(new JLabel("년"));
        dateFilterPanel.add(yearComboBox);
        dateFilterPanel.add(new JLabel("월"));
        dateFilterPanel.add(monthComboBox);
        dateFilterPanel.add(new JLabel("일"));
        dateFilterPanel.add(dayComboBox);
        dateFilterPanel.add(new JLabel("시간"));
        dateFilterPanel.add(hourComboBox);
        dateFilterPanel.add(dateSearchButton);

        // 직원 ID 검색 필터
        JPanel staffFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JLabel staffIdLabel = new JLabel("직원 ID");
        JTextField staffIdField = new JTextField(20);
        JButton staffSearchButton = new JButton("조회");

        staffFilterPanel.add(staffIdLabel);
        staffFilterPanel.add(staffIdField);
        staffFilterPanel.add(staffSearchButton);

        // 필터 패널에 두 개의 행 추가
        filterPanel.add(dateFilterPanel);
        filterPanel.add(staffFilterPanel);

        centerPanel.add(filterPanel, BorderLayout.NORTH);

        // 테이블 데이터
        String[] columnNames = { "예약번호", "회원ID", "예약일시", "예약이름", "예약상태", "담당직원ID" };
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("등록");
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 이벤트 핸들러
        dateSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String year = (String) yearComboBox.getSelectedItem();
                String month = (String) monthComboBox.getSelectedItem();
                String day = (String) dayComboBox.getSelectedItem();
                String hour = (String) hourComboBox.getSelectedItem();

                fetchReservationData(year, month, day, hour, null);
            }
        });

        staffSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String staffId = staffIdField.getText().trim();
                if (staffId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "직원 ID를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                } else {
                    fetchReservationData(null, null, null, null, staffId);
                }
            }
        });

        setVisible(true);
    }

    private void fetchReservationData(String year, String month, String day, String hour, String staffId) {
        String startDateTime = null, endDateTime = null;
        if (year != null && month != null && day != null && hour != null) {
            if ("전체시간".equals(hour)) {
                startDateTime = String.format("%04d-%02d-%02d 00:00:00",
                        Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                endDateTime = String.format("%04d-%02d-%02d 23:59:59",
                        Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            } else {
                startDateTime = String.format("%04d-%02d-%02d %s:00",
                        Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), hour.substring(0, 2));
                endDateTime = String.format("%04d-%02d-%02d %s:59",
                        Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), hour.substring(0, 2));
            }
        }

        Vector<Vector<Object>> data = ReservationDatabase.getFilteredReservations(startDateTime, endDateTime, staffId);
        tableModel.setRowCount(0); // 기존 데이터 삭제

        for (Vector<Object> row : data) {
            tableModel.addRow(row);
        }
    }

}

