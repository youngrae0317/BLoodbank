package form;

import database.*;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class BloodReservesForm extends JFrame {
    private DefaultCategoryDataset dataset;
    private JFreeChart barChart;
    private ChartPanel chartPanel;

    public BloodReservesForm() {
        setTitle("혈액보유량");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널: 제목 및 체크박스
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("혈액 보유량", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel();
        JCheckBox chkWhole = new JCheckBox("전혈", true);
        JCheckBox chkPlasma = new JCheckBox("혈장", true);
        JCheckBox chkPlatelet = new JCheckBox("혈소", false);
        filterPanel.add(chkWhole);
        filterPanel.add(chkPlasma);
        filterPanel.add(chkPlatelet);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // 그래프 데이터셋 생성
        dataset = new DefaultCategoryDataset();
        updateDataset(chkWhole.isSelected(), chkPlasma.isSelected(), chkPlatelet.isSelected());

        // 차트 생성
        barChart = createChart();
        chartPanel = new ChartPanel(barChart);
        add(chartPanel, BorderLayout.CENTER);

        // 체크박스 리스너 추가
        ActionListener filterListener = e -> {
            updateDataset(chkWhole.isSelected(), chkPlasma.isSelected(), chkPlatelet.isSelected());
            barChart.fireChartChanged(); // 차트 갱신
        };

        chkWhole.addActionListener(filterListener);
        chkPlasma.addActionListener(filterListener);
        chkPlatelet.addActionListener(filterListener);

        setVisible(true);
    }

    // 데이터셋 업데이트 메서드
    private void updateDataset(boolean includeWhole, boolean includePlasma, boolean includePlatelet) {
        dataset.clear();

        // 선택된 헌혈 종류 리스트 생성
        List<String> bloodTypesToInclude = new ArrayList<>();
        if (includeWhole) bloodTypesToInclude.add("전혈");
        if (includePlasma) bloodTypesToInclude.add("성분(혈장)");
        if (includePlatelet) bloodTypesToInclude.add("성분(혈소판)");

        // 헌혈종류가 하나라도 선택되지 않았다면 경고 메시지 표시
        if (bloodTypesToInclude.isEmpty()) {
            JOptionPane.showMessageDialog(this, "최소 하나의 헌혈종류를 선택해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 데이터베이스에서 데이터 가져오기
        for (String type : bloodTypesToInclude) {
            List<Object[]> data = reservesDB.getBloodReserves(type);

            // 데이터셋에 반영
            for (Object[] row : data) {
                String bloodType = (String) row[0];
                String donationType = (String) row[1];
                int totalAmount = (int) row[2];

                // mL -> L 변환
                double totalAmountInLiters = totalAmount / 1000.0;

                // 데이터셋에 추가
                dataset.addValue(totalAmountInLiters, donationType, bloodType);
            }
        }

        // 디버깅: 데이터셋 상태 확인
/*        System.out.println("Dataset after update:");
        for (Object rowKey : dataset.getRowKeys()) {
            for (Object columnKey : dataset.getColumnKeys()) {
                Number value = dataset.getValue((Comparable<?>) rowKey, (Comparable<?>) columnKey);
                System.out.println(rowKey + " - " + columnKey + ": " + value + " L");
            }
        }*/
    }


    // 차트 생성 메서드
    private JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createStackedBarChart(
                "혈액 보유량", // 차트 제목
                "혈액형", // X축 레이블
                "보유량(L)", // Y축 레이블
                dataset
        );

        // 폰트 설정
        Font koreanFont = new Font("맑은 고딕", Font.PLAIN, 12);

        // 차트 제목 폰트 설정
        chart.setTitle(new TextTitle("혈액 보유량", new Font("맑은 고딕", Font.BOLD, 18)));

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // X축 및 Y축 폰트 설정
        plot.getDomainAxis().setLabelFont(koreanFont); // X축 레이블 폰트
        plot.getDomainAxis().setTickLabelFont(koreanFont); // X축 값 폰트
        plot.getRangeAxis().setLabelFont(koreanFont); // Y축 레이블 폰트
        plot.getRangeAxis().setTickLabelFont(koreanFont); // Y축 값 폰트

        // Y축 범위 설정
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 10); // Y축 범위를 설정 (조정 가능)

        // 스택형 막대 그래프 렌더러 설정
        StackedBarRenderer renderer = new StackedBarRenderer();

        // 헌혈 종류별 색상 고정
        setSeriesColor(renderer, "전혈", Color.RED); // 전혈은 빨간색
        setSeriesColor(renderer, "성분(혈장)", Color.GREEN); // 혈장은 초록색
        setSeriesColor(renderer, "성분(혈소판)", Color.BLUE); // 혈소판은 파란색

        // 데이터 항목 레이블(막대 위 텍스트) 폰트 설정
        renderer.setBaseItemLabelFont(koreanFont);
        renderer.setBaseItemLabelsVisible(true); // 데이터 레이블 표시

        // 데이터 항목 레이블 생성기 (익명 클래스 사용)
        renderer.setBaseItemLabelGenerator(new CategoryItemLabelGenerator() {
            @Override
            public String generateRowLabel(CategoryDataset dataset, int row) {
                return dataset.getRowKey(row).toString();
            }

            @Override
            public String generateColumnLabel(CategoryDataset dataset, int column) {
                return dataset.getColumnKey(column).toString();
            }

            @Override
            public String generateLabel(CategoryDataset dataset, int row, int column) {
                return dataset.getRowKey(row) + " (" + dataset.getValue(row, column) + ")";
            }
        });

        plot.setRenderer(renderer);

        // 범례(legend) 폰트 설정
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(koreanFont); // 범례 폰트 설정
        }

        return chart;
    }

    // 행 이름으로 색상을 설정하는 헬퍼 메서드
    private void setSeriesColor(StackedBarRenderer renderer, String rowKey, Color color) {
        int rowIndex = dataset.getRowIndex(rowKey);
        if (rowIndex >= 0) { // 데이터셋에 해당 행이 존재하는 경우에만 색상을 설정
            renderer.setSeriesPaint(rowIndex, color);
        }
    }




    public static void main(String[] args) {
        new BloodReservesForm();
    }
}
