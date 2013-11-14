package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaEspecificacionHistograma extends JFrame implements ActionListener {

  private HashMap<String, String> idioma;
  private PanelPrincipal pPrincipal;
  private JButton bSeleccionar;
  private JButton bCancelar;
  private JComboBox<String> cbImagenes;
  private String[] nombresImagenes;
  private PanelVistaPrevia pVista;  
  private VentanaImagen vSeleccionada;
  
  public VentanaEspecificacionHistograma(HashMap<String, String> idioma, PanelPrincipal panelPrincipal) {
    super(idioma.get("s_espHistograma"));
    this.idioma = idioma;
    this.pPrincipal = panelPrincipal;
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
    this.setResizable(false);
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
      calcularEspecificacion();
      this.dispose();
    }

  }
  
  private void calcularEspecificacion() {
    VentanaImagen imgFoco = this.pPrincipal.getImgFoco();
    final int MAX_PIXELS = 256;
    int[] Vout = new int[MAX_PIXELS];
    double[] pA = normalizarHistograma(imgFoco.getNivelGris());
    double[] pB = normalizarHistograma(this.vSeleccionada.getNivelGris());
    
    for (int a = 0; a < MAX_PIXELS; a++) {
      int j = MAX_PIXELS - 1;
      do {
        Vout[a] = j;
        j--;
      } while ((j >= 0) && (pA[a] <= pB[j]));
    }    
    this.pPrincipal.duplicarImagen();
    this.pPrincipal.getImgFoco().ajustarPixels(Vout);
    this.pPrincipal.getImgFoco().fijarGris(true);    
  }
  
  private double[] normalizarHistograma(int[] nGris) {
    final int MAX_PIXELS = 256;
    int n = 0;
    double[] p = new double[MAX_PIXELS];
    int c = 0;
    
    for (int i = 0; i < MAX_PIXELS; i++)
      n += nGris[i];
    for (int i = 0; i < MAX_PIXELS; i++) {
      c += nGris[i];
      p[i] = (double) c / n;
    }    
    return (p);      
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
