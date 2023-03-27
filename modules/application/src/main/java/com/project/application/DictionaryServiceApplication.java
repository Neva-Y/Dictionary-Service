package com.project.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
@Configuration
public class DictionaryServiceApplication {
    private static final String ip = "localhost";
    private static final int port = 4887;
    private static int counter = 0;
    private static final Logger logger = LoggerFactory.getLogger(DictionaryServiceApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(DictionaryServiceApplication.class).run(args);
        logger.info("Application is up!");

        try {
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);
            logger.info("Waiting for client connection-");

            while(true) {
                Socket client = server.accept();
                counter++;
                logger.info("Client "+counter+": Applying for connection!");
                logger.info("Client port is: " + client.getPort());
                logger.info("Client local port is: " + client.getLocalPort());
                logger.info("Server local port is: " + server.getLocalPort());
//				System.out.println(server.getPort());
                // Start a new thread for a connection
                Thread t = new Thread(() -> serveClient(client));
                t.start();
            }
        } catch (IOException e) {
            logger.error("IO Exception occurred", e);
        }
    }

    private static void serveClient(Socket client)
    {
        try(Socket clientSocket = client)
        {
            // Input stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("CLIENT: "+input.readUTF());

            output.writeUTF("Server: Hi Client "+counter+" !!!");
        }
        catch (IOException e)
        {
            logger.error("IO Exception occurred", e);
        }
    }
}
