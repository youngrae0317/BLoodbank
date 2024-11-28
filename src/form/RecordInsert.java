package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RecordInsert extends JFrame {
    public RecordInsert() {
        setTitle("헌혈 기록 삽입");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 중앙 패널: 입력 필드
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 2, 10, 10)); // 8행 2열 그리드, 간격 10px
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 외부 여백

        // 입력 필드 추가
        formPanel.add(new JLabel("회원 ID:"));
        JTextField memberIdField = new JTextField();
        formPanel.add(memberIdField);

        formPanel.add(new JLabel("담당 직원 ID:"));
        JTextField staffIdField = new JTextField();
        formPanel.add(staffIdField);

        formPanel.add(new JLabel("헌혈 일자:"));

        // 헌혈 날짜 입력 필드
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        HintTextField yearField = new HintTextField("년"); // 연도 4자리
        HintTextField monthField = new HintTextField("월"); // 월 2자리
        HintTextField dayField = new HintTextField("일"); // 일 2자리

        // 숫자 입력만 허용하는 KeyListener 추가
        KeyAdapter numericOnlyKeyListener = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                JTextField source = (JTextField) e.getSource();
                if (!Character.isDigit(c) || source.getText().length() >= getMaxLength(source)) {
                    e.consume(); // 숫자가 아니거나 최대 길이를 초과하면 입력 차단
                }
            }
        };

        // 필드에 KeyListener 적용
        yearField.addKeyListener(numericOnlyKeyListener);
        monthField.addKeyListener(numericOnlyKeyListener);
        dayField.addKeyListener(numericOnlyKeyListener);

        // 각 필드의 최대 길이 설정
        yearField.setColumns(4);
        monthField.setColumns(2);
        dayField.setColumns(2);

        // 날짜 필드를 패널에 추가
        datePanel.add(yearField);
        datePanel.add(new JLabel(" - "));
        datePanel.add(monthField);
        datePanel.add(new JLabel(" - "));
        datePanel.add(dayField);
        formPanel.add(datePanel);

        formPanel.add(new JLabel("헌혈 종류:"));
        JComboBox<String> donationTypeComboBox = new JComboBox<>(new String[]{"전혈", "혈장", "혈소판"});
        formPanel.add(donationTypeComboBox);

        formPanel.add(new JLabel("헌혈량(ml):"));
        JTextField donationAmountField = new JTextField();
        formPanel.add(donationAmountField);

        formPanel.add(new JLabel("보관 유효기간(일):"));
        JTextField storageValidityField = new JTextField();
        formPanel.add(storageValidityField);

        formPanel.add(new JLabel("증정품 종류:"));
        JTextField giftTypeField = new JTextField();
        formPanel.add(giftTypeField);

        add(formPanel, BorderLayout.CENTER);

        // 하단 패널: 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // 버튼 색상 및 폰트 설정
        Color buttonColor = new Color(100, 149, 237); // 파란색
        Font buttonFont = new Font("맑은 고딕", Font.BOLD, 14);

        JButton saveButton = new JButton("저장");
        saveButton.setBackground(buttonColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(buttonFont);
        saveButton.addActionListener(e -> {
            String memberId = memberIdField.getText();
            String staffId = staffIdField.getText();
            String year = yearField.getText();
            String month = monthField.getText();
            String day = dayField.getText();
            String donationType = (String) donationTypeComboBox.getSelectedItem();
            String donationAmount = donationAmountField.getText();
            String storageValidity = storageValidityField.getText();
            String giftType = giftTypeField.getText();

            // 날짜 유효성 확인
            if (year.length() != 4 || month.length() != 2 || day.length() != 2) {
                JOptionPane.showMessageDialog(
                        RecordInsert.this,
                        "헌혈 일자는 반드시 'YYYY-MM-DD' 형식으로 입력해주세요.",
                        "입력 오류",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    RecordInsert.this,
                    "데이터가 저장되었습니다:\n" +
                            "회원 ID: " + memberId + "\n" +
                            "담당 직원 ID: " + staffId + "\n" +
                            "헌혈 일자: " + year + "-" + month + "-" + day + "\n" +
                            "헌혈 종류: " + donationType + "\n" +
                            "헌혈량: " + donationAmount + "ml\n" +
                            "보관 유효기간: " + storageValidity + "일\n" +
                            "증정품 종류: " + giftType
            );
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("취소");
        cancelButton.setBackground(buttonColor);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(buttonFont);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 필드별 최대 길이를 반환하는 메서드
    private int getMaxLength(JTextField field) {
        if (field.getColumns() == 4) return 4; // 연도 필드
        if (field.getColumns() == 2) return 2; // 월, 일 필드
        return Integer.MAX_VALUE; // 기본값
    }

    // 힌트 텍스트 필드 클래스
    private static class HintTextField extends JTextField {
        private final String hint;

        public HintTextField(String hint) {
            this.hint = hint;
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(hint)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(hint);
                        setForeground(Color.GRAY);
                    }
                }
            });
            setText(hint);
            setForeground(Color.GRAY);
        }
    }

}
