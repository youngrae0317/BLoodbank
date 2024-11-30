package form;

import database.TestResultDAO;

import javax.swing.*;
import java.awt.*;

public class TestResultInsertForm extends JFrame {
    public TestResultInsertForm(int recordId) {

        // 검사 결과 존재 여부 확인
        if (TestResultDAO.isTestResultExists(recordId)) {
            JOptionPane.showMessageDialog(null, "해당 헌혈기록에 대한 검사 결과가 이미 존재합니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // 폼 닫기
            return;
        }

        setTitle("검사 결과 삽입");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 제목 패널
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // 파란색 배경
        JLabel titleLabel = new JLabel("검사 결과 삽입", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // 중앙 검사 결과 입력 패널
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addLabelAndField(formPanel, gbc, "헌혈기록번호", String.valueOf(recordId), row++, false);
        addLabelAndComboBox(formPanel, gbc, "B형간염", row++);
        addLabelAndComboBox(formPanel, gbc, "C형간염", row++);
        addLabelAndComboBox(formPanel, gbc, "매독", row++);
        addLabelAndComboBox(formPanel, gbc, "비예기항체", row++);
        addLabelAndComboBox(formPanel, gbc, "혈액형아형", row++);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("저장");
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 스타일 지정
        saveButton.setBackground(new Color(100, 149, 237));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 저장 버튼 동작
        saveButton.addActionListener(e -> {
            // 입력된 값 가져오기
            String hepatitisB = ((JComboBox<String>) componentMap.get("B형간염")).getSelectedItem().toString();
            String hepatitisC = ((JComboBox<String>) componentMap.get("C형간염")).getSelectedItem().toString();
            String syphilis = ((JComboBox<String>) componentMap.get("매독")).getSelectedItem().toString();
            String unexpectedAntibody = ((JComboBox<String>) componentMap.get("비예기항체")).getSelectedItem().toString();
            String bloodSubtype = ((JComboBox<String>) componentMap.get("혈액형아형")).getSelectedItem().toString();

            // 데이터베이스에 삽입하는 로직
            boolean success = TestResultDAO.insertTestResult(recordId, hepatitisB, hepatitisC, syphilis, unexpectedAntibody, bloodSubtype);

            if (success) {
                JOptionPane.showMessageDialog(this, "검사 결과가 성공적으로 저장되었습니다.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "검사 결과 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // 컴포넌트를 저장하기 위한 맵
    private java.util.Map<String, JComponent> componentMap = new java.util.HashMap<>();

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, String value, int row, boolean isEditable) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        JTextField textField = new JTextField(value, 15);
        textField.setEditable(isEditable);
        panel.add(textField, gbc);

        componentMap.put(label, textField);
    }

    private void addLabelAndComboBox(JPanel panel, GridBagConstraints gbc, String label, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"음성", "양성"});
        panel.add(comboBox, gbc);

        componentMap.put(label, comboBox);
    }
}
