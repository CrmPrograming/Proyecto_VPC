package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class VentanaDebug extends JFrame {
  
  /**
   *  JTextArea donde se muestran los mensajes de log
   */
  private JTextArea log;
  /**
   *  JPanel donde se incluye el log
   */
  private JPanel panel;
  
  /**
   * Instancia un nuevo objeto
   * de tipo VentanaDebug.
   */
  public VentanaDebug() {
    super("Console Log");
    this.panel = new JPanel(new BorderLayout());
    this.log = new JTextArea(5, 40);
    this.log.setMargin(new Insets(5, 5, 5, 5));
    this.log.setEditable(false);
    JScrollPane logScrollPane = new JScrollPane(log);
    this.panel.add(logScrollPane, BorderLayout.CENTER);
    this.setContentPane(this.panel);
    this.pack();
    this.setResizable(false);
    this.setAlwaysOnTop(true);
    this.setVisible(false);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      
      public void windowClosing(WindowEvent e) {
        escribirMensaje("> Cerrando ventana 'debug'");
        setVisible(false);
      }
      
    });
    escribirMensaje("> Inicio de programa");
  }
  
  /**
   * Muestra/Oculta la ventana debug
   *
   * @param visible Valor booleano correspondiente a mostrar/ocultar ventana
   */
  public void mostrarDebug(boolean visible) {
    this.setVisible(visible);
  }
  
  /**
   * Escribe el mensaje dado por par&aacute;metro
   * en el log
   *
   * @param mensaje Mensaje a escribir
   */
  public void escribirMensaje(String mensaje) {
    this.log.append(mensaje + "\n");
    this.log.setCaretPosition(this.log.getDocument().getLength());
  }
  
  /**
   * M&eacute;todo encargado de indicar si
   * la ventana est&aacute; visible o no
   *
   * @return true, si la ventana est&aacute; visible
   */
  public boolean estadoVisible() {
    return this.isVisible();
  }

}
