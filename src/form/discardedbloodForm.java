package form;

import database.discardedbloodDatabase;
import database.discardedbloodDatabase.DiscardedBlood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class discardedbloodForm extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField; // 검색 입력 필드

    public discardedbloodForm() {
        setTitle("폐기 혈액");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메인 패널
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("헌혈기록번호 검색:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("검색");

        // 버튼 스타일 적용
        styleButton(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDiscardedBlood(); // 검색 버튼 클릭 시 검색 실행
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // 테이블 생성
        String[] columnNames = {"폐기_ID", "헌혈기록번호", "폐기일", "폐기사유"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀 비활성화
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 데이터 로드
        loadDiscardedBloodData();

        setVisible(true);
    }

    // 데이터 로드 메서드
    private void loadDiscardedBloodData() {
        discardedbloodDatabase db = new discardedbloodDatabase();
        List<DiscardedBlood> discardedBloodList = db.getAllDiscardedBlood();

        tableModel.setRowCount(0); // 기존 데이터 초기화
        for (DiscardedBlood blood : discardedBloodList) {
            tableModel.addRow(new Object[]{
                    blood.폐기_ID,
                    blood.헌혈기록번호,
                    blood.폐기일,
                    blood.폐기사유
            });
        }
    }

    // 검색 실행 메서드
    private void searchDiscardedBlood() {
        String searchKey = searchField.getText().trim(); // 검색 키워드 가져오기

        if (searchKey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "헌혈기록번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        discardedbloodDatabase db = new discardedbloodDatabase();
        List<DiscardedBlood> discardedBloodList = db.getAllDiscardedBlood(); // 모든 데이터 가져오기

        tableModel.setRowCount(0); // 기존 테이블 데이터 초기화
        boolean found = false;
        for (DiscardedBlood blood : discardedBloodList) {
            if (String.valueOf(blood.헌혈기록번호).equals(searchKey)) { // 헌혈기록번호로 필터링
                tableModel.addRow(new Object[]{
                        blood.폐기_ID,
                        blood.헌혈기록번호,
                        blood.폐기일,
                        blood.폐기사유
                });
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "검색 결과가 없습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
            loadDiscardedBloodData(); // 검색 결과 없을 시 모든 데이터 다시 로드
        }
    }

    // 버튼 스타일 적용 메서드
    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 149, 237)); // 파란색
        button.setForeground(Color.WHITE); // 흰색 텍스트
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
    }

   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(discardedbloodForm::new);
    }*/
}
