// program główny

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        /**
         * maksymalna libcza wątków (semafor)
         */
        int numberOfCalculationThreads = 5;
        if(args.length > 0) {
            numberOfCalculationThreads = Integer.valueOf(args[0]);
        }

        /**
         * synchrozniowana lista liczb do sprawdzenia
         */
        Input jobs = new Input();
        Output out = new Output();
        /**
         *  inicjalizacja obiektu Scanner
         */
        Scanner input = new Scanner(System.in);

        /**
         * rozpoczęcie nowego wątku odpowiedzialnego za dodawanie nowych liczb
         */


        jobs.put(0, Integer.valueOf(1));
        jobs.put(1, 10);

        Thread inputThread = new Thread(new InputThread(input, jobs, 2));


        List<Thread> threadList = new ArrayList<>();

        for(int i =0 ; i< numberOfCalculationThreads ; i++){
            threadList.add(new Thread((new CalculationsThread(jobs, out))));
        }

        inputThread.start();

        for(Thread t : threadList){
            t.start();
        }
        try {
            inputThread.join();
            for(Thread t : threadList){
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out.printALlResults();
    }
}
