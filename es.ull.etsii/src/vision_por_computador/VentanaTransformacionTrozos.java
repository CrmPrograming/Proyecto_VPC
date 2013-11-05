package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
      this.nTramos = Integer.parseInt(this.tfNTramos.getText());
      new VentanaTramos();
      this.dispose();
    }
  }
  
  private class VentanaTramos extends JFrame implements ActionListener {

    private JLabel lPunto;
    private JLabel lValor;
    private JButton bAceptar;
    private JButton bCancelar;
    private JTextField[] vPuntos;
    private JTextField[] vValores;
    
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
      this.setVisible(true);
      this.pack();
      this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      
      
    }
    
    private class PanelGrafica extends JPanel {
      
      public void paintComponent(Graphics g) {
        
      }
      
    }
    
  }
}
