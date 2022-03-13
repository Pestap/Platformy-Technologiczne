import java.util.List;
import java.util.Scanner;

public class OutputThread implements Runnable{
    private Numbers numbers;
    /**
     *
     * @param toTake - obiket klasy Numbers - zasób współdzielony z którego pobieramy wyniki obliczeń
     */
    public OutputThread(Numbers toTake){
        this.numbers = toTake;
    }

    @Override
    /**
     * metoda run - wypisywanie wyników
     */
    public void run(){
        while(true) {
            try {
                System.out.println(numbers.take());
            } catch (InterruptedException e) {
                System.out.println("OUTPUT FINISHED");
                break;
            }
        }
    }
}
