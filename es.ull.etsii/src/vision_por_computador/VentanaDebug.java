package vision_por_computador;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class VentanaDebug extends JFrame {
  
  private JTextArea log;
  private JPanel panel;
  
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
  
  public void mostrarDebug(boolean visible) {
    this.setVisible(visible);
  }
  
  public void escribirMensaje(String mensaje) {
    this.log.append(mensaje + "\n");
    this.log.setCaretPosition(this.log.getDocument().getLength());
  }
  
  public boolean estadoVisible() {
    return this.isVisible();
  }

}
