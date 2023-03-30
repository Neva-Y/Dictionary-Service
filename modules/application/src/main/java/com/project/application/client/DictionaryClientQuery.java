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

public class DictionaryClientQuery {

    // IP and port
    private static final String ip = "localhost";
    private static final int port = 4887;

    public static void main(String[] args) {

        try(Socket socket = new Socket(ip, port);)
        {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DictionaryOperation sendData = new DictionaryOperation("Hello", null, DictionaryOperation.Operation.QUERY);
            output.writeUTF(Codecs.objectMapper.writeValueAsString(sendData));
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();

            while(true)
            {
                if(input.available()>0) {
                    DictionaryEntry entry = Codecs.objectMapper.reader().readValue(input.readUTF(), DictionaryEntry.class);
                    System.out.println("Word is " + entry.word + " with meanings " + Arrays.toString(entry.meanings));

                }
            }

        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
