package vision_por_computador;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
  private HashMap<String, String> idioma;
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
  
  /**
   * Objeto de la clase Color con el color actual
   * de la interfaz
   * 
   * @see Color
   */
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
    this.panelEscritorio.setBackground(new Color(0x90CBC1));
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
    JMenu subMenu = null;
    JMenu subMenu2 = null;
    
    // Menu "Archivo"
    
    menu = new JMenu(this.idioma.get("c_archivo"));
    menu.setMnemonic(KeyEvent.VK_A);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem(this.idioma.get("s_abrir"));
    menuItem.setMnemonic(KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("abrir");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_guardar"));
    menuItem.setMnemonic(KeyEvent.VK_G);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("guardar");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_gComo"));
    menuItem.setMnemonic(KeyEvent.VK_C);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("guardarComo");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menu.addSeparator();
    
    menuItem = new JMenuItem(this.idioma.get("s_salir"));
    menuItem.setMnemonic(KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("salir");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    // Menu "Información"
    
    menu = new JMenu(this.idioma.get("c_informacion"));
    menu.setMnemonic(KeyEvent.VK_F);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem(this.idioma.get("s_hAbsoluto"));
    menuItem.setMnemonic(KeyEvent.VK_H);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("histogramaAbsoluto");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_hAcumulativo"));
    menuItem.setMnemonic(KeyEvent.VK_H);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("histogramaAcumulativo");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menu.addSeparator();
    
    menuItem = new JMenuItem(this.idioma.get("s_entropia"));
    menuItem.setMnemonic(KeyEvent.VK_E);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("entropia");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_iImagen"));
    menuItem.setMnemonic(KeyEvent.VK_F);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("info");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    // Menu "Operaciones"
    
    menu = new JMenu(this.idioma.get("c_operaciones"));
    menu.setMnemonic(KeyEvent.VK_O);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem(this.idioma.get("s_duplicar"));
    menuItem.setMnemonic(KeyEvent.VK_D);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("duplicar");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_cEscalaGrises"));
    menuItem.setMnemonic(KeyEvent.VK_G);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("cambiarGris");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    // Submenu Operaciones Lineales
    
    subMenu = new JMenu(this.idioma.get("s_oLineales"));
    subMenu.setMnemonic(KeyEvent.VK_L);
    subMenu.addActionListener(this);    

    menuItem = new JMenuItem("Transformaciones Lineales por tramos");
    menuItem.setMnemonic(KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("tLinealesTramos");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_aBrilloContraste"));
    menuItem.setMnemonic(KeyEvent.VK_B);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("aBrilloContraste");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);   
    
    menu.add(subMenu);
    
    // Submenu Operaciones No Lineales
    
    subMenu = new JMenu(this.idioma.get("s_nLineales"));
    subMenu.setMnemonic(KeyEvent.VK_N);
    subMenu.addActionListener(this);   
    
    menuItem = new JMenuItem(this.idioma.get("s_eqHistograma"));
    menuItem.setMnemonic(KeyEvent.VK_E);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("eqHistograma");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_espHistograma"));
    menuItem.setMnemonic(KeyEvent.VK_H);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("espHistograma");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_gamma"));
    menuItem.setMnemonic(KeyEvent.VK_G);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("gamma");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_dImagenes"));
    menuItem.setMnemonic(KeyEvent.VK_F);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("dImagenes");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_mCambios"));
    menuItem.setMnemonic(KeyEvent.VK_M);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("mCambios");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);   
    
    menu.add(subMenu);
    
    // Submenu Operaciones Geométricas
    
    subMenu = new JMenu(this.idioma.get("s_opGeometricas"));
    subMenu.setMnemonic(KeyEvent.VK_T);
    subMenu.addActionListener(this);   
    
    // Submenu Transformaciones Espejo
    
    subMenu2 = new JMenu(this.idioma.get("s_trasnEspejo"));
    subMenu2.setMnemonic(KeyEvent.VK_T);
    subMenu2.addActionListener(this);   
    
    menuItem = new JMenuItem(this.idioma.get("s_espejoHorizontal"));
    menuItem.setMnemonic(KeyEvent.VK_H);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("espHorizontal");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem);    
    
    menuItem = new JMenuItem(this.idioma.get("s_espejoVertical"));
    menuItem.setMnemonic(KeyEvent.VK_V);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("espVertical");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem);  
    
    subMenu.add(subMenu2);   
    
    // Traspuesta de imagen
    
    menuItem = new JMenuItem(this.idioma.get("s_traspuesta"));
    menuItem.setMnemonic(KeyEvent.VK_G);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("traspuestaImagen");
    menuItem.addActionListener(this);
    subMenu.add(menuItem);
    
    menu.add(subMenu);
    
    // Rotaciones de imagen
    
    subMenu = new JMenu(this.idioma.get("s_rotaciones"));
    subMenu.setMnemonic(KeyEvent.VK_T);
    subMenu.addActionListener(this);
    
    subMenu2 = new JMenu(this.idioma.get("s_m90"));
    subMenu2.setMnemonic(KeyEvent.VK_M);
    subMenu2.addActionListener(this); 
    
    menuItem = new JMenuItem(this.idioma.get("s_270"));
    menuItem.setMnemonic(KeyEvent.VK_2);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("s_270");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem); 
    
    menuItem = new JMenuItem(this.idioma.get("s_180"));
    menuItem.setMnemonic(KeyEvent.VK_1);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("s_180");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_90"));
    menuItem.setMnemonic(KeyEvent.VK_9);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("s_90");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem);
    
    subMenu2.addSeparator();
    
    menuItem = new JMenuItem(this.idioma.get("s_-90"));
    menuItem.setMnemonic(KeyEvent.VK_0);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("s_-90");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_-180"));
    menuItem.setMnemonic(KeyEvent.VK_8);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("s_-180");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("s_-270"));
    menuItem.setMnemonic(KeyEvent.VK_7);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("s_-270");
    menuItem.addActionListener(this);
    subMenu2.add(menuItem);
    
    subMenu.add(subMenu2);
    
    menu.add(subMenu);
    
    // Menu "Ayuda"
    
    menu = new JMenu(this.idioma.get("c_ayuda"));
    menu.setMnemonic(KeyEvent.VK_Y);
    barraMenu.add(menu);
    
    menuItem = new JMenuItem("Debug");
    menuItem.setMnemonic(KeyEvent.VK_B);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("debug");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem(this.idioma.get("c_opciones"));
    menuItem.setMnemonic(KeyEvent.VK_P);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
    menuItem.setActionCommand("opciones");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menu.addSeparator();
    
    menuItem = new JMenuItem(this.idioma.get("s_acerca"));
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
  
  /**
   *  M&eacute;todo encargado de comprobar que
   *  un color no supere los rangos [0, 255]
   * 
   *  @param r Color en canal Rojo
   *  @param g Color en canal Verde
   *  @param b Color en canal Azul
   *  @since  1.0
   *
   */
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
    if ("entropia".equals(arg0.getActionCommand())) {
      mostrarEntropia();
    }
    if ("aBrilloContraste".equals(arg0.getActionCommand())) {
      ajusteBrilloContraste();
    }
    if ("tLinealesTramos".equals(arg0.getActionCommand())) {
      transformacionLinealTramos();
    }    
    if ("eqHistograma".equals(arg0.getActionCommand())) {
      ecualizacionHistograma();
    }
    if ("gamma".equals(arg0.getActionCommand())) {
      operacionGamma();
    }
    if ("dImagenes".equals(arg0.getActionCommand())) {
      diferenciaDeImagenes(false);
    }
    if ("mCambios".equals(arg0.getActionCommand())) {
      diferenciaDeImagenes(true);
    }
    if ("espHistograma".equals(arg0.getActionCommand())) {
      especificacionHistograma();
    }    
    if ("espHorizontal".equals(arg0.getActionCommand())) {
      espejoHorizontal();
    }
    if ("espVertical".equals(arg0.getActionCommand())) {
      espejoVertical();
    }
    if ("traspuestaImagen".equals(arg0.getActionCommand())) {
      traspuestaImagen();
    }
    if ("s_90".equals(arg0.getActionCommand())) {
      rotacion(90);
    }
    if ("s_-270".equals(arg0.getActionCommand())) {
      rotacion(-270);
    }
    if ("s_180".equals(arg0.getActionCommand())) {
      rotacion(180);
    }
    if ("s_-180".equals(arg0.getActionCommand())) {
      rotacion(-180);
    }
    if ("s_-90".equals(arg0.getActionCommand())) {
      rotacion(-90);
    }
    if ("s_270".equals(arg0.getActionCommand())) {
      rotacion(270);
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
      FileNameExtensionFilter tiffFilter = new FileNameExtensionFilter(this.idioma.get("mm_iTif"), FORMATO_FICHERO);
      fc.setFileFilter(tiffFilter);
      fc.setDialogTitle(this.idioma.get("s_abrir"));
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File fichero = fc.getSelectedFile();
        if (fichero.getName().toLowerCase().contains(FORMATO_FICHERO)) {
          seguir = true;
          String nombre = fichero.getName();
          if (this.listaImagenes.size() > 0) {
            boolean repetida = false;
            int i = 0;
            do {
              if (this.listaImagenes.get(i).getNombre().equals(nombre)) {
                repetida = true;
                nombre = nombre.split("\\." + FORMATO_FICHERO)[0] + "_bi." + FORMATO_FICHERO;
              } else {
                i++;
              }
            } while ((!repetida) && (i < this.listaImagenes.size()));
          }
          try {
            BufferedImage bImage = null;
            bImage = construirImagen(fichero);
            VentanaImagen aux;
            aux = new VentanaImagen(this.cantidadImagenes, 
                                    bImage, 
                                    nombre, 
                                    this.debug, 
                                    this,
                                    fichero.getAbsolutePath());
            this.listaImagenes.add(aux);
            this.add(aux);
            this.debug.escribirMensaje("> Se ha cargado la imagen " + fichero.getName());
            this.cantidadImagenes++;
            this.imagenFocus = aux;
            if ((aux.getImagen().getType() == 0) || (aux.getImagen().getType() == BufferedImage.TYPE_INT_RGB)) {
              aux.fijarGris(true);
            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }          
        } else {
          mostrarError(21);
          this.debug.escribirMensaje("> Se ha producido el error " + this.idioma.get("e_solotif"));
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
  public void mostrarError(int idError) {
    JFrame.setDefaultLookAndFeelDecorated(false);
    String aux = null;
    switch (idError) {
      case 21:
        aux = this.idioma.get("e_solotif");
        break;
      case 22:
        aux = this.idioma.get("e_iAbierta");
        break;
      case 23:
        aux = this.idioma.get("e_iGris");
        break;
      case 24:
        aux = this.idioma.get("e_fNumerico");
        break;
      case 25:
        aux = this.idioma.get("e_mImagenes");
      default:
        break;
    }
    JOptionPane.showMessageDialog(this,
        aux,
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
        this.idioma.get("mm_autores") + ": \n   - " + AUTORES[0] + "\n   - " + AUTORES[1] + "\n" + VERSION,
        this.idioma.get("s_acerca"),
        JOptionPane.INFORMATION_MESSAGE);
    JFrame.setDefaultLookAndFeelDecorated(true);
  }
  
  /**
   * M&eacute;todo encargado de duplicar la imagen
   * en foco creando una nueva ventana con la misma
   * imagen
   */
  public void duplicarImagen() {
    if (this.imagenFocus == null) {
      mostrarError(22);      
    } else {
      String[] nombre = this.imagenFocus.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = this.imagenFocus.getRuta().split(this.imagenFocus.getNombre());
      String nuevoNombre = nombre[0] + "_copia." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;
      BufferedImage imagenOriginal = this.imagenFocus.getImagen();
      boolean gris = this.imagenFocus.esGris();
      BufferedImage imagenNueva = new BufferedImage(imagenOriginal.getWidth(), imagenOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);
      for (int i = 0; i < imagenNueva.getWidth(); i++) {
        for (int j = 0; j < imagenNueva.getHeight(); j++) {
          imagenNueva.setRGB(i, j, imagenOriginal.getRGB(i, j));
        }
      }
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imagenNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.add(aux);
      this.debug.escribirMensaje("> Se ha duplicado la imagen en foco");
      this.cantidadImagenes++;
      aux.fijarGris(gris);
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
      FileNameExtensionFilter tiffFilter = new FileNameExtensionFilter(this.idioma.get("mm_iTif"), FORMATO_FICHERO);
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
   */
  private void cambiarImagenGris() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else {
      BufferedImage imagenColor = this.imagenFocus.getImagen();
      BufferedImage imagenGris = new BufferedImage(imagenColor.getWidth(), imagenColor.getHeight(), BufferedImage.TYPE_INT_RGB);
      for (int i = 0; i < imagenGris.getWidth(); i++) {
       for (int j = 0; j < imagenGris.getHeight(); j++) {
         Color auxColor = new Color(imagenColor.getRGB(i, j));
         int r = auxColor.getRed();
         int b = auxColor.getBlue();
         int g = auxColor.getGreen();
         int nPixel = (int) (0.222 * r + 0.707 * g + 0.071 * b);
         int result = nPixel << 16; // Red
         result += nPixel << 8;     // Green
         result += nPixel;          // Blue
         imagenGris.setRGB(i, j, result);
       }
     }
      String[] nombre = this.imagenFocus.getNombre().split("." + FORMATO_FICHERO);      
      String[] ruta = this.imagenFocus.getRuta().split(this.imagenFocus.getNombre());
      String nuevoNombre = nombre[0] + "_gris." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imagenGris, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.add(aux);
      this.debug.escribirMensaje("> Se ha cambiado a escala de grises la imagen en foco");
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
      informacion += "Brillo: " + this.imagenFocus.getBrillo() + "\n";
      informacion += "Contraste: " + this.imagenFocus.getContraste() + "\n";
      JFrame.setDefaultLookAndFeelDecorated(false);
      JOptionPane.showMessageDialog(this,
          informacion,
          this.idioma.get("s_iImagen"),
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
      this.imagenFocus.dibujarHistogramaAbsoluto(this.idioma.get("s_hAbsoluto"));
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
      this.imagenFocus.dibujarHistogramaAcumulativo(this.idioma.get("s_hAcumulativo"));
    }
  }
  
  /**
   *  M&eacute;todo encargado de mostrar
   *  la entrop&iacute;a de la imagen
   *  en foco
   * 
   *  @since  1.0
   *
   */
  private void mostrarEntropia() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()){
      mostrarError(23);
    } else {
      JFrame.setDefaultLookAndFeelDecorated(false);
      JOptionPane.showMessageDialog(this,
          this.idioma.get("s_entropia") + ": " + this.imagenFocus.getEntropia(),
          this.idioma.get("s_entropia"),
          JOptionPane.INFORMATION_MESSAGE);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
  }
  
  /**
   *  M&eacute;todo encargado de abrir el
   *  JFrame en el cual se realiza la operaci&oacute;n
   *  de ajustar el brillo y el contraste de la imagen
   *  en foco
   *  
   *  @since  1.0
   *
   */
  private void ajusteBrilloContraste() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()){
      mostrarError(23);
    } else {
      JFrame.setDefaultLookAndFeelDecorated(false);
      new VentanaBrilloContraste(this.idioma, this);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
  }
  
  /**
   *  M&eacute;todo encargado de abrir el
   *  JFrame en el cual se realiza la operaci&oacute;n
   *  de transformaci&oacute;n lineal a tramos de la imagen
   *  en foco
   *  
   *  @since  1.0
   *
   */
  private void transformacionLinealTramos() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else {
      JFrame.setDefaultLookAndFeelDecorated(false);
      new VentanaTransformacionTrozos(this.idioma, this);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
  } 
  
  /**
   *  M&eacute;todo encargado de realizar la operaci&oacute;n
   *  de ecualizaci&oacute;n del histograma de la imagen
   *  en foco
   *  
   *  @since  1.0
   *
   */
  private void ecualizacionHistograma() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()){
      mostrarError(23);
    } else {
      VentanaImagen iFoc = this.imagenFocus;
      BufferedImage imgFoc = iFoc.getImagen();
      BufferedImage imgNueva = new BufferedImage(imgFoc.getWidth(), imgFoc.getHeight(), BufferedImage.TYPE_INT_RGB);
      final int W = imgFoc.getWidth();
      final int H = imgFoc.getHeight();
      final int M = W * H;
      final int MAX_PIXELS = 256;
      int[] nivelGris = new int[iFoc.getNivelGris().length]; 
      iFoc.dibujarHistogramaAbsoluto(this.idioma.get("s_hAbsoluto") + " " + iFoc.getNombre());
      iFoc.dibujarHistogramaAcumulativo(this.idioma.get("s_hAcumulativo") + " " + iFoc.getNombre());      
      for (int i = 1; i < nivelGris.length; i++) {
        nivelGris[i] = iFoc.getNivelGris()[i];
        if (i > 0) {
          nivelGris[i] = nivelGris[i - 1] + nivelGris[i];
        }
      }
      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          int a = new Color(imgFoc.getRGB(i, j)).getRed();
          int b = nivelGris[a] * (MAX_PIXELS - 1) / M;
          int result = b << 16;
          result += b << 8;     
          result += b;
          imgNueva.setRGB(i, j, result);
        }
      }
      final String FORMATO_FICHERO = "tif";
      String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
      String nuevoNombre = nombre[0] + "_ecualizada." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;      
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imgNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.cantidadImagenes++;
      this.add(aux);
      aux.fijarGris(true);
      this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
      try {
        Thread.sleep(500);
      } catch (Exception e) {
        
      }
      aux.dibujarHistogramaAbsoluto(this.idioma.get("s_hAbsoluto") + " " + aux.getNombre());
      aux.dibujarHistogramaAcumulativo(this.idioma.get("s_hAcumulativo") + " " + aux.getNombre());
    }
    
  }
  
  /**
   *  M&eacute;todo encargado de realizar la operaci&oacute;n
   *  gamma de la imagen en foco
   *  
   *  @since  1.0
   *
   */
  private void operacionGamma() {
    final JFrame fGamma = new JFrame(this.idioma.get("s_gamma"));
    JPanel panelVentana = new JPanel(new BorderLayout());
    JPanel panelBotones = new JPanel(new GridLayout(1, 2));
    JButton bAceptar = new JButton(this.idioma.get("mm_aceptar"));
    JButton bCancelar = new JButton(this.idioma.get("mm_cancelar"));
    final JTextField tfValor = new JTextField();
    fGamma.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    bAceptar.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        try {
        imagenFocus.operacionGamma(Double.parseDouble(tfValor.getText()));
        debug.escribirMensaje("> Se ha mostrado la operación gamma");        
        } catch (NumberFormatException e) {
          mostrarError(24);
        }
        fGamma.dispose();        
      }
      
    });
    
    bCancelar.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        fGamma.dispose();
      }
    });
    panelBotones.add(bAceptar);
    panelBotones.add(bCancelar);
    panelVentana.add(panelBotones, BorderLayout.SOUTH);
    panelVentana.add(tfValor);
    fGamma.setContentPane(panelVentana);
    fGamma.setLocationRelativeTo(null);
    fGamma.setSize(300, 100);
    fGamma.setVisible(true);
  }
  
  /**
   *  M&eacute;todo encargado de abrir el
   *  JFrame en el cual se realiza la operaci&oacute;n
   *  de diferencia de im&aacute;genes y mapa de cambios
   *  
   *  @param mod variable booleana indicando si se calcula la diferencia o el mapa
   *  @since  1.0
   *
   */
  private void diferenciaDeImagenes(boolean mod) {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else if (this.listaImagenes.size() < 2) {
      mostrarError(25);
    } else {
      JFrame.setDefaultLookAndFeelDecorated(false);
      new VentanaDiferenciaImagenes(this.idioma, this).setDiferencia(mod);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
  }
  
  /**
   *  M&eacute;todo encargado de abrir el
   *  JFrame en el cual se realiza la operaci&oacute;n
   *  de especificaci&oacute;n del histograma
   *  
   *  @since  1.0
   *
   */
  private void especificacionHistograma() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else if (this.listaImagenes.size() < 2) {
      mostrarError(25);
    } else {
      JFrame.setDefaultLookAndFeelDecorated(false);
      new VentanaEspecificacionHistograma(this.idioma, this);
      JFrame.setDefaultLookAndFeelDecorated(true);
    }
  }
  
  private void espejoHorizontal() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else {
      VentanaImagen iFoc = this.imagenFocus;
      BufferedImage imgFoc = iFoc.getImagen();
      BufferedImage imgNueva = new BufferedImage(imgFoc.getWidth(), imgFoc.getHeight(), BufferedImage.TYPE_INT_RGB);
      final int W = imgFoc.getWidth();
      final int H = imgFoc.getHeight();
      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          imgNueva.setRGB((W - 1 - i), j, imgFoc.getRGB(i, j));
        }
      }
      final String FORMATO_FICHERO = "tif";
      String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
      String nuevoNombre = nombre[0] + "_espejoHorizontal." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;      
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imgNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.cantidadImagenes++;
      this.add(aux);
      aux.fijarGris(true);
      this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
      
    }
  }
  
  private void espejoVertical() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else {
      VentanaImagen iFoc = this.imagenFocus;
      BufferedImage imgFoc = iFoc.getImagen();
      BufferedImage imgNueva = new BufferedImage(imgFoc.getWidth(), imgFoc.getHeight(), BufferedImage.TYPE_INT_RGB);
      final int W = imgFoc.getWidth();
      final int H = imgFoc.getHeight();
      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          imgNueva.setRGB(i, (H - 1 - j), imgFoc.getRGB(i, j));
        }
      }
      final String FORMATO_FICHERO = "tif";
      String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
      String nuevoNombre = nombre[0] + "_espejoVertical." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;      
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imgNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.cantidadImagenes++;
      this.add(aux);
      aux.fijarGris(true);
      this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
      
    }
  }
  
  private void traspuestaImagen() {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else {
      VentanaImagen iFoc = this.imagenFocus;
      BufferedImage imgFoc = iFoc.getImagen();
      BufferedImage imgNueva = new BufferedImage(imgFoc.getHeight(), imgFoc.getWidth(), BufferedImage.TYPE_INT_RGB);
      final int W = imgFoc.getWidth();
      final int H = imgFoc.getHeight();
      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          imgNueva.setRGB(j, i, imgFoc.getRGB(i, j));
        }
      }
      final String FORMATO_FICHERO = "tif";
      String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
      String nuevoNombre = nombre[0] + "_traspuesta." + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;      
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imgNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.cantidadImagenes++;
      this.add(aux);
      aux.fijarGris(true);
      this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
      
    }
  }
  
  private void rotacion(int angulos) {
    switch (angulos) {
      case 90:      
        rotacion90("_90.");
        break;
      case -270:      
        rotacion90("_-270.");
        break;
      case 180:      
        rotacion180("_180.");
        break;
      case -180:      
        rotacion180("_-180.");
        break;
      case -90:      
        rotacion270("_-90.");
        break;
      case 270:      
        rotacion270("_270.");
        break;
    }
  }
  
  private void rotacion90(String grado) {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else {
      VentanaImagen iFoc = this.imagenFocus;
      BufferedImage imgFoc = iFoc.getImagen();
      BufferedImage imgNueva = new BufferedImage(imgFoc.getHeight(), imgFoc.getWidth(), BufferedImage.TYPE_INT_RGB);
      final int W = imgFoc.getWidth();
      final int H = imgFoc.getHeight();
      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          imgNueva.setRGB(j, W - 1 - i, imgFoc.getRGB(i, j));
        }
      }
      final String FORMATO_FICHERO = "tif";
      String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
      String nuevoNombre = nombre[0] + grado + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;      
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imgNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.cantidadImagenes++;
      this.add(aux);
      aux.fijarGris(true);
      this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
      
    }
  }
  
  private void rotacion180(String grado) {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else {
      VentanaImagen iFoc = this.imagenFocus;
      BufferedImage imgFoc = iFoc.getImagen();
      BufferedImage imgNueva = new BufferedImage(imgFoc.getWidth(), imgFoc.getHeight(), BufferedImage.TYPE_INT_RGB);
      final int W = imgFoc.getWidth();
      final int H = imgFoc.getHeight();
      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          imgNueva.setRGB(W - 1 - i, H - 1 - j, imgFoc.getRGB(i, j));
        }
      }
      final String FORMATO_FICHERO = "tif";
      String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
      String nuevoNombre = nombre[0] + grado + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;      
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imgNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.cantidadImagenes++;
      this.add(aux);
      aux.fijarGris(true);
      this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
      
    }
  }
  
  private void rotacion270(String grado) {
    if (this.imagenFocus == null) {
      mostrarError(22);
    } else if (!this.imagenFocus.esGris()) {
      mostrarError(23);
    } else {
      VentanaImagen iFoc = this.imagenFocus;
      BufferedImage imgFoc = iFoc.getImagen();
      BufferedImage imgNueva = new BufferedImage(imgFoc.getHeight(), imgFoc.getWidth(), BufferedImage.TYPE_INT_RGB);
      final int W = imgFoc.getWidth();
      final int H = imgFoc.getHeight();
      for (int i = 0; i < W; i++) {
        for (int j = 0; j < H; j++) {
          imgNueva.setRGB(H - 1 - j, i, imgFoc.getRGB(i, j));
        }
      }
      final String FORMATO_FICHERO = "tif";
      String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
      String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
      String nuevoNombre = nombre[0] + grado + FORMATO_FICHERO; 
      String nuevaRuta = ruta[0] + nuevoNombre;      
      VentanaImagen aux = new VentanaImagen(this.cantidadImagenes, 
                                            imgNueva, 
                                            nuevoNombre, 
                                            this.debug, 
                                            this,
                                            nuevaRuta);   
      this.listaImagenes.add(aux);
      this.cantidadImagenes++;
      this.add(aux);
      aux.fijarGris(true);
      this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
      
    }
  }  
  
  /**
   *  M&eacute;todo encargado de borrar una
   *  imagen de la lista de im&aacute;genes
   *  
   *  @since  1.0
   *
   */
  public void borrarVentana(int i) {
    this.listaImagenes.remove(i);
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
   *  M&eacute;todo getter para obtener
   *  la imagen en foco en el momento que
   *  se usa
   *  
   *  @return imagenFocus la imagen en foco
   *  @since  1.0
   *
   */
  public VentanaImagen getImgFoco() {
    return (this.imagenFocus);
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
  
  /**
   *  M&eacute;todo getter para el atributo
   *  colorElementos
   *  
   *  @return colorElementos
   *  @since  1.0
   *
   */
  public Color getElemColor() {
    return this.colorElementos;
  }
  
  /**
   *  M&eacute;todo setter para el atributo
   *  colorElementos
   *  
   *  @param nColor
   *  @since  1.0
   *
   */
  public void setElemColor(Color nColor) {
    this.colorElementos = nColor;
  }
  
  /**
   *  M&eacute;todo getter para el atributo
   *  listaImagenes
   *  
   *  @return listaImagen
   *  @since  1.0
   *
   */
  public ArrayList<VentanaImagen> getListaImagenes() {
    return (this.listaImagenes);
  }
  
  /**
   *  M&eacute;todo getter para el atributo
   *  debug
   *  
   *  @return debug
   *  @since  1.0
   *
   */
  public VentanaDebug getVentanaDebug() {
    return (this.debug);
  }

}
