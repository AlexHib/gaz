import java.awt.*;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Plot {

    JFrame jfrm;
    XYPlot plot;

    public void makePlot(int[] masX, double[] masY){
        jfrm = new JFrame();
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setSize(400, 300);
        jfrm.setVisible(true);

        XYSeries s1 = new XYSeries("Plot");
        for(int i = 0; i < 1100; i++){
            if(masY[i]!=0) s1.add(masX[i], masY[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);

        JFreeChart chart = ChartFactory.createXYLineChart("Plot", "T",
                "E", dataset, PlotOrientation.VERTICAL, false, false, false);

        chart.setBackgroundPaint(Color.white);

        plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis xAxis = (NumberAxis)plot.getDomainAxis();
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setAutoRangeIncludesZero(false);
        final NumberAxis yAxis = new NumberAxis();
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        final ChartPanel chartPanel = new ChartPanel(chart);
        jfrm.add(chartPanel);

    }


}
