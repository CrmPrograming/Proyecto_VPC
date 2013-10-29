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

@SuppressWarnings("serial")
public class PanelPrincipal extends JFrame implements ActionListener, Idiomas {
  
  /**
   * Constante con el nombre de la aplicaci&oacute;n
   */
  private final static String TITULO_APLICACION = "Sin nombre";
  /**
   * Constante con el nombre del fichero con la configuraci&oacute;n
   */
  private final String FICHERO_CONFIG = "config.txt";
  /**
   * Constante con la versi&oacute;n del programa
   */
  private final String VERSION = "vr 0.1.0";
  /**
   * Constante con el nombre del formato por defecto de las im&aacute;genes
   */
  private final String FORMATO_FICHERO = "tif";
  /**
   * Array constante con el nombre de los autores
   */
  private final String[] AUTORES = new String[] {"Noel Díaz Mesa" , "César Ravelo Martínez"};
  /**
   * JDesktopPane principal del programa sobre el cual
   * se abrir&aacute;n las im&aacute;genes
   */
  private JDesktopPane panelEscritorio;
  /**
   * Array con las cadenas del idioma actual
   */
  private String[] idioma;
  /**
   * Instanciaci&oacute;n de la ventana debug del programa
   */
  private VentanaDebug debug;
  /**
   * Lista de im&aacute;genes abiertas actualmente
   */
  private ArrayList<VentanaImagen> listaImagenes;
  
  /**
   * Cantidad de im&aacute;genes abiertas
   */
  private int cantidadImagenes = 0;
  /**
   * Im&aacute;gen actual en foco
   */
  private VentanaImagen imagenFocus;
  
  private Color colorElementos;

  /**
   * Instancia un nuevo objeto
   * de tipo PanelPrincipal.
   */
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
    this.panelEscritorio.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    this.setVisible(true);
  }
  
  /**
   * Establece el men&uacute; en la barra principal
   *
   * @return JMenuBar
   */
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
  
  /**
   * Carga la configuraci&oacute;n del
   * programa del fichero definido en la constante
   * FICHERO_CONFIG
   * 
   */
  private void cargarConfiguracion() {
    try {
      final String SEPARADOR = "=";
      BufferedReader bLectura = new BufferedReader(new FileReader(FICHERO_CONFIG));
      int idiom = Integer.valueOf(bLectura.readLine().split(SEPARADOR)[1]).intValue();
      switch (idiom) {
        case 1:
          this.idioma = Idiomas.I_ENGLISH;
          break;
        case 0:
        default:
          this.idioma = Idiomas.I_ESPANOL;          
      }
      String lineaColores = bLectura.readLine().split(SEPARADOR)[1];      
      int r = Integer.valueOf(lineaColores.split(",")[0]).intValue();
      int g = Integer.valueOf(lineaColores.split(",")[1]).intValue();
      int b = Integer.valueOf(lineaColores.split(",")[2]).intValue();
      comprobarColores(r, g, b);
      this.colorElementos = new Color(r, g, b);
      if ((Integer.valueOf(bLectura.readLine().split(SEPARADOR)[1]).intValue()) == 1) {
        this.debug.mostrarDebug(true);
      }      
      bLectura.close();
    } catch (Exception e) {
      System.err.println("> Fichero de configuración no encontrado o no válido. Creando nuevo fichero:");
      try {
        BufferedWriter bEscritura = new BufferedWriter(new FileWriter(FICHERO_CONFIG));
        bEscritura.write("idioma=0\n");
        bEscritura.write("color=255,0,0\n");
        bEscritura.write("debug=0");
        this.idioma = Idiomas.I_ESPANOL;
        bEscritura.close();
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
  }
  
  private void comprobarColores(int r, int g, int b) throws Exception {
    if ((r > 255) || (r < 0)) {
      throw new Exception();
    }
    if ((g > 255) || (g < 0)) {
      throw new Exception();
    }
    if ((b > 255) || (b < 0)) {
      throw new Exception();
    }
  }
  
  /**
   * Sobreescritura del m&eacute;todo  "actionPerformed"
   * para realizar las operaciones del bot&oacute;n 
   * seleccionado
   *
   * @param arg0 Evento disparado
   */
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
      new VentanaOpciones(this.idioma, FICHERO_CONFIG, this);
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
  
  /**
   * M&eacute;todo encargado de 
   * buscar la imagen en disco y luego
   * abrirla en una VentanaImagen
   * 
   * @see VentanaImagen
   */
  private void cargarImagen() {
    boolean seguir = false;
    JFileChooser fc = null;
    do {
      fc = new JFileChooser(); 
      fc.setPreferredSize(new Dimension(800, 600));
      fc.setAccessory(new VistaPrevia(fc));
      FileNameExtensionFilter tiffFilter = new FileNameExtensionFilter(this.idioma[16], FORMATO_FICHERO);
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
  
  /**
   * M&eacute;todo encargado de extraer
   * la imagen desde fichero
   *
   * @param selectedFile Fichero de la imagen
   * @return buffered Imagen en memoria
   * @throws IOException Se&ntilde;al de que se ha producido una excepci&oacute;n de tipo I/O
   */
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

  /**
   * M&eacute;todo encargado de mostrar
   * una ventana con el error producido
   *
   * @param idError Identificador del error
   */
  private void mostrarError(int idError) {
    JFrame.setDefaultLookAndFeelDecorated(false);
    JOptionPane.showMessageDialog(this,
        this.idioma[idError],
        "Error",
        JOptionPane.ERROR_MESSAGE);
    JFrame.setDefaultLookAndFeelDecorated(true);
  }
  
  /**
   * M&eacute;todo encargado de cerrar la aplicaci&oacute;n
   */
  private void cerrar() {
    System.exit(0);    
  }
  
  /**
   * M&eacute;todo encargado de mostrar
   * la ventana "Acerca de"
   */
  private void mostrarAcercaDe() {
    JFrame.setDefaultLookAndFeelDecorated(false);
    JOptionPane.showMessageDialog(this,
        this.idioma[20] + ": \n   - " + AUTORES[0] + "\n   - " + AUTORES[1] + "\n" + VERSION,
        this.idioma[15],
        JOptionPane.INFORMATION_MESSAGE);
    JFrame.setDefaultLookAndFeelDecorated(true);
  }
  
  /**
   * M&eacute;todo encargado de duplicar la imagen
   * en foco creando una nueva ventana con la misma
   * imagen
   */
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
  
  /**
   * M&eacute;todo encargado de guardar
   * la imagen en el fichero con el mismo nombre
   * de la im&aacute;gen y en su ruta 
   */
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
  
  /**
   * M&eacute;todo encargado de preguntar
   * al usuario c&oacute;mo quiere guardar la 
   * imagen y en d&oacute;nde
   */
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
  
  /**
   * M&eacute;todo encargado de cambiar la imagen 
   * a escala de gris 
   * 
   * <p><b>Anotaciones</b></p>
   * Revisar transformaci&oacute;n de color a gris
   */
  @Anotaciones(desc = "Revisar transformación de color a gris")  
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
    
  /**
   * M&eacute;todo encargado de mostrar la informaci&oacute;n
   * correspondiente a la imagen en foco
   * 
   * <p><b>Anotaciones</b></p>
   * Falta a&ntilde;adir la informaci&oacute;n de brillo y contraste
   */
  @Anotaciones(desc = "Falta añadir la información de brillo y contraste")
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
  
  /**
   * M&eacute;todo encargado de mostrar el
   * histograma absoluto de la imagen en foco
   */
  private void histogramaAbsoluto() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()){
      mostrarError(23);
    } else {
      this.imagenFocus.dibujarHistogramaAbsoluto(this.idioma[9]);
    }
  }
  
  /**
   * M&eacute;todo encargado de mostrar el histograma
   * acumulativo de la imagen en foco
   */
  private void histogramaAcumulativo() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()){
      mostrarError(23);
    } else {
      this.imagenFocus.dibujarHistogramaAcumulativo(this.idioma[9]);
    }
  }
  
  /**
   * M&eacute;todo setter para cambiar 
   * la imagen en foco
   *
   * @param vI Nueva imagen en foco
   */
  public void setFocus(VentanaImagen vI) {
    this.imagenFocus = vI;
  }
  
  /**
   * M&eacute;todo setter para cambiar
   * la cantidad de im&aacute;genes abiertas
   *
   * @param valor Nueva cantidad de im&aacute;genes
   */
  public void setCantidadImagenes(int valor) {
    this.cantidadImagenes = valor;
  }
  
  /**
   * M&eacute;todo Getter para retornar
   * la cantidad de im&aacute;genes abiertas
   *
   * @return cantidadImagenes
   */
  public int getCantidadImagenes() {
    return this.cantidadImagenes;
  }
  
  public Color getElemColor() {
    return this.colorElementos;
  }
  
  public void setElemColor(Color nColor) {
    this.colorElementos = nColor;
  }

}
