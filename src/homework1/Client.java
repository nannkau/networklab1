package homework1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    BufferedReader stdIn = null;

    public Client(String address, int port) throws UnknownHostException, IOException
    {
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.print("Client input: ");
            String line = stdIn.readLine();
            out.write(line);
            out.newLine();
            out.flush();
            if(line.equals("bye"))
                break;
            String data = in.readLine();
            System.out.println("===>" + data);
        }
        System.out.println("Client closed connection");
        in.close();
        out.close();
        stdIn.close();
        socket.close();
    }
    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Client client = new Client("localhost", 6000);
    }

}