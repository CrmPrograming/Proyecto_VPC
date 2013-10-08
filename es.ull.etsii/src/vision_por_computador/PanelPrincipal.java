package vision_por_computador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class PanelPrincipal extends JFrame implements ActionListener, Idiomas {
  
  private final static String TITULO_APLICACION = "Sin nombre";
  private final String FICHERO_CONFIG = "config.txt";
  private final String VERSION = "vr 0.1.0";
  private final String FORMATO_FICHERO = "tif";
  private final String[] AUTORES = new String[] {"Noel Díaz Mesa" , "César Ravelo Martínez"};
  private JDesktopPane panelEscritorio;
  private String[] idioma;
  private VentanaDebug debug;
  private ArrayList<VentanaImagen> listaImagenes;
  private int cantidadImagenes = 0;
  private VentanaImagen imagenFocus;

  public PanelPrincipal() {
    super(TITULO_APLICACION);
    this.imagenFocus = null;
    this.debug = new VentanaDebug();
    cargarConfiguracion();
    this.listaImagenes = new ArrayList<VentanaImagen>();    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setBounds(inset, inset, screenSize.width  - inset * 2, screenSize.height - inset*2);
    this.panelEscritorio = new JDesktopPane();
    this.setContentPane(this.panelEscritorio);
    this.setJMenuBar(crearMenu());    
    this.setJMenuBar(crearMenu());
    this.panelEscritorio.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    this.setVisible(true);
  }
  
  private JMenuBar crearMenu() {
    JMenuBar barraMenu = new JMenuBar();
    JMenu menu = null;
    JMenuItem menuItem = null;
    
    // Menu "Archivo"
    
    menu = new JMenu(this.idioma[0]);
    menu.setMnemonic(KeyEvent.VK_A);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem(this.idioma[5]);
    menuItem.setMnemonic(KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("abrir");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[6]);
    menuItem.setMnemonic(KeyEvent.VK_G);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("guardar");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[7]);
    menuItem.setMnemonic(KeyEvent.VK_C);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("guardarComo");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[8]);
    menuItem.setMnemonic(KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("salir");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    // Menu "Información"
    
    menu = new JMenu(this.idioma[1]);
    menu.setMnemonic(KeyEvent.VK_F);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem(this.idioma[9]);
    menuItem.setMnemonic(KeyEvent.VK_H);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("histogramaAbsoluto");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[10]);
    menuItem.setMnemonic(KeyEvent.VK_H);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("histogramaAcumulativo");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[11]);
    menuItem.setMnemonic(KeyEvent.VK_E);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("entropia");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[12]);
    menuItem.setMnemonic(KeyEvent.VK_F);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("info");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    // Menu "Operaciones"
    
    menu = new JMenu(this.idioma[2]);
    menu.setMnemonic(KeyEvent.VK_O);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem(this.idioma[13]);
    menuItem.setMnemonic(KeyEvent.VK_D);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("duplicar");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[14]);
    menuItem.setMnemonic(KeyEvent.VK_G);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("cambiarGris");
    menuItem.addActionListener(this);
    menu.add(menuItem);    
    
    // Menu "Ayuda"
    
    menu = new JMenu(this.idioma[4]);
    menu.setMnemonic(KeyEvent.VK_Y);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem("Debug");
    menuItem.setMnemonic(KeyEvent.VK_B);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("debug");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[3]);
    menuItem.setMnemonic(KeyEvent.VK_P);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("opciones");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma[15]);
    menuItem.setMnemonic(KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("acercaDe");
    menuItem.addActionListener(this);
    menu.add(menuItem);    
    
    return barraMenu;
  }  
  
  private void cargarConfiguracion() {
    try {
      BufferedReader bLectura = new BufferedReader(new FileReader(FICHERO_CONFIG));
      int idiom = Integer.valueOf(bLectura.readLine()).intValue();
      switch (idiom) {
        case 1:
          this.idioma = Idiomas.I_ENGLISH;
          break;
        case 0:
        default:
          this.idioma = Idiomas.I_ESPANOL;          
      }
      if ((Integer.valueOf(bLectura.readLine()).intValue()) == 1) {
        this.debug.mostrarDebug(true);
      }
      bLectura.close();
    } catch (Exception e) {
      System.err.println("> Fichero de configuración no encontrado o no válido. Creando nuevo fichero:");
      try {
        BufferedWriter bEscritura = new BufferedWriter(new FileWriter(FICHERO_CONFIG));
        bEscritura.write("0\n");
        bEscritura.write("0");
        this.idioma = Idiomas.I_ESPANOL;
        bEscritura.close();
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
  }
  
  @Override
  public void actionPerformed(ActionEvent arg0) {
    this.debug.escribirMensaje("> Abriendo pestaña '" + arg0.getActionCommand() + "'");
    if ("abrir".equals(arg0.getActionCommand())) {
      cargarImagen();
    }
    if ("salir".equals(arg0.getActionCommand())) {
      cerrar();
    }    
    if ("opciones".equals(arg0.getActionCommand())) {
      JFrame.setDefaultLookAndFeelDecorated(false);
      new VentanaOpciones(this.idioma, FICHERO_CONFIG);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
    if ("duplicar".equals(arg0.getActionCommand())) {
      duplicarImagen();
    }    
    if ("acercaDe".equals(arg0.getActionCommand())) {
      mostrarAcercaDe();
    }
    if ("debug".equals(arg0.getActionCommand())) {      
      this.debug.mostrarDebug(!this.debug.estadoVisible());
    }
    if ("guardar".equals(arg0.getActionCommand())) {
      guardarImagen();
    }
    if ("guardarComo".equals(arg0.getActionCommand())) {
      guardarImagenComo();
    }
    if ("cambiarGris".equals(arg0.getActionCommand())) {
      cambiarImagenGris();
    }
    if ("info".equals(arg0.getActionCommand())) {
      mostrarInformacion();
    }
    if ("histogramaAbsoluto".equals(arg0.getActionCommand())) {
      histogramaAbsoluto();
    }
    if ("histogramaAcumulativo".equals(arg0.getActionCommand())) {
      histogramaAcumulativo();
    }
  }
  
  private void cargarImagen() {
    boolean seguir = false;
    JFileChooser fc = null;
    do {
      fc = new JFileChooser();    
      FileNameExtensionFilter tiffFilter = new FileNameExtensionFilter(this.idioma[15], FORMATO_FICHERO);
      fc.setFileFilter(tiffFilter);
      fc.setDialogTitle(this.idioma[5]);
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File fichero = fc.getSelectedFile();
        if (fichero.getName().toLowerCase().contains(FORMATO_FICHERO)) {
          seguir = true;  
          try {
            BufferedImage bImage = null;
            bImage = construirImagen(fichero);
            VentanaImagen aux;
            aux = new VentanaImagen(this.cantidadImagenes, 
                                    bImage, 
                                    fichero.getName(), 
                                    this.debug, 
                                    this.listaImagenes, 
                                    this,
                                    fichero.getAbsolutePath());
            this.listaImagenes.add(aux);
            this.add(aux);
            this.debug.escribirMensaje("> Se ha cargado la imagen " + fichero.getName());
            this.cantidadImagenes++;
            this.imagenFocus = aux;
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }          
        } else {
          mostrarError(21);
          this.debug.escribirMensaje("> Se ha producido el error " + this.idioma[21]);
        }
      } else {
        seguir = true;
      }
    } while (!seguir);    
  }
  
  private BufferedImage construirImagen(File selectedFile) throws IOException {
    FileSeekableStream stream = new FileSeekableStream(selectedFile.getAbsolutePath());
    TIFFDecodeParam decodeParam = new TIFFDecodeParam();
    decodeParam.setDecodePaletteAsShorts(true);
    ParameterBlock params = new ParameterBlock();
    params.add(stream);
    RenderedOp image1 = JAI.create("tiff", params);
    BufferedImage img = image1.getAsBufferedImage();
    return img;
  }

  private void mostrarError(int idError) {
    JFrame.setDefaultLookAndFeelDecorated(false);
    JOptionPane.showMessageDialog(this,
        this.idioma[idError],
        "Error",
        JOptionPane.ERROR_MESSAGE);
    JFrame.setDefaultLookAndFeelDecorated(true);
  }
  
  private void cerrar() {
    System.exit(0);    
  }
  
  private void mostrarAcercaDe() {
    JFrame.setDefaultLookAndFeelDecorated(false);
    JOptionPane.showMessageDialog(this,
        this.idioma[20] + ": \n   - " + AUTORES[0] + "\n   - " + AUTORES[1] + "\n" + VERSION,
        this.idioma[15],
        JOptionPane.INFORMATION_MESSAGE);
    JFrame.setDefaultLookAndFeelDecorated(true);
  }
  
  private void duplicarImagen() {
    if (this.imagenFocus == null) {
      mostrarError(22);      
    } else {
      String[] nombre = this.imagenFocus.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = this.imagenFocus.getRuta().split(this.imagenFocus.getNombre());
      String nuevoNombre = nombre[0] + "_copia." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            this.imagenFocus.getImagen(), 
                                            nuevoNombre, 
                                            this.debug, 
                                            this.listaImagenes, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.add(aux);
      this.debug.escribirMensaje("> Se ha duplicado la imagen en foco");
      this.cantidadImagenes++;
      this.imagenFocus = aux;
    }
  }
  
  private void guardarImagen() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else {
      TIFFEncodeParam params = new TIFFEncodeParam();
      params.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
      JAI.create("filestore", this.imagenFocus.getImagen(), this.imagenFocus.getRuta(), "TIFF", params);
      this.debug.escribirMensaje("> Guardada imagen en ruta: \n" + this.imagenFocus.getRuta());
    }
  }
  
  private void guardarImagenComo() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else {
      JFileChooser fC = new JFileChooser();
      FileNameExtensionFilter tiffFilter = new FileNameExtensionFilter(this.idioma[16], FORMATO_FICHERO);
      fC.setFileFilter(tiffFilter);
      fC.setCurrentDirectory(new File(this.imagenFocus.getRuta()));
      int valor = fC.showSaveDialog(this);
      if (valor == JFileChooser.APPROVE_OPTION) {
        TIFFEncodeParam params = new TIFFEncodeParam();
        params.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
        File fichero = fC.getSelectedFile();
        String ruta = "";
        if (!fichero.getName().toLowerCase().contains(FORMATO_FICHERO)) {
          ruta = fichero + "." + FORMATO_FICHERO;
        } else {
          ruta = fichero.toString();
        }
        JAI.create("filestore", this.imagenFocus.getImagen(), ruta, "TIFF", params);
        this.debug.escribirMensaje("> Guardada imagen en ruta: \n" + ruta);
      }
    }
  }
  
  private void cambiarImagenGris() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else {
      BufferedImage imagenColor = this.imagenFocus.getImagen();
      BufferedImage imagenGris = new BufferedImage(imagenColor.getWidth(), imagenColor.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      ColorConvertOp op = new ColorConvertOp(imagenColor.getColorModel().getColorSpace(),
                                             imagenGris.getColorModel().getColorSpace(), null);
      op.filter(imagenColor, imagenGris);    
      String[] nombre = this.imagenFocus.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = this.imagenFocus.getRuta().split(this.imagenFocus.getNombre());
      String nuevoNombre = nombre[0] + "_gris." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imagenGris, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this.listaImagenes, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.add(aux);
      this.debug.escribirMensaje("> Se ha cambiado a escala de grices la imagen en foco");
      this.cantidadImagenes++;
      aux.fijarGris(true);
      this.imagenFocus = aux;
    }
  }
  
  private void mostrarInformacion() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else {
      BufferedImage bImagen = this.imagenFocus.getImagen();
      int min = this.imagenFocus.getValorMin();
      int max = this.imagenFocus.getValorMax();
      String informacion = "";
      informacion += "Tipo de fichero: " + FORMATO_FICHERO + "\n";
      informacion += "Tamaño de la imagen: " + bImagen.getWidth() + "x" + bImagen.getHeight() + "\n";
      informacion += "Rango de valores [min, max]: " 
                  + ((this.imagenFocus.esGris())? ("[" + min + ", " + max + "]"): "[null]") 
                  + "\n";
      JFrame.setDefaultLookAndFeelDecorated(false);
      JOptionPane.showMessageDialog(this,
          informacion,
          this.idioma[12],
          JOptionPane.INFORMATION_MESSAGE);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
  }
  
  private void histogramaAbsoluto() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()){
      mostrarError(23);
    } else {
      this.imagenFocus.dibujarHistogramaAbsoluto(this.idioma[9]);
    }
  }
  
  private void histogramaAcumulativo() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()){
      mostrarError(23);
    } else {
      this.imagenFocus.dibujarHistogramaAcumulativo(this.idioma[9]);
    }
  }
  
  public void setFocus(VentanaImagen vI) {
    this.imagenFocus = vI;
  }
  
  public void setCantidadImagenes(int valor) {
    this.cantidadImagenes = valor;
  }
  
  public int getCantidadImagenes() {
    return this.cantidadImagenes;
  }

}
