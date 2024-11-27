package ImageEditor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageFile {

    final static int MAX_BUFFER_SIZE = 10000000;
    final static int HEADER_SIZE = 54;
    final static int PIXEL_SIZE = 4;

    byte[] headerBuffer;
    PixelStruct[] pixelBuffer;

    private int headerSize = 0;
    private int pixelBufferSize = 0 ;

    private final String pathFileString;

    /**
     * Construtor. Inicializa os buffers da imagem;
     */
    ImageFile(String pathFileString){
        this.headerBuffer = new byte[HEADER_SIZE];
        this.pixelBuffer = new PixelStruct[MAX_BUFFER_SIZE];
        this.pathFileString = pathFileString;
    }

    /**
     * Method para leitura do arquivo
     */
    void readFile(){

        // definição do caminho do arquivo
        Path inputFilePath = Paths.get(pathFileString);

        // leitura do arquivo
        try (BufferedInputStream inputFile = new BufferedInputStream(Files.newInputStream(inputFilePath))){


            System.out.println("\nLendo o arquivo: " + inputFilePath);

            //ler 54 bytes do cabeçalho da imagem
            System.out.println(" > lendo header imagem");
            headerSize = inputFile.read(headerBuffer, 0, HEADER_SIZE);

            // loop ler os pixels enquanto não chegar ao fim do arquivo. cada pixel composto por  4 bytes
            // loop ler pixels em grupos de 4 bytes
            System.out.println(" > lendo pixels");

            int pixelCount = 0;
            byte[] bytePixel = new byte[PIXEL_SIZE];

            while(inputFile.read(bytePixel, 0, PIXEL_SIZE) != -1 && pixelBufferSize < MAX_BUFFER_SIZE){

                pixelBuffer[pixelCount] = new PixelStruct( bytePixel[0] , bytePixel[1] , bytePixel[2] , bytePixel[3] );
                pixelCount++;
            }

            pixelBufferSize = pixelCount;
        }
        catch (IOException e) {
            System.out.printf("\n Erro inesperado durante a leitura do arquivo %s \n", e.getMessage());
        }
    }
    void grayScale(){

        // escreve pixels
        System.out.println(" > aplicando gray effect nos pixels");

        int red, green, blue, gray;

        // buffer temporário.
        PixelStruct[] tempBuffer = new PixelStruct[pixelBufferSize];

        for(int bytesCount = 0; bytesCount < pixelBufferSize; bytesCount++) {

            //Converte o byte para um valor unsigned ( de 0 a 255), sou seja considera apenas os 8 bits do valor.
            blue = (pixelBuffer[bytesCount].blue()   & 0xFF) ;
            green= (pixelBuffer[bytesCount].green()  & 0xFF);
            red =  (pixelBuffer[bytesCount].red()    & 0xFF);

            // Cálculo do tom de cinza
            gray = Math.round((blue + green + red )/3.0f) ;

            // Define os valores do pixel no formato RGBA
            tempBuffer[bytesCount] = new PixelStruct((byte) gray,(byte) gray,(byte) gray,(byte) gray);

        }

        pixelBuffer = tempBuffer;

    }
    /**
     * Method para escrita do arquivo
     */
    void writeFile(){
        // escrever tudo num novo arquivo
        Path outPathFile = Paths.get(".\\ImageEditor\\assets\\image1copy.bmp");

        System.out.println("\nCopiando o arquivo: "+ outPathFile);
        try(BufferedOutputStream outputFile = new BufferedOutputStream(Files.newOutputStream(outPathFile))){
            byte[] bytePixel = new byte[PIXEL_SIZE];

            // escreve header1
            int bytesCount = 0;

            System.out.println(" > escrevendo header file");
            outputFile.write( headerBuffer, 0, headerSize);

            // escreve pixels
            System.out.println(" > escrevendo pixels");

            while (bytesCount < pixelBufferSize) {

                // Define os valores do pixel no formato RGBA
                bytePixel[0] = pixelBuffer[bytesCount].blue() ; // Blue
                bytePixel[1] = pixelBuffer[bytesCount].green() ; // Green
                bytePixel[2] = pixelBuffer[bytesCount].red() ; // Red
                bytePixel[3] = pixelBuffer[bytesCount].alpha(); // Alpha

                // Grava o pixel no arquivo de saída
                outputFile.write(bytePixel, 0, PIXEL_SIZE );
                bytesCount++;
            }

        }catch(IOException e){
            System.out.printf("\n Erro inesperado durante a escrita do arquivo  %s \n", e.getMessage());


        }
    }

}
