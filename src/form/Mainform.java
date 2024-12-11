package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Mainform {
    // 각 폼의 인스턴스를 관리할 static 변수
    private static BloodReservesForm bloodReservesFormInstance;
    private static BloodRecordsForm bloodRecordsFormInstance;
    private static ReservationForm reservationFormInstance;
    private static discardedbloodForm discardedbloodFormInstance;

    public Mainform() {
        // FlatLaf 모던한 Look and Feel 설정
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 메인 프레임 생성
        JFrame frame = new JFrame("헌혈의집 관리 프로그램");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600); // 크기 조정
        frame.setResizable(true); // 창 크기 조정 가능

        // 레이아웃 설정
        frame.setLayout(new BorderLayout());

        // 상단 제목 패널
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(34, 150, 243)); // 메인 색상 설정
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // 여백 추가
        JLabel titleLabel = new JLabel("헌혈의집 관리 프로그램", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE); // 텍스트 색상 변경
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);

        // 중앙 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE); // 배경색 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // 버튼 간격 설정
        gbc.fill = GridBagConstraints.BOTH; // 버튼이 셀을 가득 채우도록 설정

        // 버튼 생성
        JButton[] buttons = new JButton[4]; // 버튼 개수를 4로 설정
        String[] buttonLabels = { "혈액보유량", "헌혈기록", "예약 조회", "폐기 혈액" };
        String[] iconPaths = {
                "/icons/blood_reserves.png",
                "/icons/blood_records.png",
                "/icons/reservation.png",
                "/icons/discarded_blood.png"
        };

        Dimension buttonSize = new Dimension(200, 200); // 버튼 크기 설정

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setFont(new Font("맑은 고딕", Font.BOLD, 18));
            buttons[i].setFocusPainted(false);
            buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            buttons[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBackground(new Color(34, 150, 243));
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            // 둥근 모서리 설정
            buttons[i].setUI(new RoundedButtonUI());

            // 아이콘 추가
            java.net.URL imgURL = getClass().getResource(iconPaths[i]);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                buttons[i].setIcon(new ImageIcon(img));
            } else {
                System.err.println("Couldn't find file: " + iconPaths[i]);
                buttons[i].setIcon(UIManager.getIcon("OptionPane.warningIcon"));
            }

            // 호버 효과 추가
            buttons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    JButton sourceButton = (JButton) evt.getSource();
                    sourceButton.setBackground(new Color(85, 172, 238));
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    JButton sourceButton = (JButton) evt.getSource();
                    sourceButton.setBackground(new Color(34, 150, 243));
                }
            });

            // GridBagLayout에 버튼 추가
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            buttonPanel.add(buttons[i], gbc);
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

        // 프레임 중앙에 위치
        frame.setLocationRelativeTo(null);
        // 프레임 표시
        frame.setVisible(true);
    }

    /*public static void main(String[] args) {
        // Event Dispatch Thread에서 GUI 생성
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Mainform();
            }
        });
    }*/
}

/**
 * 커스텀 버튼 UI 클래스로, 버튼의 모서리를 둥글게 만듭니다.
 */
class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
    private static final int ARC_WIDTH = 20;
    private static final int ARC_HEIGHT = 20;

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱 설정
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경 그리기
        if (button.getModel().isArmed()) {
            g2.setColor(button.getBackground().darker());
        } else {
            g2.setColor(button.getBackground());
        }
        g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), ARC_WIDTH, ARC_HEIGHT);

        // 텍스트 그리기
        super.paint(g2, c);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return c.getPreferredSize();
    }
}
