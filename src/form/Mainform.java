package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mainform {
    // 각 폼의 인스턴스를 관리할 static 변수
    private static BloodReservesForm bloodReservesFormInstance;
    private static BloodRecordsForm bloodRecordsFormInstance;
    private static ReservationForm reservationFormInstance;
    private static discardedbloodForm discardedbloodFormInstance;

    public Mainform() {
        // 메인 프레임 생성
        JFrame frame = new JFrame("헌혈의집 관리 프로그램");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400); // 적당한 크기로 고정
        frame.setResizable(false);

        // 레이아웃 설정
        frame.setLayout(new BorderLayout());

        // 상단 제목 패널
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("헌혈의집 관리 프로그램", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        frame.add(titlePanel, BorderLayout.NORTH);

        // 중앙 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 버튼 가로 정렬 및 간격 설정
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // 상단 여백 30px 추가

        // 버튼 생성
        JButton[] buttons = new JButton[4]; // 버튼 개수를 4로 설정
        String[] buttonLabels = { "혈액보유량", "헌혈기록", "예약 조회", "폐기 혈액" };

        Dimension buttonSize = new Dimension(150, 120); // 버튼 크기 설정
        Color buttonColor = new Color(100, 149, 237); // 파란색

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setBackground(buttonColor);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("맑은 고딕", Font.BOLD, 14));
            buttons[i].setPreferredSize(buttonSize); // 버튼 크기 고정
            buttonPanel.add(buttons[i]);
        }

        // 각 버튼에 ActionListener 추가
        buttons[0].addActionListener(new ActionListener() { // 혈액보유량 버튼
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bloodReservesFormInstance == null || !bloodReservesFormInstance.isVisible()) {
                    bloodReservesFormInstance = new BloodReservesForm();
                } else {
                    bloodReservesFormInstance.toFront(); // 이미 열려 있으면 앞으로 가져오기
                }
            }
        });

        buttons[1].addActionListener(new ActionListener() { // 헌혈기록 버튼
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bloodRecordsFormInstance == null || !bloodRecordsFormInstance.isVisible()) {
                    bloodRecordsFormInstance = new BloodRecordsForm();
                } else {
                    bloodRecordsFormInstance.toFront(); // 이미 열려 있으면 앞으로 가져오기
                }
            }
        });

        buttons[2].addActionListener(new ActionListener() { // 예약 조회 버튼
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reservationFormInstance == null || !reservationFormInstance.isVisible()) {
                    reservationFormInstance = new ReservationForm();
                } else {
                    reservationFormInstance.toFront(); // 이미 열려 있으면 앞으로 가져오기
                }
            }
        });

        buttons[3].addActionListener(new ActionListener() { // 폐기 혈액 버튼
            @Override
            public void actionPerformed(ActionEvent e) {
                if (discardedbloodFormInstance == null || !discardedbloodFormInstance.isVisible()) {
                    discardedbloodFormInstance = new discardedbloodForm();
                } else {
                    discardedbloodFormInstance.toFront(); // 이미 열려 있으면 앞으로 가져오기
                }
            }
        });

        frame.add(buttonPanel, BorderLayout.CENTER);

        // 프레임 표시
        frame.setVisible(true);
    }
}
