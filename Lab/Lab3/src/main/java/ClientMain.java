import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args){
        try (Socket client = new Socket("localhost", 9797)) {

            try(InputStream is = client.getInputStream()){
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(is);

                Scanner input = new Scanner(System.in);

                boolean serverReady = false;

                while(!serverReady){
                    String ready = (String)in.readObject();
                    if(ready.equals("ready")){
                        serverReady = true;
                    }
                }
                System.out.println("Serwer gotowy. Podaj liczbę wiadomości:");

                int numberOfMessages = Integer.valueOf(input.nextLine());
                out.writeInt(numberOfMessages);
                out.flush();
                System.out.println("Podaj " + numberOfMessages + " wiadomości");

                for(int i =0; i < numberOfMessages; i++){
                    String msg = input.nextLine();
                    Message toSend = new Message(i, msg);
                    out.writeObject(toSend);
                    out.flush();
                }

                System.out.println("Wiadomości zostały wysłane");

                boolean serverFinished = false;

                while(!serverFinished){
                    String finished = (String)in.readObject();
                    if(finished.equals("finished")){
                        serverFinished = true;
                    }
                }
                System.out.println("Serwer przyjął wiadomości");

            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }

    }
}
