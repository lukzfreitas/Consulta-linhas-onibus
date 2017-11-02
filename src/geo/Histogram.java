package geo;


import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.Map;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lucas
 */


  public class Histogram extends JFrame {
  
  private static final long serialVersionUID = 1L;


  public Histogram() throws IOException {
    JFreeChart chart = createChart();
       
    ChartPanel cpanel = new ChartPanel(chart);
    getContentPane().add(cpanel, BorderLayout.CENTER);
  }
  
  private JFreeChart createChart() throws IOException {
     EstruturaDeDados estrutura = new EstruturaDeDados();
    Map<Integer, ListDoubleLinked<Parada>> lista = estrutura.getListaDeParadasPorLinha();
    double[] v1 = new double[estrutura.listaLinhas.size()];
    int bin = 100;
    int i = 0;
    HistogramDataset dataset = new HistogramDataset();
    for(Integer key: lista.keySet()){        
        v1[i] = lista.get(key).size();
        i++;
    }
    
    
    
    dataset.addSeries("exemplo", v1, bin, 1, 100);    
    
    
    JFreeChart chart = ChartFactory.createHistogram(
              "Histogram de paradas por linha", 
              null, 
              null, 
              dataset, 
              PlotOrientation.VERTICAL, 
              true, 
              false, 
              false
          );

    chart.setBackgroundPaint(new Color(230,230,230));
    XYPlot xyplot = (XYPlot)chart.getPlot();
    xyplot.setForegroundAlpha(0.7F);
    xyplot.setBackgroundPaint(Color.WHITE);
    xyplot.setDomainGridlinePaint(new Color(150,150,150));
    xyplot.setRangeGridlinePaint(new Color(150,150,150));
    XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
    xybarrenderer.setShadowVisible(false);
    xybarrenderer.setBarPainter(new StandardXYBarPainter()); 
    return chart;
  }
  
}


