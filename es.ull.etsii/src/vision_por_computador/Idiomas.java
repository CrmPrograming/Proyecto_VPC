package vision_por_computador;

import java.util.Collections;
import java.util.HashMap;

public interface Idiomas {
  
  /**
   *  Tabla Hash con los mensajes
   *  del programa en Español
   *  
   */
  @Anotaciones(desc = "Implementar usando fichero XML")
  final HashMap<String, String> I_ESPANOL = new HashMap<String, String> (Collections.unmodifiableMap(
      new HashMap<String, String>() {{
        
        // Categorías
        
        put("c_archivo", "Archivo");
        put("c_informacion", "Información");
        put("c_operaciones", "Operaciones");
        put("c_opciones", "Opciones");
        put("c_ayuda", "Ayuda");
        
        // Subcategorías
        
        put("s_abrir", "Abrir Imagen");
        put("s_guardar", "Guardar");
        put("s_gComo", "Guardar Como...");
        put("s_salir", "Salir");
        put("s_hAbsoluto", "Histograma Absoluto");
        put("s_hAcumulativo", "Histograma Acumulativo");
        put("s_entropia", "Entropía");
        put("s_iImagen", "Información Imagen");
        put("s_duplicar", "Duplicar");
        put("s_cEscalaGrises", "Cambiar a escala de Grises");
        put("s_acerca", "Acerca de");
        put("s_oLineales", "Operaciones Lineales");
        put("s_aBrilloContraste", "Ajuste lineal del brillo y contraste");
        put("s_nLineales", "Operaciones no Lineales");
        put("s_dImagenes", "Diferencia de imágenes");
        put("s_mCambios", "Mapa de Cambios");
        put("s_gamma", "Correción Gamma");
        put("s_eqHistograma", "Ecualización de Histograma");
        put("s_espHistograma", "Especificación de Histograma");
        put("s_opGeometricas", "Operaciones Geométricas");
        put("s_trasnEspejo", "Transformaciones Espejo");
        put("s_espejoHorizontal", "Espejo Horizontal");
        put("s_espejoVertical", "Espejo Vertical");
        put("s_traspuesta", "Traspuesta Imagen");
        put("s_rotaciones", "Rotaciones");
        put("s_rEspecifica", "Rotaciones Específicas");
        put("s_m90", "Multiplo 90");
        put("s_90", "+ 90º");
        put("s_180", "+ 180º");
        put("s_270", "+ 270º");
        put("s_-90", "- 90º");
        put("s_-180", "- 180º");
        put("s_-270", "- 270º");
        put("s_escalado", "Escalado de imagen");
        
        // Mensajes Menús
        
        put("mm_iTif", "Imagenes tif (*.tif)");
        put("mm_aceptar", "Aceptar");
        put("mm_cancelar", "Cancelar");
        put("mm_algunosCambios", "Algunos cambios tendrán lugar tras reiniciar el programa");
        put("mm_autores", "Autores");
        
        // Errores
        
        put("e_solotif", "Sólo se admiten ficheros de tipo '.tif'");  // 21
        put("e_iAbierta", "No se puede realizar esta operación sin imágenes abiertas");  // 22
        put("e_iGris", "Operación sólo para imágenes en escala de grises");   // 23   
        put("e_fNumerico", "El valor dado debe ser un número"); // 24
        put("e_mImagenes", "No se puede realizar esta operación sin tener 2 imágenes abiertas como mínimo");
      }}));
     
  /**
   *  Tabla Hash con los mensajes
   *  del programa en Inglés
   *  
   */
  @Anotaciones(desc = "Implementar usando fichero XML")
  final HashMap<String, String> I_ENGLISH = new HashMap<String, String> (Collections.unmodifiableMap(
      new HashMap<String, String>() {{
        
        // Categorías
        
        put("c_archivo", "File");
        put("c_informacion", "Data");
        put("c_operaciones", "Actions");
        put("c_opciones", "Preferences");
        put("c_ayuda", "Help");
        
        // Subcategorías

        put("s_abrir", "Open Image");
        put("s_guardar", "Save");
        put("s_gComo", "Save as...");
        put("s_salir", "Exit");
        put("s_hAbsoluto", "Absolute value Histogram");
        put("s_hAcumulativo", "Accumulative Histogram");
        put("s_entropia", "Entropy");
        put("s_iImagen", "Image info");
        put("s_duplicar", "Duplicate");
        put("s_cEscalaGrises", "Change to Grayscale");
        put("s_acerca", "About");
        put("s_oLineales", "Lineal Operations");
        put("s_aBrilloContraste", "Brightness and Contrast lineal adjust");
        put("s_nLineales", "Not Lineal Operations");
        put("s_dImagenes", "Differences between two images");
        put("s_mCambios", "Change's map");
        put("s_gamma", "Gamma Correction");
        put("s_eqHistograma", "Histogram Equalization ");
        put("s_espHistograma", "Histogram Specification");
        put("s_opGeometricas", "Operaciones Geométricas");
        put("s_trasnEspejo", "Transformaciones Espejo");
        put("s_espejoHorizontal", "Espejo Horizontal");
        put("s_espejoVertical", "Espejo Vertical");
        put("s_traspuesta", "Traspuesta Imagen");
        put("s_rotaciones", "Rotaciones");
        put("s_rEspecifica", "Rotaciones Específicas");
        put("s_m90", "Multiplo 90");
        put("s_90", "+ 90º");
        put("s_180", "+ 180º");
        put("s_270", "+ 270º");
        put("s_-90", "- 90º");
        put("s_-180", "- 180º");
        put("s_-270", "- 270º");
        put("s_escalado", "Escalado de imagen");
        
        // Mensajes Menús
        
        put("mm_iTif", "Tiff images (*.tif)");
        put("mm_aceptar", "Ok");
        put("mm_cancelar", "Cancel");
        put("mm_algunosCambios", "Some changes will take place after restarting the program");
        put("mm_autores", "Developed by");
        
        // Errores
        
        put("e_solotif", "Only '.tif' files accepted"); // 21
        put("e_iAbierta", "Can not perform this operation without open images"); // 22
        put("e_iGris", "Only avaliable for Grayscale images"); // 23
        put("e_fNumerico", "The given value must be a number"); // 24
        put("e_mImagenes", "Can not perform this operation without two open image at least");
      }}));

}
