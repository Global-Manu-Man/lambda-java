package login.spring.security;

import java.util.List;

/**
 * Clase S3UploadRequest que representa una solicitud para subir archivos a S3.
 * Contiene los nombres de los archivos y sus contenidos codificados en Base64.
 */
public class S3UploadRequest {
    private List<String> fileNames; // Lista de nombres de archivos
    private List<String> fileContents; // Contenido de los archivos codificado en Base64

    /**
     * Constructor por defecto.
     */
    public S3UploadRequest() {}

    /**
     * Obtiene la lista de nombres de archivos.
     *
     * @return Lista de nombres de archivos.
     */
    public List<String> getFileNames() {
        return fileNames;
    }

    /**
     * Establece la lista de nombres de archivos.
     *
     * @param fileNames Lista de nombres de archivos.
     */
    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    /**
     * Obtiene la lista de contenidos de archivos codificados en Base64.
     *
     * @return Lista de contenidos de archivos codificados en Base64.
     */
    public List<String> getFileContents() {
        return fileContents;
    }

    /**
     * Establece la lista de contenidos de archivos codificados en Base64.
     *
     * @param fileContents Lista de contenidos de archivos codificados en Base64.
     */
    public void setFileContents(List<String> fileContents) {
        this.fileContents = fileContents;
    }
}
