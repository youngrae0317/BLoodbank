package form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import database.*;

public class BloodRecordsForm extends JFrame {
    private RecordInsert recordInsertForm; // RecordInsert 폼 참조를 저장할 변수
    private DefaultTableModel tableModel;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    public BloodRecordsForm() {
        setTitle("헌혈기록");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널: 제목
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("헌혈기록", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // 중앙 패널: ID 입력 및 테이블
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ID 입력 패널
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new BorderLayout());
        JLabel idLabel = new JLabel("회원 ID", SwingConstants.LEFT);
        JTextField idField = new JTextField();
        JButton detailButton = new JButton("상세 정보 조회");
        idLabel.setPreferredSize(new Dimension(70, 30));
        idField.setPreferredSize(new Dimension(400, 30));
        detailButton.setPreferredSize(new Dimension(150, 30));
        idPanel.add(idLabel, BorderLayout.WEST);
        idPanel.add(idField, BorderLayout.CENTER);
        idPanel.add(detailButton, BorderLayout.EAST);
        centerPanel.add(idPanel, BorderLayout.NORTH);

        // 테이블 데이터
        String[] columnNames = {"기록번호", "ID", "담당직원", "헌혈종류", "헌혈량", "헌혈일자"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 셀 수정 불가
            }
        };
        table = new JTable(tableModel);

        // 데이터 로드 (기본 정렬: 기록번호 오름차순)
        RecordeDB.loadBloodRecords(tableModel, "헌혈기록번호", "ASC");

        // 테이블에 RowSorter 설정 (동적 정렬 지원)
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // 숫자형 및 날짜형 열의 Comparator 설정
        sorter.setComparator(0, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        sorter.setComparator(4, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // "ml" 제거 후 숫자 비교
                Integer val1 = Integer.parseInt(o1.replace("ml", ""));
                Integer val2 = Integer.parseInt(o2.replace("ml", ""));
                return val1.compareTo(val2);
            }
        });
        sorter.setComparator(5, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2); // 날짜 문자열 비교 (yyyy-MM-dd 형식이므로 가능)
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널: 삽입 버튼
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton insertButton = new JButton("삽입");
        buttonPanel.add(insertButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 스타일 지정
        Color buttonColor = new Color(100, 149, 237);
        JButton[] buttons = {insertButton, detailButton};
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        }

        // 테이블 클릭 이벤트: 선택된 ID 값을 ID 입력 필드에 표시
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow); // 정렬된 뷰 인덱스를 모델 인덱스로 변환
                        String selectedMemberInfo = tableModel.getValueAt(modelRow, 1).toString(); // 회원정보: 이름(ID)
                        String extractedId = selectedMemberInfo.substring(selectedMemberInfo.indexOf("(") + 1, selectedMemberInfo.indexOf(")")); // ID만 추출
                        idField.setText(extractedId); // ID 필드에 설정
                    }
                }
            }
        });

        // 상세 정보 조회 버튼 클릭 이벤트
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = idField.getText().trim(); // 회원 ID 입력값 가져오기
                if (!idText.isEmpty()) { // ID가 비어있지 않다면
                    System.out.println("회원 ID 전달: " + idText); // 디버깅 출력
                    DetailedInfoForm detailedInfoForm = new DetailedInfoForm(idText); // DetailedInfoForm 호출
                    detailedInfoForm.setVisible(true); // 창 표시
                } else {
                    JOptionPane.showMessageDialog(BloodRecordsForm.this, "회원 ID를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 삽입 버튼 클릭 이벤트: 폼 여러 번 실행 방지
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (recordInsertForm == null || !recordInsertForm.isVisible()) {
                    recordInsertForm = new RecordInsert(); // 새 삽입 폼 생성
                } else {
                    recordInsertForm.toFront();
                }
            }
        });

        setVisible(true);
    }
}
