package form;

import javax.swing.*;
import java.awt.*;

public class TestResultForm extends JFrame {
    // TestResultForm의 단일 인스턴스를 유지하기 위한 정적 변수
    private static TestResultForm instance;

    // 정적 메서드: 단일 인스턴스 관리
    public static TestResultForm getInstance(String memberId, boolean isInsertMode) {
        if (instance == null || !instance.isDisplayable()) {
            instance = new TestResultForm(memberId, isInsertMode);
        } else {
            instance.toFront(); // 이미 열려 있으면 앞으로 가져오기
        }
        return instance;
    }

    // private 생성자: 외부에서 직접 호출할 수 없음
    private TestResultForm(String memberId, boolean isInsertMode) {
        setTitle(isInsertMode ? "검사 결과 삽입" : "검사 결과 조회");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 제목 패널
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // 파란색 배경
        JLabel titleLabel = new JLabel(isInsertMode ? "검사 결과 삽입" : "검사 결과 조회", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // 중앙 검사 결과 패널
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 검사 결과 필드 추가
        addTestResultField(formPanel, gbc, "회원_ID", memberId, 0, false);
        addTestResultComboBoxOrLabel(formPanel, gbc, "B형간염", 1, isInsertMode, "음성");
        addTestResultComboBoxOrLabel(formPanel, gbc, "C형간염", 2, isInsertMode, "음성");
        addTestResultComboBoxOrLabel(formPanel, gbc, "매독", 3, isInsertMode, "음성");
        addTestResultComboBoxOrLabel(formPanel, gbc, "비예기항체", 4, isInsertMode, "음성");
        addTestResultComboBoxOrLabel(formPanel, gbc, "혈액형아형", 5, isInsertMode, "음성");

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton(isInsertMode ? "저장" : "닫기");
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 스타일 지정
        saveButton.setBackground(new Color(100, 149, 237));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 저장 버튼 동작
        saveButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void addTestResultField(JPanel panel, GridBagConstraints gbc, String label, String value, int row, boolean isEditable) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        JTextField textField = new JTextField(value, 15);
        textField.setEditable(isEditable);
        panel.add(textField, gbc);
    }

    private void addTestResultComboBoxOrLabel(JPanel panel, GridBagConstraints gbc, String label, int row, boolean isInsertMode, String defaultValue) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        if (isInsertMode) {
            // 활성화 상태에서는 콤보박스
            JComboBox<String> comboBox = new JComboBox<>(new String[]{"양성", "음성"});
            comboBox.setSelectedItem(defaultValue); // 기본 값 설정
            panel.add(comboBox, gbc);
        } else {
            // 비활성화 상태에서는 레이블
            JLabel valueLabel = new JLabel(defaultValue);
            valueLabel.setOpaque(true);
            valueLabel.setBackground(Color.WHITE);
            valueLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
            panel.add(valueLabel, gbc);
        }
    }
}
