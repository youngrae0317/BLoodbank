package form;

import database.DetailedInfoDatabase;
import database.TestResultDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import java.util.ArrayList;
import java.util.List;

public class DetailedInfoForm extends JFrame {
    private int selectedRecordId = -1; // 선호된 허핵기록번호를 저장할 변수

    public DetailedInfoForm(String memberId) {
        setTitle("상세 정보");
        setSize(1010, 750);
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
        memberInfoPanel.setBounds(10, 70, 980, 250);
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

        // DB에서 회원 정보 검색
        populateMemberInfo(memberId, memberIdField, nameField, birthField, phoneField, genderField, bloodTypeField, addressField, donationCountField, lastDonationDateField, nextDonationDateField);

        // 검색 결과 버튼 패널
        JPanel resultButtonPanel = new JPanel();
        resultButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        resultButtonPanel.setBounds(10, 330, 980, 40);
        JButton insertResultButton = new JButton("검사 결과 삽입");
        JButton viewResultButton = new JButton("검사 결과 조회");
        insertResultButton.setPreferredSize(new Dimension(150, 30));
        viewResultButton.setPreferredSize(new Dimension(150, 30));
        resultButtonPanel.add(insertResultButton);
        resultButtonPanel.add(viewResultButton);

        add(resultButtonPanel);

        // 헌혈 기록 테이블
        String[] columnNames = {"기록번호", "ID", "담당직원", "헌혈종류", "헌혈량", "헌혈일자", "헌혈릴레이", "보관유효기간", "검사상태"};
        Object[][] data = DetailedInfoDatabase.getDonationRecords(memberId);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 테이블 셀 수정 비활성화
            }
        };
        JTable table = new JTable(tableModel);

        // 테이블에 RowSorter 설정 (동적 정렬 지원)
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Comparator 설정
        sorter.setComparator(0, Comparator.comparingInt(o -> Integer.parseInt(o.toString()))); // 기록번호
        sorter.setComparator(4, Comparator.comparingInt(o -> Integer.parseInt(o.toString().replace("ml", "").trim()))); // 허핵량
        sorter.setComparator(5, Comparator.comparing(o -> LocalDate.parse(o.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))); // 허핵일자
        sorter.setComparator(7, Comparator.comparingInt(o -> Integer.parseInt(o.toString().replace("일", "").trim()))); // 보관유효기간

        // 최초 정렬 키 설정 (기록번호 열을 내림차순으로 정렬)
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING)); // 0번 열(기록번호)을 내림차순으로 정렬
        sorter.setSortKeys(sortKeys);
        sorter.sort(); // 정렬 적용

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(10, 380, 980, 300); // 테이블을 버튼 위에서 와로 내려뜨린 위치
        table.setFillsViewportHeight(true);

        add(tableScrollPane);

        // 테이블 선택 이벤트 리스너 추가
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    selectedRecordId = (int) tableModel.getValueAt(modelRow, 0); // 0번째 열이 헌혈기록번호
                }
            }
        });

        // 버튼 동작 설정
        insertResultButton.addActionListener(e -> {
            if (selectedRecordId != -1) {
                if (TestResultDAO.isTestResultExists(selectedRecordId)) {
                    JOptionPane.showMessageDialog(this, "해당 헌혈기록에 대한 검사 결과가 이미 존재합니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    new TestResultInsertForm(selectedRecordId); // 삽입 폼 생성
                }
            } else {
                JOptionPane.showMessageDialog(this, "헌혈 기록을 선택해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        viewResultButton.addActionListener(e -> {
            if (selectedRecordId != -1) {
                new TestResultViewForm(selectedRecordId); // 선택된 헌혈기록번호로 조회 폼 생성
            } else {
                JOptionPane.showMessageDialog(this, "헌혈 기록을 선택해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 버튼 스타일 지정
        Color buttonColor = new Color(100, 149, 237);
        JButton[] buttons = {insertResultButton, viewResultButton};
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        }

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
