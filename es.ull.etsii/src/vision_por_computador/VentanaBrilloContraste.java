package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class VentanaBrilloContraste extends JFrame implements ActionListener {
  
  /**
   * Constante con el ancho de esta ventana
   */
  private final int ANCHO = 300;
  /**
   * Constante con el alto de esta ventana
   */
  private final int ALTO = 100;
  private HashMap<String, String> idioma;
  private PanelPrincipal panelPrincipal;
  private JButton bAceptar;
  private JButton bCancelar;
  private JTextField tfBrillo;
  private JTextField tfContraste;
  private JLabel lBrillo;
  private JLabel lContraste;
  private int[] tVout = new int[256];

  public VentanaBrilloContraste(HashMap<String, String> idioma, PanelPrincipal pPrincipal) {
    super("Ajuste de Brillo y Contraste");  
    final int COLUMNAS = 3;
    this.idioma = idioma;
    this.panelPrincipal = pPrincipal;
    this.tfBrillo = new JTextField(String.valueOf(panelPrincipal.getImgFoco().getBrillo()), COLUMNAS);
    this.tfBrillo.setActionCommand("brillo");
    this.tfContraste = new JTextField(String.valueOf(panelPrincipal.getImgFoco().getContraste()), COLUMNAS);
    this.bAceptar = new JButton(idioma.get("mm_aceptar"));
    this.bAceptar.setActionCommand("aceptar");
    this.bAceptar.addActionListener(this);
    this.bCancelar = new JButton(idioma.get("mm_cancelar"));
    this.bCancelar.setActionCommand("cancelar");
    this.bCancelar.addActionListener(this);
    this.lBrillo = new JLabel("Brillo:");
    this.lContraste = new JLabel("Contraste:");
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);    
    JPanel panelVentana = new JPanel(new BorderLayout());
    JPanel panelAuxiliar = new JPanel(new GridLayout(3, 2));
    panelAuxiliar.add(this.lBrillo);
    panelAuxiliar.add(this.tfBrillo);
    panelAuxiliar.add(this.lContraste);
    panelAuxiliar.add(this.tfContraste);
    panelAuxiliar.add(this.bAceptar);
    panelAuxiliar.add(this.bCancelar);
    panelVentana.add(panelAuxiliar, BorderLayout.CENTER);
    this.setContentPane(panelVentana);
    this.setSize(ANCHO, ALTO);
    this.setVisible(true);
    this.setResizable(false);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if ("aceptar".equals(arg0.getActionCommand())) {
      construirTabla();
      crearNuevaImagen();
    }
      
    if ("cancelar".equals(arg0.getActionCommand())) {
      this.dispose();
    }
    
  }
  
  private void construirTabla() {    
    Double nContraste = Double.parseDouble(this.tfContraste.getText());
    nContraste /= panelPrincipal.getImgFoco().getContraste();
    Double nBrillo = Double.parseDouble(this.tfBrillo.getText());
    nBrillo = nBrillo - nContraste * panelPrincipal.getImgFoco().getBrillo();
    for (int i = 0; i < 256; i++) {
      this.tVout[i] = (int) ((nContraste * i) + nBrillo);
      if (this.tVout[i] > 255)
        this.tVout[i] = 255;
      if (this.tVout[i] < 0)
        this.tVout[i] = 0;
    }
  }
  
  private void crearNuevaImagen() {
    this.panelPrincipal.duplicarImagen();
    this.panelPrincipal.getImgFoco().ajustarPixels(this.tVout);
    this.panelPrincipal.getImgFoco().fijarGris(true);
  }

}
