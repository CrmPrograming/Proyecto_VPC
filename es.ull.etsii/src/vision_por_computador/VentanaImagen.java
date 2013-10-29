package vision_por_computador;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.jfree.ui.RefineryUtilities;

@SuppressWarnings("serial")
public class VentanaImagen extends JInternalFrame {
  
  /**
   * Imagen a mostrar almacenada en memoria
   */
  private BufferedImage bufferImagen;
  /**
   * Panel donde se muestra la imagen
   */
  private PanelImagen panelImagen;
  /**
   * Identificador asociado a la ventana
   */
  private int id;
  /**
   * Variable a modo de enlace a la VentanaDebug
   * propia de la clase PanelPrincipal
   * 
   * @see PanelPrincipal
   * @see VentanaDebug
   */
  private VentanaDebug debug;
  /**
   * ArrayList con todas las im&aacute;genes abiertas
   * en un momento dado obtenido de la clase PanelPrincipal
   * 
   * @see PanelPrincipal
   */
  private ArrayList<VentanaImagen> listaImagens;
  /**
   * Cadena con el nombre de la ventana
   */
  private String nombre;
  /**
   * Atributo con instanciaci&oacute;n del PanelPrincipal
   * 
   * @see PanelPrincipal
   */
  private PanelPrincipal panelPrincipal;
  /**
   * Cadena con la ruta de la imagen guardada en memoria
   */
  private String ruta;
  /**
   * Variable booleana la cual indica si la imagen
   * est&aacute; en escala de grices o no
   */
  private boolean escalaGris;
  /**
   * Valor m&iacute;nimo de pixels en nivel de gris
   */
  private int valorMin;
  /**
   * Valor m&aacute;ximo de pixels en nivel de gris
   */
  private int valorMax;
  /**
   * Array con n&uacute;mero de pixels correspondiente
   * a cada posici&oacute;n del array
   */
  private int[] nivelGris; 
  
  /**
   * Instancia un nuevo objeto
   * de tipo VentanaImagen.
   *
   * @param idVentana Identificador de la ventana
   * @param bImage Imagen a mostrar en memoria
   * @param nombreImagen Nombre de la imagen
   * @param debg Instanciaci&oacute;n de la VentanaDebug
   * @param listaImagenes Lista de im&aacute;genes abiertas actualmente
   * @param pPrincipal Instanciaci&oacute;n del panel principal
   * @param path Ruta de la imagen
   */
  public VentanaImagen(int idVentana, BufferedImage bImage, String nombreImagen, VentanaDebug debg, ArrayList<VentanaImagen> listaImagenes, PanelPrincipal pPrincipal, String path) {
    super("Imagen " + idVentana + ": " + nombreImagen,
        false, //resizable
        true, //closable
        false, //maximizable
        true);//iconifiable
    this.id = idVentana;
    this.debug = debg;
    this.bufferImagen = bImage;  
    this.listaImagens = listaImagenes;
    this.nombre = nombreImagen;
    this.ruta = path;
    this.escalaGris = false;
    this.valorMin = 0;
    this.valorMax = 0;
    setSize(this.bufferImagen.getWidth(), this.bufferImagen.getHeight());
    System.out.println("Tam: " + this.bufferImagen.getWidth() + ", " +this.bufferImagen.getHeight());
    System.out.println(this.getSize());
    this.panelPrincipal = pPrincipal;
    this.panelImagen = new PanelImagen();
    setContentPane(this.panelImagen); 
    setVisible(true);
    this.addInternalFrameListener(new InternalFrameAdapter() {      
      
      /**
       * Sobreescritura del m&eacute;todo internalFrameClosing 
       * para eliminar la imagen de la lista de im&aacute;genes
       * abiertas y quitarle el foco
       *
       * @param e Evento disparado
       */
      @Override
      public void internalFrameClosing(InternalFrameEvent e) {
        listaImagens.remove(this);
        debug.escribirMensaje("> Cerrada la imagen " + nombre);
        if (listaImagens.size() == 0) {
          panelPrincipal.setFocus(null);
        }
      }
      
      /**
       * Sobreescritura del m&eacute;todo internalFrameActivated
       * para ponerle el foco a la imagen
       * 
       * @param e Evento disparado
       */
      public void internalFrameActivated(InternalFrameEvent e) {
        panelPrincipal.setFocus((VentanaImagen) e.getSource());
        debug.escribirMensaje("> Foco en imagen " + nombre);
      }
      
    });
  }  

  private class PanelImagen extends JPanel {
    
    /**
     * Variable booleana para comprobar si se dibuja o no
     * el rect&aacute;ngulo
     */
    private boolean marcado = false;
    /**
     * Posici&oacute;n X inicial desde donde se dibuja
     * el rect&aacute;ngulo 
     */
    private int posX = 0;
    /**
     * Posici&oacute;n Y inicial desde donde se dibuja
     * el rect&aacute;ngulo 
     */
    private int posY = 0;
    /**
     * Posici&oacute;n X actual del rat&oacute;n hasta donde se dibuja
     * el rect&aacute;ngulo
     */
    private int posXActual = 0;
    /**
     * Posici&oacute;n Y actual del rat&oacute;n hasta donde se dibuja
     * el rect&aacute;ngulo
     */
    private int posYActual = 0;
    /**
     * Temporizador encargado de dibujar el rect&aacute;ngulo en 
     * funci&oacute;n del tiempo definido en la constante DELAY
     * 
     */
    private Timer temporizador;
    /**
     * Constante correspondiente al retraso para el timer 
     */
    private final int DELAY = 100;
    /**
     * Panel sobre el cual se dibuja la imagen
     */
    private JPanel panelG;
    
    /**
     * Instancia un nuevo objeto
     * de tipo PanelImagen.
     */
    public PanelImagen() {
      super(new FlowLayout());
      this.panelG = this;
      this.temporizador = new Timer(0, new TimerListener());
      this.temporizador.setDelay(DELAY);
      this.temporizador.start();      
      this.addMouseListener(new MouseAdapter() {
        
        /**
         * Sobreescritura del m&eacute;todo "mouseClicked"
         * a fin de dibujar el rect&aacute;ngulo desde la
         * posici&oacute;n marcada por primera vez hasta la
         * posici&oacute;n actual del rat&oacute;n
         * 
         * @param e Evento disparado
         */
        @Override
        public void mouseClicked(MouseEvent e) {
          if (!marcado) {
            marcado = true;
            posX = e.getX();
            posY = e.getY();
          } else {
            marcado = false;
            if (escalaGris) {
              BufferedImage subImagen = null;
              int ancho = posXActual - posX;
              int alto = posYActual - posY;   
              System.out.println("Clipped: " + ancho + ", " + alto);
              subImagen = bufferImagen.getSubimage(posX, posY, ancho, alto);
              String[] nombre = getNombre().split("." + "tif");
              String[] ruta = getRuta().split(getNombre());
              String nuevoNombre = nombre[0] + "_subimagen." + "tif"; 
              String nuevaRuta = ruta[0] + nuevoNombre;
              VentanaImagen aux = new VentanaImagen(panelPrincipal.getCantidadImagenes(), 
                                                  subImagen, 
                                                  nuevoNombre, 
                                                  debug, 
                                                  listaImagens, 
                                                  panelPrincipal,
                                                  nuevaRuta);   
              listaImagens.add(aux);
              panelPrincipal.add(aux);
              debug.escribirMensaje("> Se ha cambiado a escala de grices la imagen en foco");
              panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() + 1);
              aux.fijarGris(true);
              panelPrincipal.setFocus(aux);
            }
          }
        }
        
      });
      this.addMouseMotionListener(new MouseMotionAdapter() {

        /**
         * Sobreescritura del m&eacute;todo "mouseMoved"
         * para actualizar la posiciones actuales del rat&oacute;n
         * 
         * @param arg0 Evento disparado
         */
        @Override
        public void mouseMoved(MouseEvent arg0) {
          posXActual = (int) arg0.getPoint().getX();
          posYActual = (int) arg0.getPoint().getY();
        }
        
      });
    }
    
    /**
     * Sobreescritura del m&eacute;todo "paintComponent"
     * para dibujar tanto la imagen como el rect&aacute;ngulo
     * de selecci&oacute;n y la informaci&oacute;n
     *
     * @param g Componente gr&aacute;fica
     */
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      dibujarImagen(g);
      dibujarRectangulo(g);
      dibujarInformacion(g);
    }
    
    /**
     * M&eacute;todo encargado de dibujar la imagen
     * en el panel
     *
     * @param g Componente gr&aacute;fica
     */
    private void dibujarImagen(Graphics g) {
      g.drawImage(bufferImagen, 0, 0, null);
    }
    
    /**
     * M&eacute;todo encargado de dibujar el rect&aacute;gulo
     * de selecci&oacute;n sobre el panel
     *
     * @param g Componente gr&aacute;fica
     */
    private void dibujarRectangulo(Graphics g) {
      if (this.marcado) {
        Color aux = g.getColor();
        g.setColor(panelPrincipal.getElemColor());
        g.drawRect(this.posX, this.posY, this.posXActual - this.posX, this.posYActual - this.posY);
        g.setColor(aux);
      }
    }
    
    /**
     * M&eacute;todo encargado de mostrar la informaci&oacute;n
     * asociada al pixel sobre el cual se encuentre el cursor
     *
     * @param g Componente gr&aacute;fica
     */
    private void dibujarInformacion(Graphics g) {
      Color aux = g.getColor();
      Font fm = g.getFont();
      final int TAM_FUENTE = 15;
      g.setFont(new Font("Coordenada", Font.BOLD, TAM_FUENTE));
      int x = 0;
      int y = TAM_FUENTE;
      Color color = new Color(bufferImagen.getRGB(this.posXActual, this.posYActual));
      int red = color.getRed();
      int green = color.getGreen();
      int blue = color.getBlue();
      g.setColor(panelPrincipal.getElemColor());
      String coordenadas = "(" + this.posXActual + ", " + this.posYActual + ", " 
                            + "R: " + red + ", G: " + green + ", B: " + blue + ")";
      if (this.posXActual < this.getWidth() / 2) {
        x = this.getWidth() - 250;
      }
      if (this.posYActual < this.getHeight() / 2) {
        y = this.getHeight();
      }
      g.drawString(coordenadas, x, y);
      g.setColor(aux);
      g.setFont(fm);
    }
    
    private class TimerListener implements ActionListener {

      /**
       * Sobreescritura del m&eacute;todo "actionPerformed" 
       * para redibujar la imagen y dem&aacute;s elementos
       * en el panel cuando se dispara el Timer
       *
       * @param arg0 Evento disparado
       */
      @Override
      public void actionPerformed(ActionEvent arg0) {
        panelG.repaint();
      }
      
    }
    
  }
  
  /**
   * M&eacute;todo encargado de calcular los niveles
   * de gris de la imagen
   * 
   * <p><b>Anotaciones</b></p>
   * Implementar m&eacute;todo con hilos
   */
  @Anotaciones(desc = "Implementar método con hilos")
  private void calcularNiveles() {
    this.nivelGris = new int[256];
    this.valorMin = Integer.MAX_VALUE;
    this.valorMax = Integer.MIN_VALUE;
    Color color = null;
    for (int i = 0; i < 256; i++) {
      this.nivelGris[i] = 0;
    }
    for (int i = 0; i < this.bufferImagen.getWidth(); i++) {
      for (int j = 0; j < this.bufferImagen.getHeight(); j++) {
        color = new Color(this.bufferImagen.getRGB(i, j));
        this.nivelGris[color.getRed()]++;
        if (color.getRed() > this.valorMax) {
          this.valorMax = color.getRed(); 
        }
        if (color.getRed() < this.valorMin) {
          this.valorMin = color.getRed();
        }
      }
    }
  }
  
  /**
   * M&eacute;todo encargado de mostrar el histograma
   * absoluto
   *
   * @param title Cadena con el t&iacute;tulo de la ventana
   */
  public void dibujarHistogramaAbsoluto(String title) {
    HistogramaAbsoluto histo = new HistogramaAbsoluto(title, this.nivelGris);    
    histo.pack();
    RefineryUtilities.centerFrameOnScreen(histo);
    histo.setVisible(true);
  }
  
  /**
   * M&eacute;todo encargado de mostrar el histograma
   * acumulativo
   *
   * @param title Cadena con el t&iacute;tulo de la ventana
   */
  public void dibujarHistogramaAcumulativo(String title) {
    HistogramaAcumulativo histo = new HistogramaAcumulativo(title, this.nivelGris);    
    histo.pack();
    RefineryUtilities.centerFrameOnScreen(histo);
    histo.setVisible(true);
  }
  
  /**
   * M&eacute;todo setter para cambiar 
   * el atributo id
   *
   * @param idVentana Valor nuevo de id
   */
  public void setID(int idVentana) {
    this.id = idVentana;
  }
  
  /**
   * M&eacute;todo Getter para retornar el 
   * valor del id
   *
   * @return id
   */
  public int getID() {
    return this.id;
  }
  
  /**
   * M&eacute;todo Getter para
   * retornar el nombre de la imagen
   *
   * @return nombre
   */
  public String getNombre() {
    return this.nombre;
  }
  
  /**
   * M&eacute;todo Getter para retornar
   * la imagen en memoria
   *
   * @return imagen
   */
  public BufferedImage getImagen() {
    return this.bufferImagen;
  }
  
  /**
   * M&eacute;todo Getter para retornar
   * la ruta de la imagen
   *
   * @return ruta
   */
  public String getRuta() {
    return this.ruta;
  }
  
  /**
   * M&eacute;todo encargado de indicar si la imagen
   * est&aacute; en escala de gris o no
   *
   * @return true, si est&aacute; en escala de gris
   */
  public boolean esGris() {
    return this.escalaGris;
  }
  
  /**
   * M&eacute;todo encargado de 
   * establecer si la imagen est&aacute; en escala
   * de gris o no
   *
   * @param gris Valor booleano
   */
  public void fijarGris(boolean gris) {
    this.escalaGris = gris;
    if (gris) {
      calcularNiveles();
    }
  }

  /**
   * M&eacute;todo Getter para retornar
   * el valor m&iacute;nimo
   *
   * @return valorMin
   */
  public int getValorMin() {
    return (valorMin);
  }

  /**
   * M&eacute;todo Getter para retornar
   * el valor m&aacute;ximo
   *
   * @return valorMax
   */
  public int getValorMax() {
    return (valorMax);
  } 

}
