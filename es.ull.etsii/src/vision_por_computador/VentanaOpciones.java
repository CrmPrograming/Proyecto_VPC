package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
  private String[] idioma;

  /**
   * Instancia un nuevo objeto
   * de tipo VentanaOpciones.
   *
   * @param idioma Array con las cadenas en el idioma actual
   * @param FICHERO String con el nombre del fichero de opciones
   */
  public VentanaOpciones(String[] idioma, final String FICHERO) {
    super(idioma[3]);
    this.setLocationRelativeTo(null);
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    this.idioma = idioma;
    this.fichero = FICHERO;
    this.panelBotones = new JPanel(new FlowLayout());
    this.panelOpciones = new JPanel(new GridLayout(N_FILAS, N_COLUMNAS));
    this.botonAceptar = new JButton(idioma[17]);
    this.botonAceptar.setActionCommand("aceptar");
    this.botonAceptar.addActionListener(this);
    this.botonCancelar = new JButton(idioma[18]);
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
  }
  
  /**
   * M&eacute;todo encargado de mostrar
   * una ventana indicando que han tenido lugar
   * cambios
   */
  private void mostrarMensajeCambios() {
    JOptionPane.showMessageDialog(this,
        this.idioma[19],
        this.idioma[3],
        JOptionPane.INFORMATION_MESSAGE);
  }
  
  /**
   * M&eacute;todo encargado de almacenar los
   * cambios en el fichero de opciones
   * 
   * <p><b>Anotaciones</b></p>
   * Implementar m&eacute;todo con hilos
   */
  @Anotaciones(desc = "Formatear mejor la estructura del fichero")
  private void guardarCambios() {
    try {
      BufferedWriter bEscritura = new BufferedWriter(new FileWriter(this.fichero));
      bEscritura.write(String.valueOf(this.boxIdiomas.getSelectedIndex()) + "\n");
      bEscritura.write(String.valueOf(this.boxConsola.getSelectedIndex()));  
      bEscritura.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
