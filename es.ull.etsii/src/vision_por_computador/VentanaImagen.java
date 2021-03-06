package vision_por_computador;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.jfree.ui.RefineryUtilities;

@SuppressWarnings("serial")
public class VentanaImagen extends JInternalFrame implements Runnable {
  
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
* est&aacute; en escala de grises o no
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
* Valor calculado de la entropia
*/
  private double entropia = 0.0d;
  
  private double brillo = 0.0d;
  
  private double contraste = 0.0d;
  
  private VentanaImagen copiaStatica;
  
  private double anguloGirado = 0d;
  
  private BufferedImage imgSinGiro = null;
  /**
* Instancia un nuevo objeto
* de tipo VentanaImagen.
*
* @param idVentana Identificador de la ventana
* @param bImage Imagen a mostrar en memoria
* @param nombreImagen Nombre de la imagen
* @param debg Instanciaci&oacute;n de la VentanaDebug
* @param pPrincipal Instanciaci&oacute;n del panel principal
* @param path Ruta de la imagen
*/
  public VentanaImagen(int idVentana, BufferedImage bImage, String nombreImagen, VentanaDebug debg, PanelPrincipal pPrincipal, String path) {
    super("Imagen " + idVentana + ": " + nombreImagen,
        false, //resizable
        true, //closable
        false, //maximizable
        true);//iconifiable
    this.id = idVentana;
    this.debug = debg;
    this.bufferImagen = bImage;
    this.imgSinGiro = bImage;
    this.nombre = nombreImagen;
    this.ruta = path;
    this.escalaGris = false;
    this.valorMin = 0;
    this.valorMax = 0;
    setSize(this.bufferImagen.getWidth() + 10, this.bufferImagen.getHeight() + 33);
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
        ArrayList<VentanaImagen> lista = panelPrincipal.getListaImagenes();
        boolean encontrado = false;
        int i = 0;
        do {
          if (lista.get(i) == copiaStatica) {
            encontrado = true;
          } else {
            i++;
          }
        } while (!encontrado);
        panelPrincipal.borrarVentana(i);
        panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() - 1);
        debug.escribirMensaje("> Cerrada la imagen " + nombre);
        panelPrincipal.setFocus(null);
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
    this.copiaStatica = this;
    Color aux = new Color(this.bufferImagen.getRGB(0, 0));
    if ((aux.getRed() == aux.getBlue()) && (aux.getRed() == aux.getGreen())) {
      this.fijarGris(true);
    }
  }
  
  public void run() {
    try {
      Thread.sleep(100);
    } catch (Exception e) {
      
    }
    calcularNiveles();
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
              subImagen = bufferImagen.getSubimage(posX, posY, ancho, alto);
              String[] nombre = getNombre().split("." + "tif");
              String[] ruta = getRuta().split(getNombre());
              String nuevoNombre = nombre[0] + "_subimagen." + "tif";
              String nuevaRuta = ruta[0] + nuevoNombre;
              VentanaImagen aux = new VentanaImagen(panelPrincipal.getCantidadImagenes(),
                                                  subImagen,
                                                  nuevoNombre,
                                                  debug,
                                                  panelPrincipal,
                                                  nuevaRuta);
              panelPrincipal.getListaImagenes().add(aux);
              panelPrincipal.add(aux);
              debug.escribirMensaje("> Se ha cambiado a escala de grises la imagen en foco");
              panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() + 1);
              aux.fijarGris(true);
              aux.setAnguloGirado(getAnguloGirado());
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
*/
  private synchronized void calcularNiveles() {
    this.nivelGris = new int[256];
    this.valorMin = Integer.MAX_VALUE;
    this.valorMax = Integer.MIN_VALUE;
    Color color = null;
    int total = 0;
    int sumatorio = 0;
    double des = 0.0d;
    double sumDesviacion = 0.0d;
    for (int i = 0; i < 256; i++) {
      this.nivelGris[i] = 0;
    }
    total = this.bufferImagen.getWidth() * this.bufferImagen.getHeight();
    for (int i = 0; i < this.bufferImagen.getWidth(); i++) {
      for (int j = 0; j < this.bufferImagen.getHeight(); j++) {
        color = new Color(this.bufferImagen.getRGB(i, j));
        if ((color.getRed() == color.getBlue()) && (color.getRed() == color.getGreen())) {
          this.nivelGris[color.getRed()]++;
          sumatorio += color.getRed();
          if (color.getRed() > this.valorMax) {
            this.valorMax = color.getRed();
          }
          if (color.getRed() < this.valorMin) {
            this.valorMin = color.getRed();
          }
        }
      }
    }
    // Entropía
    this.entropia = 0;
    for (int i = 0; i < this.nivelGris.length; i++) {
      double prob = (double) this.nivelGris[i] / total;
      if (prob > 0.0d)
        this.entropia -= prob * Math.log(prob);
    }
    this.entropia = (this.entropia / Math.log(2.0d));
    // Brillo y Contraste
    this.brillo = sumatorio / total;
    for (int i = 0; i < this.bufferImagen.getWidth(); i++) {
      for (int j = 0; j < this.bufferImagen.getHeight(); j++) {
        color = new Color(this.bufferImagen.getRGB(i, j));
        if ((color.getRed() == color.getBlue()) && (color.getRed() == color.getGreen())) {
          des = color.getRed() - this.brillo;
          sumDesviacion += Math.pow(des, 2);
        }
      }
    }
    this.contraste = Math.sqrt(sumDesviacion / total);
    
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
  
  public void ajustarPixels(int[] nPixels) {
    final int ANCHO = this.bufferImagen.getWidth();
    final int ALTO = this.bufferImagen.getHeight();
    for (int i = 0; i < ANCHO; i++) {
      for (int j = 0; j < ALTO; j++) {
        int pixelActual = new Color(this.bufferImagen.getRGB(i, j)).getRed();
        int nuevoPixel = nPixels[pixelActual];
        int result = nuevoPixel << 16;
        result += nuevoPixel << 8;
        result += nuevoPixel;
        this.bufferImagen.setRGB(i, j, result);
      }
    }
    new Thread(this).start();
  }
  
  public void operacionGamma(double gamma) {
    final int K = 256;
    int aMax = K - 1;
    int[] Fgc = new int[K];
    
    for (int a = 0; a < K; a++) {
       double aa = (double) a / aMax;
       double bb = Math.pow(aa,gamma);
       int b = (int) Math.round(bb * aMax);
       Fgc[a] = b;
     }
    
    this.panelPrincipal.duplicarImagen();
    this.panelPrincipal.getImgFoco().ajustarPixels(Fgc);
    
  }
  
  /**
* @param
* @return
* @since 1.0
*
*/

  public void escalarVecino(final int N_ANCHO, final int N_ALTO) {
    VentanaImagen iFoc = this.panelPrincipal.getImgFoco();
    BufferedImage imgFoc = iFoc.getImagen();
    BufferedImage imgNueva = new BufferedImage(N_ANCHO, N_ALTO, BufferedImage.TYPE_INT_RGB);
    final double W = imgFoc.getWidth();
    final double H = imgFoc.getHeight();
    final double INDICE_X = (double) N_ANCHO / (double) W;
    final double INDICE_Y = (double) N_ALTO / (double) H;
    for (int i = 0; i < N_ANCHO; i++) {
      for (int j = 0; j < N_ALTO; j++) {
        double x = i / INDICE_X;
        double y = j / INDICE_Y;
        double w = Math.round(x);
        double v = Math.round(y);
        double X = Math.floor(x);
        double Y = Math.floor(y);       
        if (w == W) {
          w = imgFoc.getWidth() - 1;
        }
        if (v == H) {
          v = imgFoc.getHeight() - 1;
        }
        
        final Point A = new Point((int) X, (int) v);
        final Point B = new Point((int) w, (int) v);
        final Point C = new Point((int) X, (int) Y);
        final Point D = new Point((int) w, (int) Y);
        
        imgNueva.setRGB(i, j, pixelVecino(x, y, imgFoc, A, B, C, D));
      }
    }
    final String FORMATO_FICHERO = "tif";
    String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
    String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
    String nuevoNombre = nombre[0] + "_escalada." + FORMATO_FICHERO;
    String nuevaRuta = ruta[0] + nuevoNombre;
    VentanaImagen aux = new VentanaImagen(this.panelPrincipal.getCantidadImagenes(),
                                          imgNueva,
                                          nuevoNombre,
                                          this.debug,
                                          this.panelPrincipal,
                                          nuevaRuta);
    this.panelPrincipal.getListaImagenes().add(aux);
    this.panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() + 1);
    this.panelPrincipal.add(aux);
    aux.fijarGris(true);
    aux.setAnguloGirado(iFoc.getAnguloGirado());
    this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
  }
  
  private int pixelVecino(double x, double y, BufferedImage imagen, final Point ... PUNTOS) {
    int pixel = 0;
    double distancia = Double.MAX_VALUE;
    double distanciaTemporal = 0d;
    for (Point punto: PUNTOS) {
      distanciaTemporal = Math.sqrt(Math.pow(punto.getX() - x, 2) + Math.pow(punto.getY() - y, 2));
      if (distanciaTemporal < distancia) {
        distancia = distanciaTemporal;
        pixel = imagen.getRGB((int) punto.getX(), (int) punto.getY());
      }
    }
    return (pixel);
  }
  
  public void escalarBilineal(final int N_ANCHO, final int N_ALTO) {
    VentanaImagen iFoc = this.panelPrincipal.getImgFoco();
    BufferedImage imgFoc = iFoc.getImagen();
    BufferedImage imgNueva = new BufferedImage(N_ANCHO, N_ALTO, BufferedImage.TYPE_INT_RGB);
    final int W = imgFoc.getWidth();
    final int H = imgFoc.getHeight();
    final float INDICE_X = (float) N_ANCHO / W;
    final float INDICE_Y = (float) N_ALTO / H;
    for (int i = 0; i < N_ANCHO; i++) {
      for (int j = 0; j < N_ALTO; j++) {
        float x = i / INDICE_X;
        float y = j / INDICE_Y;
        int w = (int) Math.ceil(x);
        int v = (int) Math.ceil(y);
        int X = (int) Math.floor(x);
        int Y = (int) Math.floor(y);       
        if (w == W) {
          w--;
        }
        if (v == H) {
          v--;
        }
        int A = new Color(imgFoc.getRGB(X, v)).getRed();
        int B = new Color(imgFoc.getRGB(w, v)).getRed();
        int C = new Color(imgFoc.getRGB(X, Y)).getRed();
        int D = new Color(imgFoc.getRGB(w, Y)).getRed();
        float p = x - X;
        float q = y - Y;
        int P = (int) (C + (D - C) * p + (A - C) * q + (B + C - A - D) * p * q);
        int colorRGB = (P << 16 | P << 8 | P);
        imgNueva.setRGB(i,  j,  colorRGB);
      }
    }
    final String FORMATO_FICHERO = "tif";
    String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
    String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
    String nuevoNombre = nombre[0] + "_escalada." + FORMATO_FICHERO;
    String nuevaRuta = ruta[0] + nuevoNombre;
    VentanaImagen aux = new VentanaImagen(this.panelPrincipal.getCantidadImagenes(),
                                          imgNueva,
                                          nuevoNombre,
                                          this.debug,
                                          this.panelPrincipal,
                                          nuevaRuta);
    this.panelPrincipal.getListaImagenes().add(aux);
    this.panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() + 1);
    this.panelPrincipal.add(aux);
    aux.fijarGris(true);
    aux.setAnguloGirado(iFoc.getAnguloGirado());
    this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
  } 
  
  public void rotarPintar(final double ANGULO) {
    VentanaImagen iFoc = this.panelPrincipal.getImgFoco();
    BufferedImage imgFoc = iFoc.getImagenOriginal();
    final double W = imgFoc.getWidth();
    final double H = imgFoc.getHeight();
    final double ANGULO_RADIANES = Math.toRadians(ANGULO + iFoc.getAnguloGirado());
    ArrayList<Matriz> extremos = calcularExtremos(ANGULO_RADIANES, W, H);
    
    
    Matriz tDirecta = new Matriz(new float[][] {{(float) Math.cos(ANGULO_RADIANES), (float) Math.sin(ANGULO_RADIANES)},
                                                { (float) - Math.sin(ANGULO_RADIANES), (float) Math.cos(ANGULO_RADIANES)}});
    final int N_ANCHO = calcularMaximo(extremos, 0);
    final int N_ALTO = calcularMaximo(extremos, 1);
    
    BufferedImage imgNueva = new BufferedImage(N_ANCHO, N_ALTO, BufferedImage.TYPE_INT_RGB);
    
    int minY = Integer.MAX_VALUE;
    int minX = Integer.MAX_VALUE;
        
    for (Matriz T: extremos) {
      if (minY > T.getMatriz()[1][0]) {
        minY = (int) T.getMatriz()[1][0];
      }
      if (minX > T.getMatriz()[0][0]) {
        minX = (int) T.getMatriz()[0][0];
      }
    }
    
    for (int i = 0; i < N_ANCHO; i++)
      for (int j = 0; j < N_ALTO; j++)
        imgNueva.setRGB(i, j, Color.YELLOW.getRGB());
     
    for (int i = 0; i < W; i++)
      for (int j = 0; j < H; j++) {
        final Matriz aux = new Matriz(new float[][] {{i}, {j}});
        final Matriz p = tDirecta.producto(aux);
        final Point pixel = new Point((int) p.getMatriz()[0][0], (int) p.getMatriz()[1][0]);
        if (((pixel.getX() - minX) < N_ANCHO) && ((pixel.getY() - minY) < N_ALTO))
          imgNueva.setRGB((int) pixel.getX() - minX, (int) pixel.getY() - minY, imgFoc.getRGB(i, j));        
      }
    
    final String FORMATO_FICHERO = "tif";
    String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
    String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
    String nuevoNombre = nombre[0] + "_rotada." + FORMATO_FICHERO;
    String nuevaRuta = ruta[0] + nuevoNombre;
    VentanaImagen aux = new VentanaImagen(this.panelPrincipal.getCantidadImagenes(),
                                          imgNueva,
                                          nuevoNombre,
                                          this.debug,
                                          this.panelPrincipal,
                                          nuevaRuta);
    this.panelPrincipal.getListaImagenes().add(aux);
    this.panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() + 1);
    this.panelPrincipal.add(aux);
    aux.fijarGris(true);
    aux.setAnguloGirado(ANGULO + iFoc.getAnguloGirado());
    aux.setImagenOriginal(iFoc.getImagenOriginal());
    this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
  }
  
  public void rotacionVecinos(final double ANGULO) {
    VentanaImagen iFoc = this.panelPrincipal.getImgFoco();
    BufferedImage imgFoc = iFoc.getImagenOriginal();
    final double W = imgFoc.getWidth();
    final double H = imgFoc.getHeight();
    final double ANGULO_RADIANES = Math.toRadians(ANGULO + iFoc.getAnguloGirado());
    ArrayList<Matriz> extremos = calcularExtremos(ANGULO_RADIANES, W, H);
    
    Matriz tInversa = new Matriz(new float[][] {{(float) Math.cos(ANGULO_RADIANES), (float) - Math.sin(ANGULO_RADIANES)},
                                             { (float) Math.sin(ANGULO_RADIANES), (float) Math.cos(ANGULO_RADIANES)}});
    final int N_ANCHO = calcularMaximo(extremos, 0);
    final int N_ALTO = calcularMaximo(extremos, 1);
    
    BufferedImage imgNueva = new BufferedImage(N_ANCHO, N_ALTO, BufferedImage.TYPE_INT_RGB);
    
    Matriz oOrigen = new Matriz(new float[][] {{0f}, {0f}});
    oOrigen = tInversa.producto(oOrigen);
    final Vector OP_OR = new Vector(new Punto(oOrigen.getMatriz()[0][0], oOrigen.getMatriz()[1][0]), new Punto(0, 0));
    
    int minY = Integer.MAX_VALUE;
    int minX = Integer.MAX_VALUE;
    
    for (Matriz T: extremos) {
      if (minY > T.getMatriz()[1][0]) {
        minY = (int) T.getMatriz()[1][0];
      }
      if (minX > T.getMatriz()[0][0]) {
        minX = (int) T.getMatriz()[0][0];
      }
    }
    
    for (int i = 0; i < N_ANCHO; i++)
      for (int j = 0; j < N_ALTO; j++) {
        final Matriz aux = new Matriz(new float[][] {{i + minX}, {j + minY}});
        final Matriz p = tInversa.producto(aux);
        final Vector OP_PP = new Vector(new Punto(oOrigen.getMatriz()[0][0], oOrigen.getMatriz()[1][0]), new Punto(p.getMatriz()[0][0], p.getMatriz()[1][0]));
        final Vector OR_PP = OP_PP.restaVectores(OP_OR);
        Punto pixel = OR_PP.getDestino();
        if ((pixel.getA() >= W) || (pixel.getA() < 0)
          || (pixel.getB() >= H) || (pixel.getB() < 0)) {
          imgNueva.setRGB(i, j, Color.YELLOW.getRGB());
        } else {
          double x = pixel.getA();
          double y = pixel.getB();
          double w = Math.round(x);
          double v = Math.round(y);
          double X = Math.floor(x);
          double Y = Math.floor(y);       
          if (w == W) {
            w = imgFoc.getWidth() - 1;
          }
          if (v == H) {
            v = imgFoc.getHeight() - 1;
          }
          
          final Point A = new Point((int) X, (int) v);
          final Point B = new Point((int) w, (int) v);
          final Point C = new Point((int) X, (int) Y);
          final Point D = new Point((int) w, (int) Y);
          
          imgNueva.setRGB(i, j, pixelVecino(x, y, imgFoc, A, B, C, D));
        }
      }
    
    final String FORMATO_FICHERO = "tif";
    String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
    String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
    String nuevoNombre = nombre[0] + "_rotada." + FORMATO_FICHERO;
    String nuevaRuta = ruta[0] + nuevoNombre;
    VentanaImagen aux = new VentanaImagen(this.panelPrincipal.getCantidadImagenes(),
                                          imgNueva,
                                          nuevoNombre,
                                          this.debug,
                                          this.panelPrincipal,
                                          nuevaRuta);
    this.panelPrincipal.getListaImagenes().add(aux);
    this.panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() + 1);
    this.panelPrincipal.add(aux);
    aux.fijarGris(true);
    aux.setAnguloGirado(ANGULO + iFoc.getAnguloGirado());
    aux.setImagenOriginal(iFoc.getImagenOriginal());
    this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
  }
  
  //TODO
  
  public void rotacionBilineal(final double ANGULO) {
    VentanaImagen iFoc = this.panelPrincipal.getImgFoco();
    BufferedImage imgFoc = iFoc.getImagenOriginal();
    final int W = imgFoc.getWidth();
    final int H = imgFoc.getHeight();
    final float ANGULO_RADIANES = (float) Math.toRadians(ANGULO + iFoc.getAnguloGirado());
    ArrayList<Matriz> extremos = calcularExtremos(ANGULO_RADIANES, W, H);
    
    Matriz tInversa = new Matriz(new float[][] {{(float) Math.cos(ANGULO_RADIANES), (float) - Math.sin(ANGULO_RADIANES)},
                                             { (float) Math.sin(ANGULO_RADIANES), (float) Math.cos(ANGULO_RADIANES)}});
    final int N_ANCHO = calcularMaximo(extremos, 0);
    final int N_ALTO = calcularMaximo(extremos, 1);
    
    BufferedImage imgNueva = new BufferedImage(N_ANCHO, N_ALTO, BufferedImage.TYPE_INT_RGB);
    
    Matriz oOrigen = new Matriz(new float[][] {{0f}, {0f}});
    oOrigen = tInversa.producto(oOrigen);
    //final Vector OP_OR = new Vector(new Point((int) oOrigen.getMatriz()[0][0], (int) oOrigen.getMatriz()[1][0]), new Point(0, 0));
    final Vector OP_OR = new Vector(new Punto(oOrigen.getMatriz()[0][0], oOrigen.getMatriz()[1][0]), new Punto(0f, 0f));
    
    
    float minY = Float.MAX_VALUE;
    float minX = Float.MAX_VALUE;
    
    for (Matriz T: extremos) {
      if (minY > T.getMatriz()[1][0]) {
        minY = T.getMatriz()[1][0];
      }
      if (minX > T.getMatriz()[0][0]) {
        minX = T.getMatriz()[0][0];
      }
    }
    
    for (int i = 0; i < N_ANCHO; i++)
      for (int j = 0; j < N_ALTO; j++) {
        final Matriz aux = new Matriz(new float[][] {{i + minX}, {j + minY}});
        final Matriz mp = tInversa.producto(aux);
       // final Vector OP_PP = new Vector(new Point((int) oOrigen.getMatriz()[0][0], (int) oOrigen.getMatriz()[1][0]), new Point((int) mp.getMatriz()[0][0], (int) mp.getMatriz()[1][0]));
        final Vector OP_PP = new Vector(new Punto(oOrigen.getMatriz()[0][0], oOrigen.getMatriz()[1][0]), new Punto(mp.getMatriz()[0][0], mp.getMatriz()[1][0]));
        final Vector OR_PP = OP_PP.restaVectores(OP_OR);
        final Punto pixel = OR_PP.getDestino();
        if ((pixel.getA() >= W) || (pixel.getA() < 0)
          || (pixel.getB() >= H) || (pixel.getB() < 0)) {
          imgNueva.setRGB(i, j, Color.YELLOW.getRGB());
        } else {
          float x = (float) pixel.getA();
          float y = (float) pixel.getB();
          int w = (int) Math.ceil(x);
          int v = (int) Math.ceil(y);
          int X = (int) Math.floor(x);
          int Y = (int) Math.floor(y);  
          if (w == W) {
            w--;
          }
          if (v == H) {
            v--;
          }
          
          int A = new Color(imgFoc.getRGB(X, v)).getRed();
          int B = new Color(imgFoc.getRGB(w, v)).getRed();
          int C = new Color(imgFoc.getRGB(X, Y)).getRed();
          int D = new Color(imgFoc.getRGB(w, Y)).getRed();
          float p = x - X;
          float q = y - Y;
          int P = (int) (C + (D - C) * p + (A - C) * q + (B + C - A - D) * p * q);
          int colorRGB = (P << 16 | P << 8 | P);
          imgNueva.setRGB(i,  j,  colorRGB);
        }
      }
    
    final String FORMATO_FICHERO = "tif";
    String[] nombre = iFoc.getNombre().split("." + FORMATO_FICHERO);
    String[] ruta = iFoc.getRuta().split(iFoc.getNombre());
    String nuevoNombre = nombre[0] + "_rotada." + FORMATO_FICHERO;
    String nuevaRuta = ruta[0] + nuevoNombre;
    VentanaImagen aux = new VentanaImagen(this.panelPrincipal.getCantidadImagenes(),
                                          imgNueva,
                                          nuevoNombre,
                                          this.debug,
                                          this.panelPrincipal,
                                          nuevaRuta);
    this.panelPrincipal.getListaImagenes().add(aux);
    this.panelPrincipal.setCantidadImagenes(panelPrincipal.getCantidadImagenes() + 1);
    this.panelPrincipal.add(aux);
    aux.fijarGris(true);
    aux.setAnguloGirado(ANGULO + iFoc.getAnguloGirado());
    aux.setImagenOriginal(iFoc.getImagenOriginal());
    this.debug.escribirMensaje("> Se ha mostrado la ecualización de histograma");
  } 
  
  private ArrayList<Matriz> calcularExtremos(final double ANGULO, final double W, final double H) {
    ArrayList<Matriz> result = new ArrayList<Matriz>();
    
    Matriz tDirecta = new Matriz(new float[][] {{(float) Math.cos(ANGULO), (float) Math.sin(ANGULO)},
                                                       {(float) - Math.sin(ANGULO), (float) Math.cos(ANGULO)}});
    result.add(new Matriz(new float[][] {{0f}, {0f}}));
    result.add(new Matriz(new float[][] {{(float) W}, {0}}));
    result.add(new Matriz(new float[][] {{0f}, {(float) H}}));
    result.add(new Matriz(new float[][] {{(float) W}, {(float) H}}));
    
    for (int i = 0; i < 4; i++) {
      result.set(i, tDirecta.producto(result.get(i)));
    }
    return (result);
  }
  
  private int calcularMaximo(ArrayList<Matriz> extr,int opc) {
    int result = 0;
    int maximo = Integer.MIN_VALUE;
    int minimo = Integer.MAX_VALUE;
    // opc == 0 -> x, opc == 1 -> y
    if (opc == 0) {
      for (Matriz T: extr) {
        if (T.getMatriz()[0][0] > maximo) {
          maximo = (int) T.getMatriz()[0][0];
        }
        if (T.getMatriz()[0][0] < minimo) {
          minimo = (int) T.getMatriz()[0][0];
        }
      }
    } else {
      for (Matriz T: extr) {
        if (T.getMatriz()[1][0] > maximo) {
          maximo = (int) T.getMatriz()[1][0];
        }
        if (T.getMatriz()[1][0] < minimo) {
          minimo = (int) T.getMatriz()[1][0];
        }
      }
    }
    maximo = Math.abs(maximo);
    minimo = Math.abs(minimo);
    result = maximo + minimo;
    return (result);
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
      new Thread(this).start();
    }
  }

  /**
* M&eacute;todo Getter para retornar
* el valor m&iacute;nimo
*
* @return valorMin
*/
  public int getValorMin() {
    return (this.valorMin);
  }

  /**
* M&eacute;todo Getter para retornar
* el valor m&aacute;ximo
*
* @return valorMax
*/
  public int getValorMax() {
    return (this.valorMax);
  }
  
  public double getEntropia() {
    return (this.entropia);
  }
  
  public double getBrillo() {
    return (this.brillo);
  }
  
  public double getContraste() {
    return (this.contraste);
  }
  
  public int[] getNivelGris() {
    return (this.nivelGris);
  }
  
  public double getAnguloGirado() {
    return (this.anguloGirado);
  }
  
  public void setAnguloGirado(double aG) {
    this.anguloGirado = aG;
  }
  
  public BufferedImage getImagenOriginal() {
    return (this.imgSinGiro);
  }
  
  public void setImagenOriginal(BufferedImage imagen) {
    this.imgSinGiro = imagen;
  }
  
  public String toString() {
    String desc = "";
    desc = "- ID: " + this.id + "\n";
    desc += "- Nombre: " + this.nombre + "\n";
    desc += "- Ruta: " + this.ruta + "\n";
    desc += "- Tamaño: [" + this.bufferImagen.getWidth() + ", " + this.bufferImagen.getHeight() + "]\n";
    desc += "- Brillo: " + this.brillo + "\n";
    desc += "- Contraste: " + this.contraste + "\n";
    desc += "- Entropía: " + this.entropia + "\n";
    desc += "- Min/Max: [" + this.valorMin + ", " + this.valorMax + "]\n";
    return (desc);
  }

}