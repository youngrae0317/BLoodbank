/*
package form;

import database.DetailedInfoDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DetailedInfoForm extends JFrame {
    public DetailedInfoForm(String memberId) {
        setTitle("상세 정보");
        setSize(1010, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 상단 패널: 제목
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(0, 0, 1000, 60);
        JLabel titleLabel = new JLabel("상세정보", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel);

        // 회원 정보 패널
        JPanel memberInfoPanel = new JPanel(new GridBagLayout());
        memberInfoPanel.setBorder(BorderFactory.createTitledBorder("회원 정보"));
        memberInfoPanel.setBounds(10, 70, 980, 200);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 회원 정보 필드 추가
        JTextField memberIdField = addMemberInfoField(memberInfoPanel, gbc, "회원_ID", memberId, 0, 0);
        JTextField nameField = addMemberInfoField(memberInfoPanel, gbc, "이름", "", 0, 2);
        JTextField birthField = addMemberInfoField(memberInfoPanel, gbc, "생년월일", "", 1, 0);
        JTextField phoneField = addMemberInfoField(memberInfoPanel, gbc, "전화번호", "", 1, 2);
        JTextField genderField = addMemberInfoField(memberInfoPanel, gbc, "성별", "", 2, 0);
        JTextField bloodTypeField = addMemberInfoField(memberInfoPanel, gbc, "혈액형", "", 2, 2);
        JTextField addressField = addMemberInfoField(memberInfoPanel, gbc, "주소", "", 3, 0, 3);
        JTextField donationCountField = addMemberInfoField(memberInfoPanel, gbc, "헌혈횟수", "", 4, 0);
        JTextField lastDonationDateField = addMemberInfoField(memberInfoPanel, gbc, "마지막 헌혈일", "", 5, 0);
        JTextField nextDonationDateField = addMemberInfoField(memberInfoPanel, gbc, "헌혈가능일", "", 5, 2);

        add(memberInfoPanel);

        // DB에서 회원 정보 가져오기
        HashMap<String, Object> donorInfo = DetailedInfoDatabase.getDonorInfo(memberId);
        if (!donorInfo.isEmpty()) {
            memberIdField.setText((String) donorInfo.get("회원_ID"));
            nameField.setText((String) donorInfo.get("이름"));
            birthField.setText(donorInfo.get("생년월일").toString());
            phoneField.setText((String) donorInfo.get("휴대폰번호"));
            genderField.setText((String) donorInfo.get("성별"));
            bloodTypeField.setText((String) donorInfo.get("혈액형"));
            addressField.setText((String) donorInfo.get("주소"));
            donationCountField.setText(String.valueOf(donorInfo.get("헌혈횟수")));
            lastDonationDateField.setText(donorInfo.get("마지막헌혈일") != null ? donorInfo.get("마지막헌혈일").toString() : "");
            nextDonationDateField.setText(donorInfo.get("헌혈가능일") != null ? donorInfo.get("헌혈가능일").toString() : "");
        } else {
            JOptionPane.showMessageDialog(this, "회원 정보를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField addMemberInfoField(JPanel panel, GridBagConstraints gbc, String label, String value, int row, int col) {
        return addMemberInfoField(panel, gbc, label, value, row, col, 1);
    }

    private JTextField addMemberInfoField(JPanel panel, GridBagConstraints gbc, String label, String value, int row, int col, int gridWidth) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridx = col + 1;
        gbc.gridwidth = gridWidth;
        JTextField textField = new JTextField(value);
        textField.setColumns(20);
        textField.setEditable(false);
        panel.add(textField, gbc);
        return textField;
    }
}
*/
package form;

import database.DetailedInfoDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;

public class DetailedInfoForm extends JFrame {
    public DetailedInfoForm(String memberId) {
        setTitle("상세 정보");
        setSize(1010, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 상단 패널: 제목
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(0, 0, 1000, 60);
        JLabel titleLabel = new JLabel("상세정보", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel);

        // 회원 정보 패널
        JPanel memberInfoPanel = new JPanel(new GridBagLayout());
        memberInfoPanel.setBorder(BorderFactory.createTitledBorder("회원 정보"));
        memberInfoPanel.setBounds(10, 70, 980, 200);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 회원 정보 필드 추가
        JTextField memberIdField = addMemberInfoField(memberInfoPanel, gbc, "회원_ID", memberId, 0, 0);
        JTextField nameField = addMemberInfoField(memberInfoPanel, gbc, "이름", "", 0, 2);
        JTextField birthField = addMemberInfoField(memberInfoPanel, gbc, "생년월일", "", 1, 0);
        JTextField phoneField = addMemberInfoField(memberInfoPanel, gbc, "전화번호", "", 1, 2);
        JTextField genderField = addMemberInfoField(memberInfoPanel, gbc, "성별", "", 2, 0);
        JTextField bloodTypeField = addMemberInfoField(memberInfoPanel, gbc, "혈액형", "", 2, 2);
        JTextField addressField = addMemberInfoField(memberInfoPanel, gbc, "주소", "", 3, 0, 3);
        JTextField donationCountField = addMemberInfoField(memberInfoPanel, gbc, "헌혈횟수", "", 4, 0);
        JTextField lastDonationDateField = addMemberInfoField(memberInfoPanel, gbc, "마지막 헌혈일", "", 5, 0);
        JTextField nextDonationDateField = addMemberInfoField(memberInfoPanel, gbc, "헌혈가능일", "", 5, 2);

        add(memberInfoPanel);

        // 검사 결과 버튼 패널
        JPanel resultButtonPanel = new JPanel();
        resultButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        resultButtonPanel.setBounds(10, 280, 980, 40);
        JButton insertResultButton = new JButton("검사 결과 삽입");
        JButton viewResultButton = new JButton("검사 결과 조회");
        insertResultButton.setPreferredSize(new Dimension(150, 30));
        viewResultButton.setPreferredSize(new Dimension(150, 30));
        resultButtonPanel.add(insertResultButton);
        resultButtonPanel.add(viewResultButton);

        add(resultButtonPanel);

        // 헌혈 기록 테이블
        String[] columnNames = {"기록번호", "ID", "담당직원", "헌혈종류", "헌혈량", "헌혈일자", "헌혈릴레이", "보관유효기간"};
        Object[][] data = {
                {"1", memberId, "김철수", "전혈", "350ml", "2024-11-15", "참여", "2024-12-15"},
                {"2", memberId, "이영희", "혈장", "500ml", "2024-10-10", "미참여", "2024-11-10"},
                {"3", memberId, "박지민", "혈소판", "250ml", "2024-09-20", "참여", "2024-10-20"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(10, 330, 980, 300);
        table.setFillsViewportHeight(true);

        add(tableScrollPane);

        // 버튼 동작 설정
        insertResultButton.addActionListener(e -> {
            String id = memberIdField.getText();
            TestResultForm.getInstance(id, true); // TestResultForm을 삽입 모드로 호출
        });

        viewResultButton.addActionListener(e -> {
            String id = memberIdField.getText();
            TestResultForm.getInstance(id, false); // TestResultForm을 조회 모드로 호출
        });

        // 버튼 스타일 지정
        Color buttonColor = new Color(100, 149, 237);
        JButton[] buttons = {insertResultButton, viewResultButton};
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        }

        // DB에서 회원 정보 가져오기
        populateMemberInfo(memberId, memberIdField, nameField, birthField, phoneField, genderField, bloodTypeField, addressField, donationCountField, lastDonationDateField, nextDonationDateField);

        setVisible(true);
    }

    private JTextField addMemberInfoField(JPanel panel, GridBagConstraints gbc, String label, String value, int row, int col) {
        return addMemberInfoField(panel, gbc, label, value, row, col, 1);
    }

    private JTextField addMemberInfoField(JPanel panel, GridBagConstraints gbc, String label, String value, int row, int col, int gridWidth) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridx = col + 1;
        gbc.gridwidth = gridWidth;
        JTextField textField = new JTextField(value);
        textField.setColumns(20);
        textField.setEditable(false);
        panel.add(textField, gbc);
        return textField;
    }

    private void populateMemberInfo(String memberId, JTextField memberIdField, JTextField nameField, JTextField birthField,
                                    JTextField phoneField, JTextField genderField, JTextField bloodTypeField,
                                    JTextField addressField, JTextField donationCountField, JTextField lastDonationDateField,
                                    JTextField nextDonationDateField) {
        HashMap<String, Object> donorInfo = DetailedInfoDatabase.getDonorInfo(memberId);
        if (!donorInfo.isEmpty()) {
            memberIdField.setText((String) donorInfo.get("회원_ID"));
            nameField.setText((String) donorInfo.get("이름"));
            birthField.setText(donorInfo.get("생년월일").toString());
            phoneField.setText((String) donorInfo.get("휴대폰번호"));
            genderField.setText((String) donorInfo.get("성별"));
            bloodTypeField.setText((String) donorInfo.get("혈액형"));
            addressField.setText((String) donorInfo.get("주소"));
            donationCountField.setText(String.valueOf(donorInfo.get("헌혈횟수")));
            lastDonationDateField.setText(donorInfo.get("마지막헌혈일") != null ? donorInfo.get("마지막헌혈일").toString() : "");
            nextDonationDateField.setText(donorInfo.get("헌혈가능일") != null ? donorInfo.get("헌혈가능일").toString() : "");
        } else {
            JOptionPane.showMessageDialog(this, "회원 정보를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
