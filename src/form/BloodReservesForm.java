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
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.ui.TextAnchor;

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
        JCheckBox chkPlatelet = new JCheckBox("혈소", true);
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

        List<String> bloodTypesToInclude = new ArrayList<>();
        if (includeWhole) bloodTypesToInclude.add("전혈");
        if (includePlasma) bloodTypesToInclude.add("성분(혈장)");
        if (includePlatelet) bloodTypesToInclude.add("성분(혈소판)");

        if (bloodTypesToInclude.isEmpty()) {
            JOptionPane.showMessageDialog(this, "최소 하나의 헌혈종류를 선택해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (String type : bloodTypesToInclude) {
            System.out.println("Fetching data for: " + type); // 디버깅 출력
            List<Object[]> data = reservesDB.getBloodReserves(type);

            for (Object[] row : data) {
                String bloodType = (String) row[0];
                String donationType = (String) row[1];
                int totalAmount = (int) row[2];

                // 데이터 확인 출력
                System.out.println("BloodType: " + bloodType + ", DonationType: " + donationType + ", TotalAmount: " + totalAmount);

                // 문자열 표준화
                if (donationType.equals("혈소판") || donationType.equals("성분(혈소)")) {
                    donationType = "성분(혈소판)";
                }

                double totalAmountInLiters = totalAmount / 1000.0;
                dataset.addValue(totalAmountInLiters, donationType, bloodType);
            }
        }

        // 디버깅: 데이터셋 값 출력
/*        System.out.println("Dataset values:");
          for (Object rowKey : dataset.getRowKeys()) {
            for (Object columnKey : dataset.getColumnKeys()) {
                Number value = dataset.getValue((Comparable<?>) rowKey, (Comparable<?>) columnKey);
                System.out.println(rowKey + " - " + columnKey + ": " + (value != null ? value : "null") + " L");
            }
        }*/
    }


    // 데이터셋에서 모든 값을 비교하여 전체 최댓값을 계산하는 메서드
    private double getMaxSumPerBloodType() {
        double maxSum = 0;

        // 데이터셋의 각 열(column, 즉 혈액형)에 대해 합계를 계산
        for (Object columnKey : dataset.getColumnKeys()) {
            double bloodTypeSum = 0;

            // 해당 혈액형에 대해 모든 헌혈 종류의 값을 합산
            for (Object rowKey : dataset.getRowKeys()) {
                Number value = dataset.getValue((Comparable<?>) rowKey, (Comparable<?>) columnKey);
                if (value != null) {
                    bloodTypeSum += value.doubleValue();
                }
            }

            // 합계 중 최대값을 갱신
            maxSum = Math.max(maxSum, bloodTypeSum);
        }

        return maxSum;
    }


    // 차트 생성 메서드
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
        plot.getDomainAxis().setLabelFont(koreanFont);
        plot.getDomainAxis().setTickLabelFont(koreanFont);
        plot.getRangeAxis().setLabelFont(koreanFont);
        plot.getRangeAxis().setTickLabelFont(koreanFont);

        // Y축 범위 설정
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        double globalMaxDatasetValue = getMaxSumPerBloodType(); // 데이터셋에서 전체 최댓값 계산
        double adjustedMax = Math.ceil(globalMaxDatasetValue * 1.2); // 최댓값에 20% 추가
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setRange(0, adjustedMax);

        // 스택형 막대 그래프 렌더러 설정
        StackedBarRenderer renderer = new StackedBarRenderer();

        // 헌혈 종류별 색상 고정
        setSeriesColor(renderer, "전혈", Color.RED);
        setSeriesColor(renderer, "성분(혈장)", Color.GREEN);
        setSeriesColor(renderer, "성분(혈소판)", Color.YELLOW);

        // 최소 막대 크기 설정
        renderer.setMinimumBarLength(0.1);

        // 항목 레이블 폰트 설정
        renderer.setBaseItemLabelFont(new Font("맑은 고딕", Font.PLAIN, 10));

        // 각 시리즈의 레이블을 명시적으로 활성화하고 위치를 설정
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesItemLabelsVisible(i, true);
            renderer.setSeriesPositiveItemLabelPosition(i, new ItemLabelPosition(
                    ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        }

        // 데이터 항목 레이블 생성기 설정
        renderer.setBaseItemLabelGenerator(new CategoryItemLabelGenerator() {
            @Override
            public String generateLabel(CategoryDataset dataset, int row, int column) {
                Number value = dataset.getValue(row, column);
                String valueText = (value != null) ? String.format("%.1f L", value.doubleValue()) : "0 L";
                return valueText;
            }

            @Override
            public String generateRowLabel(CategoryDataset dataset, int row) {
                return dataset.getRowKey(row).toString();
            }

            @Override
            public String generateColumnLabel(CategoryDataset dataset, int column) {
                return dataset.getColumnKey(column).toString();
            }
        });

        renderer.setBaseItemLabelsVisible(true);
        plot.setRenderer(renderer);

        // 범례 폰트 설정
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(koreanFont);
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
