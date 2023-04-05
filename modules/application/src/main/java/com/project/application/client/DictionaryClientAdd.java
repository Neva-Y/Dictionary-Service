package com.project.application.client;

import com.project.application.util.Codecs;
import com.project.repository.dictionary.DictionaryEntry;
import com.project.repository.dictionary.DictionaryOperation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class DictionaryClientAdd {

    // IP and port
    private static final String ip = "localhost";
    private static final int port = 4887;
    public static void main(String[] args) {

        try(Socket socket = new Socket(ip, port);)
        {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String[] meanings = {"Greeting another individual", "Boring statement"};
            DictionaryOperation sendData = new DictionaryOperation("Hello", meanings, DictionaryOperation.Operation.ADD);
            output.writeUTF(Codecs.objectMapper.writeValueAsString(sendData));
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();


            // Wait till the task is completed, making this a per-request thread as thread closes upon
            // receiving a response from the server
            boolean isFutureIncomplete = Boolean.TRUE;
            while (isFutureIncomplete) {
                if (input.available() > 0) {
                    String response = input.readUTF();
                    if (!response.equals(Boolean.TRUE.toString()) && !response.equals(Boolean.FALSE.toString())) {
                        DictionaryEntry entry = Codecs.objectMapper.reader().readValue(response, DictionaryEntry.class);
                        System.out.println("Word is " + entry.word + " with meanings " + Arrays.toString(entry.meanings));
                    }
                    isFutureIncomplete = Boolean.FALSE;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Error, IP Address of the host is invalid");
        } catch (IOException e) {
            System.out.println("Error completing client request");
        }
    }
}
