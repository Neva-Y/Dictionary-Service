package com.project.application.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.application.util.Codecs;
import com.project.repository.DictionaryRepository;
import com.project.repository.DictionaryRepositoryImpl;
import com.project.repository.dictionary.DictionaryEntry;
import com.project.repository.dictionary.DictionaryOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages={"com.project"})
@Configuration
public class DictionaryServer {
    private static DictionaryRepositoryImpl dictionaryRepository;
    @Autowired
    public DictionaryServer(DictionaryRepositoryImpl dictionaryRepository) {
        DictionaryServer.dictionaryRepository = dictionaryRepository;
    }

    private static final int port = 4887;
    private static final Logger logger = LoggerFactory.getLogger(DictionaryServer.class);
    public static void main(String[] args) {
        new SpringApplicationBuilder(DictionaryServer.class).run(args);
        logger.info("Application is up!");
        runServer();
    }

    private static void runServer() {
        try {
            logger.info("Starting Server on port " + port);
            // Setup Server Socket to await client connections
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);

            // Setup Thread Pool Executor to handle the client requests
            int corePoolSize = 10;
            int maxPoolSize = 30;
            int keepAdditionalThreadsAliveTime = 1000;
            ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
                    corePoolSize,
                    maxPoolSize,
                    keepAdditionalThreadsAliveTime,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(64));

            while(true) {
                logger.info("Waiting for request");
                try {
                    Socket client = server.accept();
                    logger.info("Client port is: " + client.getPort());
                    logger.info("Client local port is: " + client.getLocalPort());
                    logger.info("Server local port is: " + server.getLocalPort());
                    logger.info("Processing request");
                    threadPoolExecutor.submit(new ServeRequest(client));
                } catch(IOException e) {
                    logger.info("Error accepting connection", e);
                }
            }
        }catch(IOException e) {
            logger.info("Error starting Server on " + port, e);
        }
    }
    static class ServeRequest implements Runnable {
        Socket socket;
        public ServeRequest(Socket connection) {
            this.socket = connection;
        }

        public void run() {

            try (Socket clientSocket = socket) {

                logger.info("Serving client request on Thread {}", Thread.currentThread().getName());

                //Do your logic here. You have the `socket` available to read/write data.
                // Input stream
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                // Output Stream
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

                DictionaryOperation clientRequest = Codecs.objectMapper.reader().readValue(input.readUTF(), DictionaryOperation.class);

                if (clientRequest.word == null) {
                    logger.error("No word provided from client");
                    throw new IllegalArgumentException("Unexpected word");
                }
                switch (clientRequest.operation) {
                    case ADD:
                        if (clientRequest.meanings.length > 0) {
                            Boolean flag = dictionaryRepository.insertWord(clientRequest.word, clientRequest.meanings);
                            if (flag) {
                                logger.info("Inserted {} into the dictionary", clientRequest.word);
                            } else {
                                logger.info("Could not put {} into dicitonary", clientRequest.word);
                            }
                        } else {
                            logger.error("No meanings provided to add into dictionary");
                            throw new IllegalArgumentException("Missing meanings");
                        }
                        break;
                    case QUERY:
                        try {
                            DictionaryEntry entry = dictionaryRepository.queryWord(clientRequest.word);
                            output.writeUTF(Codecs.objectMapper.writer().writeValueAsString(entry));
                            break;
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    case REMOVE:
                        dictionaryRepository.removeWord(clientRequest.word);
                        break;
                    case UPDATE:
                        if (clientRequest.meanings.length > 0) {
                            dictionaryRepository.updateWord(clientRequest.word, clientRequest.meanings);
                        } else {
                            logger.error("No meanings provided to add into dictionary");
                            throw new IllegalArgumentException("Missing meanings");
                        }
                        break;
                    default:
                        logger.error("Unexpected operation requested from client {}", clientRequest.operation);
                        throw new IllegalArgumentException("Unexpected operation");
                }

                // Close thread after finishing task
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.info("Error closing client connection", e);
                }
            } catch (IOException e) {
                logger.info("Error creating client connection", e);
            }
        }
    }
}
