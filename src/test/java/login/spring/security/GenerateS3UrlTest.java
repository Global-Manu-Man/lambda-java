package login.spring.security;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GenerateS3UrlTest {

    @InjectMocks
    private GenerateS3Url generateS3Url;

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private Context context;

    @Mock
    private LambdaLogger logger;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(context.getLogger()).thenReturn(logger);
    }


    @Test
    public void testHandleRequest_withDecodingError() {
        // Preparar los datos de entrada con contenido inválido
        S3UploadRequest request = new S3UploadRequest();
        request.setFileNames(Collections.singletonList("file1.pdf"));
        request.setFileContents(Collections.singletonList("invalid_base64"));

        // Llamar al método a probar
        Map<String, Object> response = generateS3Url.handleRequest(request, context);

        // Verificar los resultados
        assertEquals(200, response.get("statusCode"));
        List<String> urls = (List<String>) response.get("body");
        assertEquals(1, urls.size());
        assertEquals("Error de decodificación Base64: Illegal base64 character 5f", urls.get(0));

        // Verificar que los métodos del cliente S3 no fueron llamados
        verify(s3Client, never()).putObject(anyString(), anyString(), any(ByteArrayInputStream.class), any(ObjectMetadata.class));
        verify(s3Client, never()).generatePresignedUrl(anyString(), anyString(), any(Date.class));
    }


}
