package vision_por_computador;

public class Vector {
  
  private Punto origen;
  private Punto destino;
  private float[] coordenadas;
  private float distancia;
  
  public Vector(Punto x, Punto y) {
    this.origen = x;
    this.destino = y;
    this.coordenadas = new float[2];
    this.coordenadas[0] = (y.getA() - x.getA());
    this.coordenadas[1] = (y.getB() - x.getB());
    this.distancia = (float) Math.sqrt(Math.pow((x.getA() - y.getA()), 2) + Math.pow((x.getB() - y.getB()), 2));
  }
  
  public Vector(float u, float v, Punto origen) {
    this.coordenadas = new float[2];
    this.coordenadas[0] = u;
    this.coordenadas[1] = v;
    this.origen = origen;
    this.destino = new Punto((u + origen.getA()), (v + origen.getB()));
    this.distancia = (float) Math.sqrt(Math.pow((origen.getA() - destino.getA()), 2) + Math.pow((origen.getB() - destino.getB()), 2));
  }
  
  public Vector restaVectores(Vector v) {
    Vector result = null;
    result = new Vector(this.coordenadas[0] - v.getCoordenadas()[0], this.coordenadas[1] - v.getCoordenadas()[1], this.origen);    
    return (result);
  }
  
  public float[] getCoordenadas() {
    return (this.coordenadas);
  }
  
  public Punto getOrigen() {
    return (this.origen);
  }
  
  public Punto getDestino() {
    return (this.destino);
  }
  
  public float getDistancia() {
    return (this.distancia);
  }

}
