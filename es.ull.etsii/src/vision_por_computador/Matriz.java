package vision_por_computador;

public class Matriz {
  
  private float[][] A;
  private int m;
  private int p;
  
  public Matriz(float[][] B) {
    this.m = B.length;
    this.p = B[0].length;
    this.A = new float[this.m][this.p];
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.p; j++) {
        this.A[i][j] = B[i][j];
      }
    }
  }
  
  public Matriz producto(Matriz B) {
    float[][] c = new float[this.m][B.getP()];
    float[][] a = this.getMatriz();
    float[][] b = B.getMatriz();
    
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < c[0].length; j++) {
        c[i][j] = sumatorio(a, b, i, j);
      }
    }
    return (new Matriz(c));
  }
  
  private float sumatorio(float[][] a, float[][] b, int i, int j) {
    float result = 0;
    for (int k = 0; k < b.length; k++) {
      result += a[i][k] * b[k][j];
    }
    return (result);
  }
  
  public String toString() {
    String result = "";
    result = "[" + this.m + " x " + this.p + "]\n";
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.p; j++) {
        result += this.A[i][j] + " ";
      }
      result += "\n";
    }
    return (result);
  }

  public int getM() {
    return (this.m);
  }

  public int getP() {
    return (this.p);
  }
  
  public float[][] getMatriz() {
    return (this.A);
  }

}
