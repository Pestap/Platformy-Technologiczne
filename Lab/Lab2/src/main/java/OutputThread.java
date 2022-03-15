import java.util.List;
import java.util.Scanner;

public class OutputThread implements Runnable{
    private Output result;
    /**
     *
     * @param result - obiket klasy Numbers - zasób współdzielony z którego pobieramy wyniki obliczeń
     */
    public OutputThread(Output result){
        this.result = result;
    }

    @Override
    /**
     * metoda run - wypisywanie wyników
     */
    public void run(){
        while(true) {
            //try {
                System.out.println();
            //} catch (InterruptedException e) {
              //  System.out.println("OUTPUT FINISHED");
                //break;
            //}
        }
    }
}
