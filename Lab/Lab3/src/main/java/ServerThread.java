import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread implements Runnable {
    private Socket socket;
    private int clientNumber;
    public ServerThread(Socket socket, int clientNumber){
        this.socket = socket;
        this.clientNumber = clientNumber;
    }
    @Override
    public void run(){
        try(ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            ArrayList<Message> messages = new ArrayList<>();

            out.writeObject("ready");
            out.flush();
            System.out.println("Server ready");
            int numberOfMessages = 0;
            int temp;
            while((temp = in.read()) != -1){
                if(temp != -1){
                    numberOfMessages = temp;
                }
            }

            System.out.println(">> client "+clientNumber +" - Liczba wiadmości: "+ numberOfMessages);


            for(int i =0 ; i < numberOfMessages; i++){
                Message msg = (Message)in.readObject();
                messages.add(msg);
            }
            System.out.println(">> client " + clientNumber + " messages:" + messages);
            Thread.sleep(1000);
            out.writeObject("finished");
            out.flush();
            System.out.println(">> client " + clientNumber + " finished");
            socket.close();

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket(){
        return socket;
    }
    public void setSocket(Socket socket){
        this.socket = socket;
    }
    public int getClientNumber(){
        return clientNumber;
    }
    public void setClientNumber(int clientNumber){
        this.clientNumber = clientNumber;
    }

}
