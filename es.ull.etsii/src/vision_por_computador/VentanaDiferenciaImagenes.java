package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VentanaDiferenciaImagenes extends JFrame implements ActionListener {
  
  private PanelPrincipal pPrincipal;
  private HashMap<String, String> idioma;
  private JButton bSeleccionar;
  private JButton bCancelar;
  private JComboBox<String> cbImagenes;
  private String[] nombresImagenes;
  private PanelVistaPrevia pVista;  
  private VentanaImagen vSeleccionada;

  public VentanaDiferenciaImagenes(HashMap<String, String> idioma, PanelPrincipal panelPrincipal) {
    super("Diferencia de Imágenes");
    this.pPrincipal = panelPrincipal;
    this.idioma = idioma;    
    JPanel panelVentana = new JPanel(new BorderLayout());
    JPanel panelBotones = new JPanel();
    this.pVista = new PanelVistaPrevia();
    this.bSeleccionar = new JButton("Seleccionar");
    this.bCancelar = new JButton("Cancelar");
    this.bSeleccionar.addActionListener(this);
    this.bCancelar.addActionListener(this);
    this.bSeleccionar.setActionCommand("seleccionar");
    this.bCancelar.setActionCommand("cancelar");
    panelBotones.add(this.bSeleccionar);
    panelBotones.add(this.bCancelar);
    final int CANTIDAD_IMAGENES = panelPrincipal.getCantidadImagenes();
    this.nombresImagenes = new String[CANTIDAD_IMAGENES];
    int i = 1;
    this.nombresImagenes[0] = "Seleccionar Imagen";
    for (VentanaImagen aux: panelPrincipal.getListaImagenes()) {
      if (!aux.getNombre().equals(panelPrincipal.getImgFoco().getNombre())) {
        this.nombresImagenes[i] = aux.getNombre();
        i++;
      }
    }
    this.cbImagenes = new JComboBox<String>(this.nombresImagenes);
    this.cbImagenes.setSelectedIndex(0);
    this.cbImagenes.setActionCommand("comboBox");
    this.cbImagenes.addActionListener(this);
    panelVentana.add(this.cbImagenes, BorderLayout.NORTH);
    panelVentana.add(this.pVista, BorderLayout.CENTER);
    panelVentana.add(panelBotones, BorderLayout.SOUTH);
    this.setContentPane(panelVentana);
    this.setSize(300, 300);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if ("cancelar".equals(arg0.getActionCommand())) {
      this.dispose();
    }
    
    if ("comboBox".equals(arg0.getActionCommand())) {
      this.pVista.repaint();
    }
    
    if ("seleccionar".equals(arg0.getActionCommand())) {
      calcularDiferencias();
      this.dispose();
    }
    
  }
  
  private void calcularDiferencias() {
    BufferedImage imgFoco = this.pPrincipal.getImgFoco().getImagen();
    BufferedImage imgSeg = this.vSeleccionada.getImagen();
    final int UMBRAL_T = 220;
    int[][] dif = new int[imgFoco.getWidth()][imgFoco.getHeight()];
    for (int i = 0; i < imgFoco.getWidth(); i++) {      
      for (int j = 0; j < imgFoco.getHeight(); j++) {
        dif[i][j] = Math.abs(new Color(imgFoco.getRGB(i, j)).getRed() - new Color(imgSeg.getRGB(i, j)).getRed());
      }
    }
    int[] Vout = new int[256];
    for (int i = 0; i < 256; i++) {  
      if (i > UMBRAL_T) {
        Vout[i] = 255;
      } else {
        Vout[i] = 0;
      }
    }
    this.pPrincipal.duplicarImagen();
    this.pPrincipal.getImgFoco().ajustarPixels(Vout);
  }
  
  private class PanelVistaPrevia extends JPanel {
    
    @Override
    public void paintComponent(Graphics g) {
      if (cbImagenes.getSelectedIndex() != 0) {
        String nombre = (String) cbImagenes.getSelectedItem();
        ArrayList<VentanaImagen> list = pPrincipal.getListaImagenes();
        boolean encontrado = false;
        int i = 0;
        do {
          if (list.get(i).getNombre().equals(nombre)) {
            encontrado = true;
          } else {
            i++;
          }
        } while ((!encontrado) && (i < list.size()));
        vSeleccionada = pPrincipal.getListaImagenes().get(i);
        ImageIcon icon = new ImageIcon(vSeleccionada.getImagen());
        if (icon.getIconWidth() > 220) {
          icon = new ImageIcon(icon.getImage().getScaledInstance(220, -1, Image.SCALE_DEFAULT));
        }
        icon.paintIcon(this, g, pVista.getWidth() / 10, pVista.getHeight() / 10);
      }      
    }
    
  }
  

}
