import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerMain {

    public static void main(String[] args){
        try(ServerSocket server = new ServerSocket(9797)){
            try(Socket socket = server.accept()){
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

                ArrayList<Message> msgs = new ArrayList<>();
                os.writeObject("ready");

                int numberOfMessages = 0;
                boolean countSend = false;
                int temp;
                while((temp = is.read()) != -1) {
                    if(temp != -1){
                        numberOfMessages = temp;
                    }
                }

                System.out.println("Liczba wiadomości: " + numberOfMessages);

                for(int i =0; i < numberOfMessages; i++){
                    Message msg = (Message) is.readObject();
                    msgs.add(msg);
                }
                System.out.println(msgs);
                Thread.sleep(1000);
                os.writeObject("finished");
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }finally{

        }
    }
}
