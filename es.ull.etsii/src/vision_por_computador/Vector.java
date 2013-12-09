package vision_por_computador;

import java.awt.Point;

public class Vector {
  
  private Point origen;
  private Point destino;
  private int[] coordenadas;
  
  public Vector(Point x, Point y) {
    this.origen = x;
    this.destino = y;
    this.coordenadas = new int[2];
    this.coordenadas[0] = (int) (y.getX() - x.getX());
    this.coordenadas[1] = (int) (y.getY() - x.getY());
  }
  
  public Vector(int u, int v, Point origen) {
    this.coordenadas = new int[2];
    this.coordenadas[0] = u;
    this.coordenadas[1] = v;
    this.origen = origen;
    this.destino = new Point((int) (u + origen.getX()), (int) (v + origen.getY()));
  }
  
  public Vector sumaVectores(Vector v) {
    Vector result = null;
    result = new Vector(this.coordenadas[0] + v.getCoordenadas()[0], this.coordenadas[1] + v.getCoordenadas()[1], this.origen);    
    return (result);
  }
  
  public int[] getCoordenadas() {
    return (this.coordenadas);
  }
  
  public Point getOrigen() {
    return (this.origen);
  }

}
