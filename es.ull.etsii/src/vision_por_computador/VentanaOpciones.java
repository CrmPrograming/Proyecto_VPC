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

public class VentanaOpciones extends JFrame implements ActionListener {
  
  private final int ANCHO = 300;
  private final int ALTO = 500;
  private final int N_FILAS = 0;
  private final int N_COLUMNAS = 2;  
  private final String[] LISTA_IDIOMAS = new String[] {"Español", "English"};
  private String fichero;
  private JPanel panelOpciones;
  private JPanel panelBotones;
  private JButton botonAceptar;
  private JButton botonCancelar;
  private JComboBox<String> boxIdiomas;
  private JLabel lIdioma;
  private JLabel lConsola;
  private JComboBox<String> boxConsola;
  private String[] idioma;

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
  
  private void mostrarMensajeCambios() {
    JOptionPane.showMessageDialog(this,
        this.idioma[19],
        this.idioma[3],
        JOptionPane.INFORMATION_MESSAGE);
  }
  
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
