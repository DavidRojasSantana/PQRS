package co.vinni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Iniciando Suscriptor");


        try {
            Receiver.startWorker();
        } catch (IOException | TimeoutException e) {
            log.error("Error al recibir los mensajes: ", e);
        }
    }
}
