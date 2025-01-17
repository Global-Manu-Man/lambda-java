
# Proyecto Lambda: Generación de URLs Pre-firmadas de S3

Este proyecto contiene una función Lambda en Java que sube archivos a un bucket de S3 y genera URLs pre-firmadas para acceder a esos archivos.

## Estructura del Proyecto


## Descripción de la Función

### GenerateS3Url

La clase `GenerateS3Url` implementa la interfaz `RequestHandler` para manejar solicitudes de subida de archivos a S3 y generación de URLs pre-firmadas.

#### Método `handleRequest`

Este método es el punto de entrada principal para la función Lambda. Toma un objeto `S3UploadRequest` como entrada y devuelve un mapa con el código de estado, encabezados y cuerpo de la respuesta.

- **Parámetros de Entrada:**
    - `input`: Un objeto `S3UploadRequest` que contiene los nombres y contenidos de los archivos.
    - `context`: El contexto de la función Lambda, que proporciona acceso al logger y otros detalles de ejecución.

- **Salida:**
    - Un mapa con el código de estado (200 si es exitoso), encabezados y una lista de URLs pre-firmadas o mensajes de error en el cuerpo.

#### Método `generateUniqueFileName`

Este método genera un nombre de archivo único usando UUID para evitar colisiones en S3.


# Consideraciones de Seguridad

Decodificación de Base64:
Asegúrate de manejar correctamente cualquier error de decodificación de Base64 para evitar fallos en la subida de archivos.

Permisos de S3:
Asegúrate de que la función Lambda tenga los permisos adecuados para subir archivos y generar URLs pre-firmadas en S3.


- **Parámetros de Entrada:**
    - `originalFileName`: El nombre original del archivo.

- **Salida:**
    - Un nombre de archivo único.

## Uso

### Configuración

1. **Configuración de AWS CLI:**
   Asegúrate de tener configurado AWS CLI en tu entorno con las credenciales adecuadas para acceder a S3.

2. **Crear un bucket de S3:**
   Crea un bucket de S3 (o usa uno existente) donde se subirán los archivos.

### Implementación

1. **Compilar y Empaquetar:**
   Compila y empaqueta tu proyecto utilizando Maven o Gradle para generar el archivo `.jar`.

2. **Desplegar la Lambda:**
   Despliega la función Lambda usando AWS Management Console, AWS CLI o AWS SAM.

### Ejemplo de Solicitud

Envía una solicitud a la función Lambda con el siguiente formato:

```json
{
    "fileNames": ["documento1.pdf", "documento2.pdf"],
    "fileContents": ["<base64_content_1>", "<base64_content_2>"]
}

// Ejemplo de Respuesta

{
    "statusCode": 200,
    "headers": {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "POST, GET, OPTIONS, PUT, DELETE",
        "Access-Control-Allow-Headers": "Content-Type"
    },
    "body": [
        "https://crezes3.s3.amazonaws.com/<unique_file_name_1>",
        "https://crezes3.s3.amazonaws.com/<unique_file_name_2>"
    ]
}



