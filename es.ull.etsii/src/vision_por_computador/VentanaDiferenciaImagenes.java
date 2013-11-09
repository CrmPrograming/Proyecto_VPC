package vision_por_computador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.ui.RefineryUtilities;

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
  private HistogramaAbsoluto histo;

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
      calcularDiferencias();
      this.dispose();
    }
    
  }
  
  private void calcularDiferencias() {
    BufferedImage imgFoco = this.pPrincipal.getImgFoco().getImagen();
    BufferedImage imgSeg = this.vSeleccionada.getImagen();
    int[] nGris = new int[256];
    int[][] dif = new int[imgFoco.getWidth()][imgFoco.getHeight()];
    for (int i = 0; i < imgFoco.getWidth(); i++) {      
      for (int j = 0; j < imgFoco.getHeight(); j++) {
        dif[i][j] = Math.abs(new Color(imgFoco.getRGB(i, j)).getRed() - new Color(imgSeg.getRGB(i, j)).getRed());
      }
    }   
    for (int i = 0; i < 256; i++) {
      nGris[i] = 0;
    }
    for (int i = 0; i < imgFoco.getWidth(); i++) {      
      for (int j = 0; j < imgFoco.getHeight(); j++) {
        nGris[dif[i][j]]++;
      }
    } 
    this.histo = new HistogramaAbsoluto("Histograma temporal", nGris);    
    this.histo.pack();
    RefineryUtilities.centerFrameOnScreen(this.histo);
    this.histo.setVisible(true);
    JFrame.setDefaultLookAndFeelDecorated(false);
    new VentanaUmbral();
    JFrame.setDefaultLookAndFeelDecorated(true);
  }
  
  private class VentanaUmbral extends JFrame implements ActionListener {
    
    private JTextField tfUmbral;
    private JButton bAplicar;
    private JButton bCancelar;
    
    public VentanaUmbral() {
      super("Umbral T");
      JPanel panelVentana = new JPanel(new GridLayout(2, 1));
      JPanel panelBotones = new JPanel();
      this.tfUmbral = new JTextField(4);
      this.bAplicar = new JButton("Aplicar");
      this.bAplicar.setActionCommand("aplicar");
      this.bAplicar.addActionListener(this);
      this.bCancelar = new JButton("Cancelar");
      this.bCancelar.setActionCommand("cancelar");
      this.bCancelar.addActionListener(this);
      panelBotones.add(this.bAplicar);
      panelBotones.add(this.bCancelar);
      panelVentana.add(this.tfUmbral);
      panelVentana.add(panelBotones);    
      this.setResizable(false);
      this.setSize(180, 100);
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      this.setContentPane(panelVentana);
      this.setLocationRelativeTo(null);
      this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if ("aplicar".equals(e.getActionCommand())) {
        try {
          Integer.parseInt(tfUmbral.getText());
          construirMapa();
        } catch (NumberFormatException e2) {
          pPrincipal.mostrarError(24);
        }
      }
      
      if ("cancelar".equals(e.getActionCommand())) {
        dispose();
        histo.dispose();
      }
    }
    
    private void construirMapa() {
      if (pPrincipal.getImgFoco() == null) {
        pPrincipal.mostrarError(22);      
      } else {
        final String FORMATO_FICHERO = "tif";
        VentanaImagen imgFoco = pPrincipal.getImgFoco();
        String[] nombre = imgFoco.getNombre().split("." + FORMATO_FICHERO);
        String[] ruta = imgFoco.getRuta().split(imgFoco.getNombre());
        String nuevoNombre = nombre[0] + "_mapa." + FORMATO_FICHERO; 
        String nuevaRuta = ruta[0] + nuevoNombre;
        BufferedImage imagenOriginal = imgFoco.getImagen();
        BufferedImage imagenNueva = new BufferedImage(imagenOriginal.getWidth(), imagenOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);
        final int UMBRAL = Integer.parseInt(this.tfUmbral.getText());
        for (int i = 0; i < imagenNueva.getWidth(); i++) {
          for (int j = 0; j < imagenNueva.getHeight(); j++) {
            Color auxColor = new Color(imagenOriginal.getRGB(i, j));
            if (auxColor.getRed() >= UMBRAL) {
              auxColor = Color.RED;
            }
            imagenNueva.setRGB(i, j, auxColor.getRGB());
          }
        }
        VentanaImagen aux = new VentanaImagen(pPrincipal.getCantidadImagenes(), 
                                              imagenNueva, 
                                              nuevoNombre, 
                                              pPrincipal.getVentanaDebug(), 
                                              pPrincipal,
                                              nuevaRuta);   
        pPrincipal.getListaImagenes().add(aux);
        pPrincipal.add(aux);
        pPrincipal.getVentanaDebug().escribirMensaje("> Se ha construido el mapa de diferencia");    
      }
    }
    
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
