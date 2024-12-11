import imagetransformer.ImageTransformerUtil;

public class App {
    /**
     * Muestra el menú principal de opciones al usuario.
     * Imprime las diferentes transformaciones que se pueden realizar sobre la imagen:
     * - Rotación horaria
     * - Rotación antihoraria  
     * - Espejo horizontal
     * - Espejo vertical
     * También muestra la opción de salir del programa.
     */
    private static void mostrarMenu() {
        System.out.println("\nMENU DE OPCIONES:");
        System.out.println("1. Rotar en sentido horario");
        System.out.println("2. Rotar en sentido antihorario");
        System.out.println("3. Espejo horizontal");
        System.out.println("4. Espejo vertical");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Limpia la consola usando secuencias de escape ANSI.
     * \033[H mueve el cursor al inicio de la pantalla.
     * \033[2J borra toda la pantalla.
     * System.out.flush() asegura que los cambios se apliquen inmediatamente.
     */
    private static void limpiarConsola() {
        // Limpiar pantalla
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Muestra la imagen actual y el menú de opciones.
     * Primero imprime un mensaje indicando que se mostrará la imagen,
     * luego utiliza ImageTransformerUtil para mostrar la matriz de colores,
     * y finalmente muestra el menú de opciones disponibles.
     * @param matriz La matriz de píxeles que representa la imagen a mostrar
     */
    private static void mostrarImagenYMenu(int[][] matriz) {
        System.out.println("Imagen actual:");
        ImageTransformerUtil.imprimirEnColorMatriz(matriz);
        mostrarMenu();
    }

    public static void main(String[] args) {
        // Cargar la imagen inicial
        int[][] matriz = ImageTransformerUtil.cargarImagenPNG("image.png");
        if (matriz == null) {
            System.out.println("Error al cargar la imagen. El programa terminará.");
            return;
        }
        
        boolean continuar = true;
        while (continuar) {
            limpiarConsola();
            mostrarImagenYMenu(matriz);
            
            String opcion = System.console().readLine();

            switch (opcion) {
                case "1":
                    matriz = ImageTransformerUtil.rotarDerecha(matriz);
                    break;
                case "2":
                    matriz = ImageTransformerUtil.rotarIzquierda(matriz);
                    break;
                case "3":
                    matriz = ImageTransformerUtil.espejoHorizontal(matriz);
                    break;
                case "4":
                    matriz = ImageTransformerUtil.espejoVertical(matriz);
                    break;
                case "5":
                    continuar = false;
                    limpiarConsola();
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Presione Enter para continuar...");
                    System.console().readLine();
            }
        }
    }
}
