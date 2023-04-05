package com.project.application.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.application.common.ServerError;
import com.project.application.util.Codecs;
import com.project.repository.DictionaryRepositoryImpl;
import com.project.repository.dictionary.DictionaryEntry;
import com.project.repository.dictionary.DictionaryOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.*;

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
        if (args.length == 1) {
            if (dictionaryRepository.initialiseDictionary(args[0])) {
                logger.info("Successfully initialised dictionary!");
            } else {
                logger.info("Could not use the dictionary file provided for initialisation");
            }
        } else {
            logger.info("No dictionary file provided for initialisation");
        }
        runServer();
    }

    private static void runServer() {
        try {
            logger.info("Starting Server on port " + port);
            // Setup Server Socket to await client connections
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);

            // Setup Thread Pool Executor to handle the client requests
            int corePoolSize = 30;
            int maxPoolSize = 50;
            int keepAdditionalThreadsAliveTime = 5000;
            ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
                    corePoolSize,
                    maxPoolSize,
                    keepAdditionalThreadsAliveTime,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(512));

            while(true) {
                logger.info("Waiting for request");
                try {
                    Socket client = server.accept();
                    logger.info("Client port is: " + client.getPort());
                    logger.info("Server port is: " + server.getLocalPort());
                    logger.info("Processing request");
                    threadPoolExecutor.submit(new ServeRequest(client));
                } catch(IOException e) {
                    logger.error("Error accepting connection", e);
                }
            }
        }catch(IOException e) {
            logger.error("Error starting Server on " + port, e);
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
                // Input stream
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                // Output Stream
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

                DictionaryOperation clientRequest = Codecs.objectMapper.reader().readValue(input.readUTF(), DictionaryOperation.class);

                String [] emptyMeaning = {""};

                if (clientRequest.word.isBlank()) {
                    logger.error("No word provided from client");
                    output.writeUTF(ServerError.ErrorCodes.WORD_NOT_PROVIDED.toString());
                } else {
                    switch (clientRequest.operation) {
                        case ADD:
                            if (!Arrays.equals(clientRequest.meanings, emptyMeaning)) {
                                Boolean addFlag = dictionaryRepository.insertWord(clientRequest.word, clientRequest.meanings);
                                if (addFlag) {
                                    logger.info("Inserted {} into the dictionary with meanings {}", clientRequest.word, clientRequest.meanings);
                                    output.writeUTF(Boolean.TRUE.toString());
                                } else {
                                    logger.info("Word {} already exists in the dictionary", clientRequest.word);
                                    output.writeUTF(ServerError.ErrorCodes.WORD_ALREADY_EXISTS.toString());
                                }
                            } else {
                                logger.error("No meanings provided to add into dictionary");
                                output.writeUTF(ServerError.ErrorCodes.MEANING_NOT_PROVIDED.toString());
                                break;
                            }
                            break;
                        case QUERY:
                            try {
                                DictionaryEntry entry = dictionaryRepository.queryWord(clientRequest.word);
                                if (entry == null) {
                                    logger.info("Word {} does not exist in the dictionary", clientRequest.word);
                                    output.writeUTF(ServerError.ErrorCodes.WORD_NOT_FOUND.toString());
                                    break;
                                }
                                logger.info("Query for word {} successfully returned meaning {}", clientRequest.word, entry.meanings);
                                output.writeUTF(Codecs.objectMapper.writer().writeValueAsString(entry));
                                break;
                            } catch (JsonProcessingException e) {
                                logger.error("Unable to deserialize to a Dictionary Entry");
                                break;
                            }
                        case REMOVE:
                            Boolean removeFlag = dictionaryRepository.removeWord(clientRequest.word);
                            if (removeFlag) {
                                logger.info("Word {} successfully removed from the dictionary", clientRequest.word);
                                output.writeUTF(Boolean.TRUE.toString());
                            } else {
                                logger.info("Word {} does not exist in the dictionary", clientRequest.word);
                                output.writeUTF(ServerError.ErrorCodes.WORD_NOT_FOUND.toString());
                            }
                            break;
                        case UPDATE:
                            if (!Arrays.equals(clientRequest.meanings, emptyMeaning)) {
                                Boolean updateFlag = dictionaryRepository.updateWord(clientRequest.word, clientRequest.meanings);
                                if (updateFlag) {
                                    logger.info("Successfully updated word {} with meanings {}", clientRequest.word, clientRequest.meanings);
                                    output.writeUTF(Boolean.TRUE.toString());
                                } else {
                                    logger.info("Word {} does not exist in the dictionary", clientRequest.word);
                                    output.writeUTF(ServerError.ErrorCodes.WORD_NOT_FOUND.toString());
                                }
                            } else {
                                logger.error("No meanings for word {} provided to add into dictionary", clientRequest.word);
                                output.writeUTF(ServerError.ErrorCodes.MEANING_NOT_PROVIDED.toString());
                                break;
                            }
                            break;
                        default:
                            logger.error("Unexpected operation requested from client {}", clientRequest.operation);
                            output.writeUTF(ServerError.ErrorCodes.UNEXPECTED_OPERATION.toString());
                            break;
                    }
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
