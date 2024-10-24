package co.vinni;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String message;
        String messageType;

        log.info("Iniciando Publicador de Mensajes...");

        // INICIO DEL BUCLE
        while (true) {
            System.out.println("Ingrese el mensaje a enviar (o escriba 'salir' para terminar): ");
            message = sc.nextLine();

            if (message.equalsIgnoreCase("salir")) {
                System.out.println("Operación terminada por el usuario.");
                break;
            }

            if (message.isEmpty()) {
                System.out.println("El mensaje no puede estar vacío.");
                continue; // Reiniciar el ciclo
            }

            System.out.println("Ingrese el tipo de mensaje (peticion, queja, recurso, sugerencia): ");
            messageType = sc.nextLine().toLowerCase();

            long delay;

            switch (messageType) {
                case "peticion":
                    delay = 10 * 1000;
                    break;
                case "queja":
                    delay = (long) (20 * 1000);
                    break;
                case "recurso":
                    delay = (long) (30 * 1000); // 1.20 minutos
                    break;
                case "sugerencia":
                    delay = 40 * 1000; // 2 minutos
                    break;
                default:
                    System.out.println("Tipo de mensaje no válido. Debe ser peticion, queja, recurso o sugerencia.");
                    continue; // Reiniciar el ciclo
            }

            try {
                System.out.println("Esperando " + (delay / 1000) + " segundos antes de enviar el mensaje...");
                Thread.sleep(delay);


                Producer.sendMessage(message, messageType);
                System.out.println("Mensaje enviado con éxito.");
            } catch (InterruptedException e) {
                System.out.println("El envío del mensaje fue interrumpido.");
                Thread.currentThread().interrupt();
            } catch (IOException | TimeoutException e) {
                log.error("Error al enviar el mensaje: ", e);
            }
        }

        sc.close();
        log.info("Publicador finalizado.");
    }
}