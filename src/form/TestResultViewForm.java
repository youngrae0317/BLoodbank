package form;

import model.TestResult;
import database.TestResultDAO;

import javax.swing.*;
import java.awt.*;

public class TestResultViewForm extends JFrame {
    public TestResultViewForm(int recordId) {
        setTitle("검사 결과 조회");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 데이터베이스에서 검사 결과 가져오기
        TestResult testResult = TestResultDAO.getTestResultByRecordId(recordId);

        if (testResult == null) {
            JOptionPane.showMessageDialog(this, "해당 헌혈기록번호에 대한 검사 결과가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // 상단 제목 패널
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // 파란색 배경
        JLabel titleLabel = new JLabel("검사 결과 조회", SwingConstants.CENTER);
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

        int row = 0;
        addLabelAndValue(formPanel, gbc, "헌혈기록번호", String.valueOf(testResult.getRecordId()), row++);
        addLabelAndValue(formPanel, gbc, "헌혈일자", testResult.getDonationDate().toString(), row++);
        addLabelAndValue(formPanel, gbc, "회원_ID", testResult.getMemberId(), row++);
        addLabelAndValue(formPanel, gbc, "B형간염", testResult.getHepatitisB(), row++);
        addLabelAndValue(formPanel, gbc, "C형간염", testResult.getHepatitisC(), row++);
        addLabelAndValue(formPanel, gbc, "매독", testResult.getSyphilis(), row++);
        addLabelAndValue(formPanel, gbc, "비예기항체", testResult.getUnexpectedAntibody(), row++);
        addLabelAndValue(formPanel, gbc, "혈액형아형", testResult.getBloodSubtype(), row++);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("닫기");
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 스타일 지정
        closeButton.setBackground(new Color(100, 149, 237));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 닫기 버튼 동작
        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void addLabelAndValue(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        JLabel valueLabel = new JLabel(value);
        valueLabel.setOpaque(true);
        valueLabel.setBackground(Color.WHITE);
        valueLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
        panel.add(valueLabel, gbc);
    }
}
