// program główny

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        /**
         * maksymalna libcza wątków (semafor)
         */
        int numberOfCalculationThreads = 10;
        if(args.length > 0) {
            numberOfCalculationThreads = Integer.valueOf(args[0]);
        }

        /**
         * synchrozniowana lista liczb do sprawdzenia
         */
        Numbers toTake = new Numbers();
        Numbers result = new Numbers();
        /**
         *  inicjalizacja obiektu Scanner
         */
        Scanner input = new Scanner(System.in);

        /**
         * rozpoczęcie nowego wątku odpowiedzialnego za dodawanie nowych liczb
         */

        Thread inputThread = new Thread(new InputThread(input, toTake));
        Thread outputThread = new Thread(new OutputThread(result));

        List<Thread> threadList = new ArrayList<>();
        for(int i =0; i < numberOfCalculationThreads;i++){
            threadList.add(new Thread(new CalculationsThread(toTake, result)));
        }

        inputThread.start();
        outputThread.start();
        for(Thread t : threadList){
            t.start();
        }

        try {
            inputThread.join();
            outputThread.join();
            for(Thread t : threadList){
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
