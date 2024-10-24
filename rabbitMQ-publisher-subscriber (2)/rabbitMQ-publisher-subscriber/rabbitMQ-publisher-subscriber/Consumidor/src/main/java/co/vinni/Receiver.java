package co.vinni;

import com.rabbitmq.client.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);
    private static final String QUEUE_NAME = "message_queue";
    private static final String SERVER = "127.0.0.1";

    public static void startWorker() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SERVER);  // Conexión al servidor
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        String currentDir = System.getProperty("user.dir");
        log.info("Directorio actual: " + currentDir);


        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        log.info(" [*] Esperando mensajes en la cola. Presiona CTRL+C para salir.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);


            Map<String, Object> headers = delivery.getProperties().getHeaders();
            String messageType = headers != null && headers.get("messageType") != null
                    ? headers.get("messageType").toString()
                    : "desconocido";

            log.info(" [x] Recibido: '{}' con tipo '{}'", message, messageType);

            // Especificar la ruta donde se guardarán los archivos
            String directoryPath = "mensajes/";
            String fileName = directoryPath + messageType + ".txt";

            // Crear el directorio
            File dir = new File(directoryPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Guardar el mensaje en el archivo correspondiente
            try (FileWriter fileWriter = new FileWriter(fileName, true)) {
                fileWriter.write(message + System.lineSeparator());
                log.info("Mensaje guardado en '{}'", fileName);
            } catch (IOException e) {
                log.error("Error al escribir en el archivo '{}': ", fileName, e);
            }


            log.info(" [x] Procesamiento finalizado de '{}'", message);
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}