package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class VentanaOpciones extends JFrame implements ActionListener {
  
  /**
   * Constante con el ancho de esta ventana
   */
  private final int ANCHO = 300;
  /**
   * Constante con el alto de esta ventana
   */
  private final int ALTO = 500;
  /**
   * Constante con el n&uacute;mero de filas en el GridLayout
   */
  private final int N_FILAS = 0;
  /**
   * Constante con el n&uacute;mero de columnas en el GridLayout
   */
  private final int N_COLUMNAS = 2;  
  /**
   * Array con los idiomas disponibles en el programa
   */
  private final String[] LISTA_IDIOMAS = new String[] {"Español", "English"};
  
  /**
   * Cadena con el nombre del fichero donde se almacenan
   * las configuraciones del programa
   */
  private String fichero;
  /**
   * JPanel superior con las distintas opciones del programa
   */
  private JPanel panelOpciones;
  /**
   * JPanel con los botones de "Aceptar" o "Cancelar"
   */
  private JPanel panelBotones;
  /**
   * JButton para el bot&oacute;n "Aceptar"
   */
  private JButton botonAceptar;
  /**
   * JButton para el bot&oacute;n "Cancelar"
   */
  private JButton botonCancelar;
  /**
   * JComboBox asociado a la lista de idiomas
   */
  private JComboBox<String> boxIdiomas;
  /**
   * JLabel para la opci&oacute;n del idioma
   */
  private JLabel lIdioma;
  /**
   * JLabel para la opci&oacute;n de la consola
   */
  private JLabel lConsola;
  /**
   * JComboBox asociado a la consola de debug
   */
  private JComboBox<String> boxConsola;
  private HashMap<String, String> idioma;
  private PanelPrincipal pPrincipal;
  private JLabel lColor;
  private JButton bColor;

  /**
   * Instancia un nuevo objeto
   * de tipo VentanaOpciones.
   *
   * @param idioma Array con las cadenas en el idioma actual
   * @param FICHERO String con el nombre del fichero de opciones
   */
  public VentanaOpciones(HashMap<String, String> idioma, final String FICHERO, PanelPrincipal pIni) {
    super(idioma.get("c_opciones"));
    this.setLocationRelativeTo(null);
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    this.idioma = idioma;
    this.fichero = FICHERO;
    this.pPrincipal = pIni;
    this.panelBotones = new JPanel(new FlowLayout());
    this.panelOpciones = new JPanel(new GridLayout(N_FILAS, N_COLUMNAS));
    this.lColor = new JLabel("Color Interfaz");
    this.bColor = new JButton();
    this.bColor.setActionCommand("cambiarColor");
    this.bColor.addActionListener(this);
    this.bColor.setBackground(pIni.getElemColor());
    this.botonAceptar = new JButton(idioma.get("mm_aceptar"));
    this.botonAceptar.setActionCommand("aceptar");
    this.botonAceptar.addActionListener(this);
    this.botonCancelar = new JButton(idioma.get("mm_cancelar"));
    this.botonCancelar.setActionCommand("cancelar");
    this.botonCancelar.addActionListener(this);
    this.panelBotones.add(this.botonAceptar);
    this.panelBotones.add(this.botonCancelar);
    panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
    this.boxIdiomas = new JComboBox<String>(LISTA_IDIOMAS);
    this.boxIdiomas.setSelectedIndex(0);
    this.lIdioma = new JLabel("Idioma");
    this.boxConsola = new JComboBox<String>(new String[] {"Disabled", "Enabled"});
    this.boxConsola.setSelectedIndex(0);
    this.lConsola = new JLabel("Debug por defecto ");
    this.panelOpciones.add(this.lIdioma);
    this.panelOpciones.add(this.boxIdiomas);        
    this.panelOpciones.add(this.lConsola);
    this.panelOpciones.add(this.boxConsola);
    this.panelOpciones.add(this.lColor);
    this.panelOpciones.add(this.bColor);    
    JPanel aux = new JPanel(new FlowLayout());
    aux.add(this.panelOpciones);
    panelPrincipal.add(aux, BorderLayout.CENTER);
    this.setContentPane(panelPrincipal);
    this.setSize(ANCHO, ALTO);
    this.setVisible(true);
  }

  /**
   * Reescritura del m&eacute;todo "actionPerformed"
   * para los botones "aceptar" y "cancelar"
   *
   * @param arg0 Evento disparado
   * @see ActionListener
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    if ("aceptar".equals(arg0.getActionCommand())) {
      mostrarMensajeCambios();
      guardarCambios();
      this.dispose();
    }
    if ("cancelar".equals(arg0.getActionCommand())) {
      this.dispose();
    }    
    if("cambiarColor".equals(arg0.getActionCommand())) {
      JFrame.setDefaultLookAndFeelDecorated(false);
      new PanelColores();
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
  }
  
  /**
   * M&eacute;todo encargado de mostrar
   * una ventana indicando que han tenido lugar
   * cambios
   */
  private void mostrarMensajeCambios() {
    JOptionPane.showMessageDialog(this,
        this.idioma.get("mm_algunosCambios"),
        this.idioma.get("c_opciones"),
        JOptionPane.INFORMATION_MESSAGE);
  }
  
  /**
   * M&eacute;todo encargado de almacenar los
   * cambios en el fichero de opciones
   * 
   */
  private void guardarCambios() {
    try {
      BufferedWriter bEscritura = new BufferedWriter(new FileWriter(this.fichero));
      bEscritura.write("idioma=" + String.valueOf(this.boxIdiomas.getSelectedIndex()) + "\n");
      Color auxColor = this.pPrincipal.getElemColor();
      bEscritura.write("color=" + auxColor.getRed() + "," + auxColor.getGreen() + "," + auxColor.getBlue() + "\n");
      bEscritura.write("debug=" + String.valueOf(this.boxConsola.getSelectedIndex()));  
      bEscritura.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  } 
  
  private class PanelColores extends JFrame implements ChangeListener {

    private JColorChooser colorC;
    
    public PanelColores() {
      super("Paleta de Colores");
      JPanel panelContenido = new JPanel(new BorderLayout());
      this.colorC = new JColorChooser(pPrincipal.getElemColor());
      this.colorC.getSelectionModel().addChangeListener(this);
      this.colorC.setPreviewPanel(new JPanel());
      for (AbstractColorChooserPanel aux: this.colorC.getChooserPanels()) {
        if (!aux.getDisplayName().equals("RGB")) {
          this.colorC.removeChooserPanel(aux);
        }
      }
      panelContenido.add(this.colorC, BorderLayout.CENTER);
      this.setContentPane(panelContenido);
      this.pack();
      this.setVisible(true);
      this.setLocationRelativeTo(null);
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
      pPrincipal.setElemColor(this.colorC.getColor());
      bColor.setBackground(this.colorC.getColor());
    }
    
  }

}
