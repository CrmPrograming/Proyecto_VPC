package vision_por_computador;

@Anotaciones(desc = "Cambiar el Array por una tabla Hash")
public interface Idiomas {
  
  /**
   * Array para idioma español.
   */
  final String[] I_ESPANOL = new String[] {
      // Categorias
      // 0          1               2           3           4
    "Archivo", "Información", "Operaciones", "Opciones","Ayuda",
      // SubCategorias
      // 5              6           7               8
    "Abrir Imagen", "Guardar", "Guardar Como...", "Salir",
      // 9                            10                 11             12
    "Histograma Absoluto", "Histograma Acumulativo", "Entropía", "Información imagen",
      // 13           14
    "Duplicar", "Cambiar a escala de Grices",
      // 15
    "Acerca de",
      // Mensajes menús
      // 16                     17        18                            19
    "Imagenes tif (*.tif)", "Aceptar", "Cancelar", "Algunos cambios tendrán lugar tras reiniciar el programa",
      // 20
    "Autores",
      // Errores
      //          21                                                  22
    "Sólo se admiten ficheros de tipo '.tif'", "No se puede realizar esta operación sin imágenes abiertas",
      //          23
    "Operación sólo para imágenes en escala de grices"
  };
  
  /**
   * Array para idioma inglés.
   */  
  final String[] I_ENGLISH = new String[] {
    // Categorias
    // 0          1            2            3         4
    "File", "Data", "Actions", "Preferences", "Help",
    // SubCategorias
    // 5            6          7          8
    "Open Image", "Save", "Save as...", "Exit",
    // 9                            10                      11          12
    "Absolute value Histogram", "Accumulative Histogram", "Entropy", "Image info",
    // 13           14
    "Duplicar", "Change to Grayscale",
    // 15
    "About",
    // Mensajes menús
    // 16                   17      18                       19
    "Tiff images (*.tif)", "Ok", "Cancel", "Some changes will take place after restarting the program",
    // 20
    "Developed by",
    // Errores
    //        21                                        22
    "Only '.tif' files accepted", "Can not perform this operation without open images",
    //  23
    "Only avaliable for Grayscale images"
  };

}
