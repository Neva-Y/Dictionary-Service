package com.project.application.client;

import com.project.application.util.Codecs;
import com.project.application.util.Fixtures;
import com.project.repository.dictionary.DictionaryOperation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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
//            DictionaryOperation sendData = new DictionaryOperation("Hello", meanings, DictionaryOperation.Operation.ADD);
            DictionaryOperation sendData = Fixtures.easyRandom.nextObject(DictionaryOperation.class);


            output.writeUTF(Codecs.objectMapper.writeValueAsString(sendData));
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();


            while(true)
            {
                if(input.available()>0) {
                    String message = input.readUTF();
                    System.out.println(message);
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
