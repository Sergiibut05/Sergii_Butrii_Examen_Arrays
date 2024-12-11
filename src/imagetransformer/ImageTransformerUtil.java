package imagetransformer;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTransformerUtil {
    // Constantes ANSI
    private static final String ANSI_RESET = "\033[0m";
    
    private static final int ANSI_WHITE = 15;  // Color blanco en ANSI 256 colores
    private static final int ANSI_COLOR_OFFSET = 16;  // Inicio de los colores RGB en ANSI 256
    private static final int ANSI_COLOR_STEPS = 6;    // Número de niveles por componente RGB
    private static final int ANSI_RED_MULTIPLIER = 36;  // Multiplicador para componente rojo
    private static final int ANSI_GREEN_MULTIPLIER = 6;  // Multiplicador para componente verde

    /**
     * Convierte una imagen BufferedImage en una matriz 2D de enteros que representan colores ANSI.
     * Nota: No toques nada en este método
     * @param image La imagen BufferedImage a convertir
     * @return Una matriz 2D de enteros donde cada valor representa un código de color ANSI
     */
    private static int[][] get2DPixelArrayFast(BufferedImage image) {
        byte[] pixelData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int width = image.getWidth();
        int height = image.getHeight();
        boolean hasAlphaChannel = image.getAlphaRaster() != null;
    
        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            int numberOfValues = 4;
            for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {
                // Obtenemos los valores ARGB en el orden correcto
                int alpha = pixelData[valueIndex] & 0xff;
                int blue = pixelData[valueIndex + 1] & 0xff;
                int green = pixelData[valueIndex + 2] & 0xff;
                int red = pixelData[valueIndex + 3] & 0xff;
                
                // Si el pixel es transparente (alpha cercano a 0), usamos blanco
                if (alpha < 128) {
                    result[row][col] = ANSI_WHITE;
                } else {
                    // Convertimos los valores RGB a la escala de colores ANSI
                    int r = (red * ANSI_COLOR_STEPS) / 256;
                    int g = (green * ANSI_COLOR_STEPS) / 256;
                    int b = (blue * ANSI_COLOR_STEPS) / 256;
                    
                    // Calculamos el código de color ANSI final
                    result[row][col] = ANSI_COLOR_OFFSET + 
                                      (ANSI_RED_MULTIPLIER * r) + 
                                      (ANSI_GREEN_MULTIPLIER * g) + 
                                      b;
                }
    
                // Avanzamos a la siguiente posición en la matriz
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            int numberOfValues = 3;
            for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {
                // Obtenemos los valores RGB en el orden correcto
                int blue = pixelData[valueIndex] & 0xff;
                int green = pixelData[valueIndex + 1] & 0xff;
                int red = pixelData[valueIndex + 2] & 0xff;
                
                // Convertimos los valores RGB a la escala de colores ANSI
                int r = (red * ANSI_COLOR_STEPS) / 256;
                int g = (green * ANSI_COLOR_STEPS) / 256;
                int b = (blue * ANSI_COLOR_STEPS) / 256;
                
                // Calculamos el código de color ANSI final
                result[row][col] = ANSI_COLOR_OFFSET + 
                                  (ANSI_RED_MULTIPLIER * r) + 
                                  (ANSI_GREEN_MULTIPLIER * g) + 
                                  b;
    
                // Avanzamos a la siguiente posición en la matriz
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
    
        return result;
    }

    
    /**
     * Carga una imagen PNG desde un archivo y la convierte en una matriz de píxeles
     * Nota: No toques nada en este método
     * @param rutaArchivo Ruta del archivo PNG a cargar
     * @return Matriz de enteros que representa los píxeles de la imagen, o null si hay error
     */
    public static int[][] cargarImagenPNG(String rutaArchivo) {
        try {
            // Cargar la imagen desde el archivo
            BufferedImage imagen = ImageIO.read(new File(rutaArchivo));
            // Convertir la imagen a matriz de píxeles
            return get2DPixelArrayFast(imagen);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }


    /**
     * Debes devolver el codigo ANSI para establecer el color de fondo
     * Ayuda: Monta la cadena ANSI con el formato \u001B[48;5;%dm
     * Donde el valor de %d es el valor del color que quieres establecer
     * Si el valor que recibes es menor que 0 o mayor que 255, debes limitar el valor a 0 o 255
     * @param value Valor entre 0 y 255 que representa el código de color ANSI
     * @return String con la secuencia ANSI para establecer el color de fondo
     */
    private static String obtenerCodigoAnsi(int value) {
        String valor ="";
        if(value<256 && value>-1){
            valor="\u001B[48;5;"+value+"m";
        }else{
            System.out.println("ERROR: El valor tiene que estar en el rango 0-255");
        }
        return valor;
    }

    /**
     * Rota la matriz 90 grados en sentido horario
     * Ayuda: Tienes que intercambiar las filas y columnas de la matriz
     * La primera fila de la matriz original se convierte en la última columna de la matriz rotada
     * La segunda fila de la matriz original se convierte en la penúltima columna de la matriz rotada
     * Y así sucesivamente
     * Debes devolver una nueva matriz que sea la rotación de la matriz original
     * @param matriz Matriz de entrada
     * @return Nueva matriz rotada
     */
    public static int[][] rotarDerecha(int[][] matriz) {
        int[][] matrizgirada= new int[matriz[0].length][matriz.length];
        for(int i=0;i<matriz.length;i++){
            for(int j=0;j<matriz[0].length;j++){
                matrizgirada[j][matriz.length-1-i]=matriz[i][j];
            }
        }
        return matrizgirada;
    }

    /**
     * Rota la matriz 90 grados en sentido antihorario
     * Ayuda: Tienes que intercambiar las filas y columnas de la matriz
     * la primera columna de la matriz original se convierte en la primera fila de la matriz rotada
     * la segunda columna de la matriz original se convierte en la segunda fila de la matriz rotada
     * y así sucesivamente
     * Debes devolver una nueva matriz que sea la rotación de la matriz original
     * @param matriz Matriz de entrada
     * @return Nueva matriz rotada
     */
    public static int[][] rotarIzquierda(int[][] matriz) {
        int[][] matrizgirada= new int[matriz[0].length][matriz.length];
        for(int i=0;i<matriz.length;i++){
            for(int j=0;j<matriz[0].length;j++){
                matrizgirada[matriz.length-1-j][i]=matriz[i][j];
            }
        }
        return matrizgirada;
    }

    /**
     * Refleja la matriz horizontalmente
     * Ayuda: Tienes que intercambiar las columnas de la matriz
     * La primera columna de la matriz original se convierte en la última columna de la matriz reflejada
     * La segunda columna de la matriz original se convierte en la penúltima columna de la matriz reflejada
     * Y así sucesivamente
     * Debes devolver una nueva matriz que sea la reflexión de la matriz original
     * @param matriz Matriz de entrada
     * @return Nueva matriz reflejada
     */
    public static int[][] espejoHorizontal(int[][] matriz) {
        int[][] matrizgirada= new int[matriz[0].length][matriz.length];
        for(int i=0;i<matriz.length;i++){
            for(int j=0;j<matriz[0].length;j++){
                matrizgirada[i][matriz.length-1-j]=matriz[i][j];
            }
        }
        return matrizgirada;
    }

    /**
     * Refleja la matriz verticalmente
     * Ayuda: Tienes que intercambiar las filas de la matriz
     * La primera fila de la matriz original se convierte en la última fila de la matriz reflejada
     * La segunda fila de la matriz original se convierte en la penúltima fila de la matriz reflejada
     * Y así sucesivamente
     * Debes devolver una nueva matriz que sea la reflexión de la matriz original
     * @param matriz Matriz de entrada
     * @return Nueva matriz reflejada
     */
    public static int[][] espejoVertical(int[][] matriz) {
        int[][] matrizgirada= new int[matriz[0].length][matriz.length];
        for(int i=0;i<matriz.length;i++){
            for(int j=0;j<matriz[0].length;j++){
                matrizgirada[matriz[0].length-1-i][j]=matriz[i][j];
            }
        }
        return matrizgirada;
    }

    /**
     * Imprime la matriz usando colores ANSI
     * Los valores de la matriz deben estar en el rango 0-255
     * Deberás usar el método obtenerCodigoAnsi para obtener el color de fondo correcto
     * y concatenar dicho código con dos espacios en blanco. Eso para cada elemento de la matriz
     * Recuerda en cada fila debes imprimir el reset de color
     * @param matriz Matriz a imprimir
     */
    public static void imprimirEnColorMatriz(int[][] matriz) {
        String[][] matrizcolor= new String[matriz.length][matriz[0].length];
        for(int i=0;i<matriz.length;i++){
            for(int j=0;j<matriz[0].length;j++){
                if(matriz[i][j]>-1 && matriz[i][j]<256){
                System.out.print(obtenerCodigoAnsi(matriz[i][j])+"  ");
                }else{
                    System.out.println("ERROR: El valor tiene que estar en el rango 0-255");
                    break;
                }
            }
            System.out.println(ANSI_RESET);
        }
        System.out.println();
    }
} 