import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {

    public static void main(String[] args){

        try {
            ServerSocket serverSocket = new ServerSocket(9797);
            int counter =0;
            ArrayList<Thread> threads = new ArrayList<>();
            while(true) {
                counter++;
                Socket newThreadSocket = serverSocket.accept();
                System.out.println(">> client " + counter + " connected");
                Thread serverThread = new Thread(new ServerThread(newThreadSocket, counter));
                serverThread.start();
                threads.add(serverThread);
                boolean isAnyAlive = false;
                for (Thread t : threads) {
                    if (t.isAlive()) {
                        isAnyAlive = true;
                        break;
                    }
                }
                if (!isAnyAlive) {
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
