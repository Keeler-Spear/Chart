//The was primarily made by ChatGPT. I only made a few minor adjustments to the code.

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class JFreeGraph {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 620;

    //Creates a scatter plot in square window, where x and y have the same scale
    public static void scatterPlot(double[][] vals, String title, String data) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            XYSeries series = new XYSeries(data);
            double xMin = Double.POSITIVE_INFINITY, xMax = Double.NEGATIVE_INFINITY;
            double yMin = Double.POSITIVE_INFINITY, yMax = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < vals[0].length; i++) {
                double x = vals[0][i];
                double y = vals[1][i];
                series.add(x, y);

                xMin = Math.min(xMin, x);
                xMax = Math.max(xMax, x);
                yMin = Math.min(yMin, y);
                yMax = Math.max(yMax, y);
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createScatterPlot(
                    title,
                    "X",
                    "Y",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            XYPlot plot = chart.getXYPlot();
            double range = Math.max(xMax - xMin, yMax - yMin);
            double xCenter = (xMax + xMin) / 2;
            double yCenter = (yMax + yMin) / 2;

            plot.getDomainAxis().setRange(-1 + xCenter - range / 2, 1 + xCenter + range / 2);
            plot.getRangeAxis().setRange(-1 + yCenter - range / 2, 1 + yCenter + range / 2);

            // Ensure equal scale on both axes
            NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
            NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
            xAxis.setFixedAutoRange(range);
            yAxis.setFixedAutoRange(range);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

            frame.add(chartPanel);
            frame.pack();
            frame.setVisible(true);
        });
    }

}