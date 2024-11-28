/*
package form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Calendar;

public class BloodRelayForm extends JFrame {
    public BloodRelayForm() {
        setTitle("헌혈릴레이 목록");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널: 제목
        JLabel titleLabel = new JLabel("헌혈릴레이 목록", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 필터 패널: 시작일, 종료일 필터 및 생성 버튼
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.add(new JLabel("시작일"));
        JComboBox<Integer> startYearComboBox = createYearComboBox();
        JComboBox<Integer> startMonthComboBox = createMonthComboBox();
        JComboBox<Integer> startDayComboBox = createDayComboBox();
        filterPanel.add(startYearComboBox);
        filterPanel.add(startMonthComboBox);
        filterPanel.add(startDayComboBox);

        filterPanel.add(new JLabel("종료일"));
        JComboBox<Integer> endYearComboBox = createYearComboBox();
        JComboBox<Integer> endMonthComboBox = createMonthComboBox();
        JComboBox<Integer> endDayComboBox = createDayComboBox();
        filterPanel.add(endYearComboBox);
        filterPanel.add(endMonthComboBox);
        filterPanel.add(endDayComboBox);

        JButton createButton = new JButton("생성");
        filterPanel.add(createButton);

        // 헌혈 릴레이 테이블 생성
        String[] columnNames = { "헌혈릴레이_ID", "릴레이회차", "시작일", "종료일" };
        Object[][] data = {
                { "1", "1회차", "2024-01-01", "2024-01-10" },
                { "2", "2회차", "2024-02-01", "2024-02-10" },
                { "3", "3회차", "2024-03-01", "2024-03-10" }
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // 분할 패널로 필터와 테이블을 구분
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filterPanel, tableScrollPane);
        splitPane.setDividerLocation(50); // 필터 패널 높이 조정
        splitPane.setResizeWeight(0.1); // 필터 패널 비율 설정

        add(splitPane, BorderLayout.CENTER);

        // 콤보박스 크기 조정
        adjustComboBoxSize(startYearComboBox);
        adjustComboBoxSize(startMonthComboBox);
        adjustComboBoxSize(startDayComboBox);
        adjustComboBoxSize(endYearComboBox);
        adjustComboBoxSize(endMonthComboBox);
        adjustComboBoxSize(endDayComboBox);

        setVisible(true);
    }

    private JComboBox<Integer> createYearComboBox() {
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 10; i <= currentYear + 10; i++) {
            yearComboBox.addItem(i);
        }
        return yearComboBox;
    }

    private JComboBox<Integer> createMonthComboBox() {
        JComboBox<Integer> monthComboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(i);
        }
        return monthComboBox;
    }

    private JComboBox<Integer> createDayComboBox() {
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(i);
        }
        return dayComboBox;
    }

    private void adjustComboBoxSize(JComboBox<Integer> comboBox) {
        comboBox.setPreferredSize(new Dimension(70, 25)); // 길이 70, 높이 25 설정
    }


}
*/
