package form;

import database.RecordInsertDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RecordInsert extends JFrame {
    public RecordInsert() {
        setTitle("헌혈 기록 삽입");
        setSize(600, 500);
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
        JComboBox<String> donationTypeComboBox = new JComboBox<>(new String[]{"전혈", "성분(혈장)", "성분(혈소판)"});
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

        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(e -> {
            String memberId = memberIdField.getText().trim();
            String staffId = staffIdField.getText().trim();
            String year = yearField.getText().trim();
            String month = monthField.getText().trim();
            String day = dayField.getText().trim();
            String donationType = (String) donationTypeComboBox.getSelectedItem();
            String donationAmount = donationAmountField.getText().trim();
            String storageValidity = storageValidityField.getText().trim();
            String giftType = giftTypeField.getText().trim();

            // 날짜 유효성 확인
            String donationDate;
            try {
                int yearValue = Integer.parseInt(year);
                int monthValue = Integer.parseInt(month);
                int dayValue = Integer.parseInt(day);

                if (monthValue < 1 || monthValue > 12 || dayValue < 1 || dayValue > 31) {
                    throw new NumberFormatException();
                }

                // 날짜를 "YYYY-MM-DD" 형식으로 변환
                donationDate = String.format("%04d-%02d-%02d", yearValue, monthValue, dayValue);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        RecordInsert.this,
                        "헌혈 일자는 반드시 'YYYY-MM-DD' 형식으로 입력해주세요.",
                        "입력 오류",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 나머지 입력값 유효성 확인
            if (memberId.isEmpty() || staffId.isEmpty() || donationAmount.isEmpty() || storageValidity.isEmpty() || giftType.isEmpty()) {
                JOptionPane.showMessageDialog(
                        RecordInsert.this,
                        "모든 필드를 입력해주세요.",
                        "입력 오류",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                int amount = Integer.parseInt(donationAmount);
                int validity = Integer.parseInt(storageValidity);

                // JDBC 호출
                RecordInsertDatabase dao = new RecordInsertDatabase();
                boolean success = dao.insertRecord(memberId, staffId, donationDate, donationType, amount, validity, giftType);

                if (success) {
                    JOptionPane.showMessageDialog(this, "헌혈 기록이 성공적으로 저장되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "저장 중 오류가 발생했습니다.", "저장 실패", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        RecordInsert.this,
                        "헌혈량과 보관 유효기간은 숫자로 입력해주세요.",
                        "입력 오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("취소");
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

    private static class HintTextField extends JTextField {
        private final String hint;

        public HintTextField(String hint) {
            this.hint = hint;
            setForeground(Color.GRAY); // 힌트 텍스트의 색상을 기본적으로 회색으로 설정
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(hint)) {
                        setText("");
                        setForeground(Color.BLACK); // 포커스를 받으면 텍스트를 지우고 색상을 검정으로 변경
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(hint);
                        setForeground(Color.GRAY); // 텍스트가 비면 힌트 텍스트를 회색으로 설정
                    }
                }
            });
            setText(hint); // 처음에 힌트 텍스트를 표시
        }
    }


   /* public static void main(String[] args) {
        new RecordInsert();
    }*/
}
