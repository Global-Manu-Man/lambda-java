package login.spring.security;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

/**
 * Clase GenerateS3Url que implementa la interfaz RequestHandler para manejar
 * solicitudes de subida de archivos a S3 y generación de URLs pre-firmadas.
 */
public class GenerateS3Url implements RequestHandler<S3UploadRequest, Map<String, Object>> {

    /**
     * Método principal que maneja la solicitud de subida de archivos a S3 y genera
     * URLs pre-firmadas.
     *
     * @param input   Objeto S3UploadRequest que contiene los nombres y contenidos
     *                de los archivos.
     * @param context Contexto de la función Lambda.
     * @return Mapa con el código de estado, encabezados y cuerpo de la respuesta.
     */
    @Override
    public Map<String, Object> handleRequest(S3UploadRequest input, Context context) {
        LambdaLogger logger = context.getLogger(); // Obtener el logger del contexto de Lambda
        List<String> fileNames = input.getFileNames(); // Obtener la lista de nombres de archivos del input
        List<String> fileContents = input.getFileContents(); // Obtener la lista de contenidos de archivos del input
        List<String> urls = new ArrayList<>(); // Lista para almacenar las URLs pre-firmadas generadas

        String bucketName = "crezes3"; // Nombre del bucket de S3
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient(); // Crear cliente de S3

        // Iterar sobre los nombres y contenidos de los archivos
        for (int i = 0; i < fileNames.size(); i++) {
            String originalFileName = fileNames.get(i); // Obtener el nombre original del archivo
            String fileContent = fileContents.get(i); // Obtener el contenido del archivo
            String uniqueFileName = generateUniqueFileName(originalFileName); // Generar un nombre único para el archivo
            logger.log("Subiendo archivo a S3: " + uniqueFileName);

            try {
                // Decodificar el contenido del archivo de Base64 a byte array
                byte[] fileData = Base64.getDecoder().decode(fileContent);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(fileData.length); // Establecer la longitud del contenido
                metadata.setContentType("application/pdf"); // Establecer el tipo de contenido

                // Subir el archivo a S3
                s3Client.putObject(bucketName, uniqueFileName, new ByteArrayInputStream(fileData), metadata);
                logger.log("Archivo subido exitosamente: " + uniqueFileName);

                // Generar una URL pre-firmada para acceder al archivo
                URL s3Url = s3Client.generatePresignedUrl(bucketName, uniqueFileName, new Date(System.currentTimeMillis() + (1000 * 60 * 60))); // El enlace temporal es válido por una hora
                urls.add(s3Url.toString());
                logger.log("Enlace temporal generado exitosamente: " + s3Url.toString());

            } catch (IllegalArgumentException e) {
                logger.log("Error de decodificación Base64: " + e.getMessage());
                urls.add("Error de decodificación Base64: " + e.getMessage());
            } catch (Exception e) {
                logger.log("Error subiendo el archivo: " + e.getMessage());
                urls.add("Error subiendo el archivo: " + e.getMessage());
            }
        }

        // Establecer los encabezados de la respuesta
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        headers.put("Access-Control-Allow-Headers", "Content-Type");

        // Crear el mapa de respuesta con el código de estado, encabezados y cuerpo
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("headers", headers);
        response.put("body", urls);

        return response;
    }

    /**
     * Método para generar un nombre de archivo único usando UUID.
     *
     * @param originalFileName Nombre original del archivo.
     * @return Nombre de archivo único.
     */
    private String generateUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString(); // Generar un UUID
        return uuid + "-" + originalFileName; // Concatenar el UUID con el nombre original del archivo
    }
}