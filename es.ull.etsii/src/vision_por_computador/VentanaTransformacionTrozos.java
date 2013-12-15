package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class VentanaTransformacionTrozos extends JFrame implements ActionListener {
  
  protected HashMap<String, String> idioma;
  protected PanelPrincipal pPrincipal;
  protected int nTramos;
  protected int[] tVout = new int[256];
  private JTextField tfNTramos;
  private JButton bContinuar;
  private JButton bCancelar;
  private JLabel lNTramos;
  private final int ANCHO_A = 350;
  private final int ALTO_A = 200;
  
  public VentanaTransformacionTrozos(HashMap<String, String> idioma, PanelPrincipal panelPrincipal) {
    super("Transformación Lineal por Tramos");
    this.idioma = idioma;
    this.pPrincipal = panelPrincipal;
    this.tfNTramos = new JTextField();
    this.bContinuar = new JButton("Continuar");
    this.bCancelar = new JButton("Cancelar");
    this.bContinuar.addActionListener(this);
    this.bCancelar.addActionListener(this);
    this.bContinuar.setActionCommand("bContinuar");
    this.bCancelar.setActionCommand("bCancelar");
    this.lNTramos = new JLabel("Número de tramos");
    JPanel panelVentana = new JPanel(new BorderLayout());
    JPanel panelBotones = new JPanel(new GridLayout(2, 1, 30, 30));
    panelBotones.add(this.bContinuar);
    panelBotones.add(this.bCancelar);
    panelVentana.add(this.lNTramos, BorderLayout.CENTER);
    panelVentana.add(this.tfNTramos, BorderLayout.SOUTH);
    panelVentana.add(panelBotones, BorderLayout.EAST);
    this.setContentPane(panelVentana);
    this.setSize(ANCHO_A, ALTO_A);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if ("bCancelar".equals(arg0.getActionCommand())) {
      this.dispose();
    }
    if ("bContinuar".equals(arg0.getActionCommand())) {
      try {
        this.nTramos = Integer.parseInt(this.tfNTramos.getText());
        JFrame.setDefaultLookAndFeelDecorated(false);
        new VentanaTramos();
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.dispose();
      } catch (NumberFormatException e) {
        this.pPrincipal.mostrarError(24);
      }
    }
  }
  
  private class VentanaTramos extends JFrame implements ActionListener {

    private JLabel lPunto;
    private JLabel lValor;
    private JButton bAceptar;
    private JButton bCancelar;
    protected JTextField[] vPuntos;
    protected JTextField[] vValores;
    protected final int ANCHO_G = 265;
    protected final int ALTO_G = 285;
    private JFrame fGrafica;
    
    public VentanaTramos() {
      super("Tramos de la Transformación");      
      this.lPunto = new JLabel("Puntos");
      this.lValor = new JLabel("Valores");
      this.bAceptar = new JButton("Aceptar");
      this.bCancelar = new JButton("Cancelar");
      this.bAceptar.addActionListener(this);
      this.bCancelar.addActionListener(this);
      this.bAceptar.setActionCommand("aceptar");
      this.bCancelar.setActionCommand("cancelar");
      JPanel panelVentana = new JPanel(new GridLayout(nTramos + 3, 2, 25, 25));
      this.vPuntos = new JTextField[nTramos + 1];
      this.vValores = new JTextField[nTramos + 1];
      panelVentana.add(this.lPunto);
      panelVentana.add(this.lValor);
      for (int i = 0; i <= nTramos; i++) {
        this.vPuntos[i] = new JTextField(3);
        this.vValores[i] = new JTextField(3);        
        panelVentana.add(this.vPuntos[i]);        
        panelVentana.add(this.vValores[i]);
      }     
      panelVentana.add(this.bAceptar);
      panelVentana.add(this.bCancelar);
      panelVentana.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      this.setContentPane(panelVentana);
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      this.setLocationRelativeTo(null);
      this.pack();
      this.setVisible(true);      
      this.setResizable(false);
      JFrame.setDefaultLookAndFeelDecorated(false);
      this.fGrafica = new JFrame("Gráfica");
      PanelGrafica pGrafica = new PanelGrafica(fGrafica);
      this.fGrafica.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      this.fGrafica.setSize(ANCHO_G, ALTO_G);
      this.fGrafica.setLocationRelativeTo(this);
      this.fGrafica.setContentPane(pGrafica);
      this.fGrafica.setResizable(false);
      this.fGrafica.setVisible(true);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      if ("cancelar".equals(arg0.getActionCommand())) {
        this.fGrafica.dispose();
        this.dispose();        
      }
      
      if ("aceptar".equals(arg0.getActionCommand())) {
        try {
          Double.parseDouble(this.vPuntos[0].getText());
          calcularNuevaImagen();
          this.fGrafica.dispose();
          this.dispose(); 
        } catch (NumberFormatException e) {
          pPrincipal.mostrarError(24);
        }         
      }
      
    }
    
    private void calcularNuevaImagen() {
      ArrayList<Point> listaPuntos = new ArrayList<Point>();
      int tramoActual = 0;
      final int MAX_PIXEL = 256;
      int[] Vout = new int[MAX_PIXEL];
      for (int i = 0; i <= nTramos; i++) {
        int q = Integer.parseInt(this.vPuntos[i].getText()); 
        int a = Integer.parseInt(this.vValores[i].getText());
        listaPuntos.add(new Point(q, a));
      }
      Point pInicial = listaPuntos.get(tramoActual);
      Point pFinal = listaPuntos.get(tramoActual + 1);
      for (int a = 0; a < MAX_PIXEL; a++) {
        double m = (pFinal.getY() - pInicial.getY()) / (pFinal.getX() - pInicial.getX());
        double n = pInicial.getY() - (pInicial.getX() * m);
        Vout[a] = (int) ((m * a) + n);
        if ((a == pFinal.getX() - 1) && (a != MAX_PIXEL - 2)) {
          tramoActual++;
          pInicial = listaPuntos.get(tramoActual);
          pFinal = listaPuntos.get(tramoActual + 1);
        }
      }      
      double angulo = pPrincipal.getImgFoco().getAnguloGirado();
      pPrincipal.duplicarImagen();
      pPrincipal.getImgFoco().ajustarPixels(Vout);
      pPrincipal.getImgFoco().setAnguloGirado(angulo);
    }
        
    private class PanelGrafica extends JPanel {
      
      private Timer temporizador;
      private final int DELAY = 100;
      private JFrame frame;
      
      public PanelGrafica(JFrame jF) {
        this.frame = jF;
        this.temporizador = new Timer(0, new TimerListener());
        this.temporizador.setDelay(DELAY);
        this.temporizador.start();    
      }
      
      @Override
      public void paintComponent(Graphics g) {
        g.drawRect(0, 0, ANCHO_G, ALTO_G);
        if (comprobar()) {
          Color aux = g.getColor();
          g.setColor(Color.RED);        
          for (int i = 0; i < nTramos; i++) {
            dibujarRecta(g, i);
          }
          g.setColor(aux);
        }        
      }
      
      private boolean comprobar() {
        boolean result = true;
        try {
          for (int i = 0; i <= nTramos; i++) {
            Integer.parseInt(vPuntos[i].getText());
            Integer.parseInt(vValores[i].getText());
          }
        } catch (NumberFormatException e) {
          result = false;
        }        
        return (result);        
      }
      
      private void dibujarRecta(Graphics g, int i) {
        int x1 = Integer.parseInt(vPuntos[i].getText()); 
        int x2 = Integer.parseInt(vPuntos[i + 1].getText());
        int y1 = Integer.parseInt(vValores[i].getText());
        int y2 = Integer.parseInt(vValores[i + 1].getText());
        g.drawLine(x1, 255 - y1, x2, 255 - y2);
      }
      
      private class TimerListener implements ActionListener {

        /**
         * Sobreescritura del m&eacute;todo "actionPerformed" 
         * para redibujar la imagen y dem&aacute;s elementos
         * en el panel cuando se dispara el Timer
         *
         * @param arg0 Evento disparado
         */
        @Override
        public void actionPerformed(ActionEvent arg0) {
          frame.repaint();
        }
        
      }
      
    }    
    
  }
}
