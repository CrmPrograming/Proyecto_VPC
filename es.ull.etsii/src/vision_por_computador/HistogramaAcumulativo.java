package vision_por_computador;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

@SuppressWarnings("serial")
public class HistogramaAcumulativo extends JFrame {
  
  private int[] nGris;
  
  /**
   * Instancia un nuevo objeto
   * de tipo histograma acumulativo.
   *
   * @param title the title
   * @param nivelGris the nivel gris
   */
  public HistogramaAcumulativo(String title, int[] nivelGris) {
    super(title);
    this.nGris = nivelGris;
    final CategoryDataset dataset = createDataset();
    final JFreeChart chart = createChart(dataset);
    final ChartPanel chartPanel = new ChartPanel(chart);
    this.add(chartPanel, BorderLayout.CENTER);
  }
  
  /**
   * Creates the dataset.
   *
   * @return category dataset
   */
  private CategoryDataset createDataset() {
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    int acumulador = 0;
    for (int i = 0; i < this.nGris.length; i++) {
      dataset.addValue(this.nGris[i] + acumulador, "Nivel de Gris", String.valueOf(i));
      acumulador = this.nGris[i] + acumulador;      
    }       
    return dataset;   
  }
  
  /**
   * Creates the chart.
   *
   * @param dataset the dataset
   * @return j free chart
   */
  private JFreeChart createChart(final CategoryDataset dataset) {    
    final JFreeChart chart = ChartFactory.createBarChart(
         "Histograma Acumulativo",                 // chart title
         "Nivel de Gris",              // domain(x-axis) axis label
         "Cantidad de Pixels",         // range(y-axis) axis label
         dataset,                      // data
         PlotOrientation.VERTICAL,     // orientation
         false,                        // include legend
         true,                        // tooltips
         false                         // urls
     );
  
    chart.setBackgroundPaint(Color.white);
  
  //set plot specifications
    final CategoryPlot plot = (CategoryPlot) chart.getPlot();
    plot.setBackgroundPaint(new Color(0xffffe0));
    plot.setDomainGridlinesVisible(true);
    plot.setDomainGridlinePaint(Color.lightGray);
    plot.setRangeGridlinePaint(Color.lightGray);
 
  //  CUSTOMIZE DOMAIN AXIS
    final CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
 
    //customize domain label position
    domainAxis.setCategoryLabelPositions(
      CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
      );
    domainAxis.setVisible(false);
    domainAxis.setLowerMargin(0);
    domainAxis.setUpperMargin(0);
    domainAxis.setCategoryMargin(0);
    //CUSTOMIZE RANGE AXIS
    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    rangeAxis.setAutoRangeIncludesZero(true);
 
    //CUSTOMIZE THE RENDERER
    final BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setDrawBarOutline(false);
    Stroke stroke = new BasicStroke(
      3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
    renderer.setBaseOutlineStroke(stroke);  
    return chart;
  }
}
