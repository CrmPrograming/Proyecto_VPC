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
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.jfree.ui.RefineryUtilities;

public class VentanaImagen extends JInternalFrame {
  
  private BufferedImage bufferImagen;
  private PanelImagen panelImagen;
  private int id;
  private VentanaDebug debug;
  private ArrayList<VentanaImagen> listaImagens;
  private String nombre;
  private PanelPrincipal panelPrincipal;
  private String ruta;
  private boolean escalaGris;
  private int valorMin;
  private int valorMax;
  private int[] nivelGris; 
  
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
    this.panelPrincipal = pPrincipal;
    this.panelImagen = new PanelImagen();
    setContentPane(this.panelImagen); 
    setVisible(true);
    this.addInternalFrameListener(new InternalFrameAdapter() {      
      
      @Override
      public void internalFrameClosing(InternalFrameEvent e) {
        listaImagens.remove(this);
        debug.escribirMensaje("> Cerrada la imagen " + nombre);
        if (listaImagens.size() == 0) {
          panelPrincipal.setFocus(null);
        }
      }
      
      public void internalFrameActivated(InternalFrameEvent e) {
        panelPrincipal.setFocus((VentanaImagen) e.getSource());
        debug.escribirMensaje("> Foco en imagen " + nombre);
      }
      
    });
  }  

  private class PanelImagen extends JPanel {
    
    private boolean marcado = false;
    private int posX = 0;
    private int posY = 0;
    private int posXActual = 0;
    private int posYActual = 0;
    private Timer temporizador;
    private final int DELAY = 100;
    private JPanel panelG;
    
    public PanelImagen() {
      super(new FlowLayout());
      this.panelG = this;
      this.temporizador = new Timer(0, new TimerListener());
      this.temporizador.setDelay(DELAY);
      this.temporizador.start();      
      this.addMouseListener(new MouseAdapter() {
        
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
              subImagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_GRAY);
              int w = 0;
              int h = 0;
              for (int i = posX; i < posXActual; i++) {
                for (int j = posY; j < posYActual; j++) {
                  subImagen.setRGB(w, h, bufferImagen.getRGB(i, j));
                  h++;
                }
                h = 0;
                w++;
              }            
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

        @Override
        public void mouseMoved(MouseEvent arg0) {
          posXActual = (int) arg0.getPoint().getX();
          posYActual = (int) arg0.getPoint().getY();
        }
        
      });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      dibujarImagen(g);
      dibujarRectangulo(g);
      dibujarInformacion(g);
    }
    
    private void dibujarImagen(Graphics g) {
      g.drawImage(bufferImagen, 0, 0, null);
    }
    
    private void dibujarRectangulo(Graphics g) {
      if (this.marcado) {
        Color aux = g.getColor();
        g.setColor(Color.RED);
        g.drawRect(this.posX, this.posY, this.posXActual - this.posX, this.posYActual - this.posY);
        g.setColor(aux);
      }
    }
    
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
      g.setColor(Color.RED);
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

      @Override
      public void actionPerformed(ActionEvent arg0) {
        panelG.repaint();
      }
      
    }
    
  }
  
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
        if (this.nivelGris[color.getRed()] > this.valorMax) {
          this.valorMax = this.nivelGris[color.getRed()]; 
        }
        if (this.nivelGris[color.getRed()] < this.valorMin) {
          this.valorMin = this.nivelGris[color.getRed()];
        }
      }
    }
  }
  
  public void dibujarHistogramaAbsoluto(String title) {
    HistogramaAbsoluto histo = new HistogramaAbsoluto(title, this.nivelGris);    
    histo.pack();
    RefineryUtilities.centerFrameOnScreen(histo);
    histo.setVisible(true);
  }
  
  public void dibujarHistogramaAcumulativo(String title) {
    HistogramaAcumulativo histo = new HistogramaAcumulativo(title, this.nivelGris);    
    histo.pack();
    RefineryUtilities.centerFrameOnScreen(histo);
    histo.setVisible(true);
  }
  
  public void setID(int idVentana) {
    this.id = idVentana;
  }
  
  public int getID() {
    return this.id;
  }
  
  public String getNombre() {
    return this.nombre;
  }
  
  public BufferedImage getImagen() {
    return this.bufferImagen;
  }
  
  public String getRuta() {
    return this.ruta;
  }
  
  public boolean esGris() {
    return this.escalaGris;
  }
  
  public void fijarGris(boolean gris) {
    this.escalaGris = gris;
    if (gris) {
      calcularNiveles();
    }
  }

  public int getValorMin() {
    return (valorMin);
  }

  public int getValorMax() {
    return (valorMax);
  } 

}
