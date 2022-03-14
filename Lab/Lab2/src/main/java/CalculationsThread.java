
// wątek odpowiedzialny za obliczenia
public class CalculationsThread implements Runnable{
    private Numbers input;
    private Numbers output;

    public CalculationsThread(Numbers input, Numbers output){
        this.input = input;
        this.output = output;
    }
    @Override
    /**
     *
     * sprawdzamy czy liczba jest pierwsza - jezezli tak to wpisujemy do listy wynikow
     */
    public void run(){
        while(true){
            try {
                int value = input.take();
                boolean isPrime = true;
                Thread.sleep(10*value);
                for(int i = 2; i < value; i++){
                    if( (value % i) == 0){
                        isPrime = false;
                        break;
                    }
                }
                if(isPrime){
                    System.out.println("Thread " + Thread.currentThread().getId() + ": number " + value + " is prime!");
                    output.put(value);
                }else{
                    System.out.println("Thread " + Thread.currentThread().getId() + ": number " + value + " is NOT prime!");
                    continue;
                }
            } catch (InterruptedException e) {
                System.out.println("CALCULATIONS FINISHED");
                output.setFinished();
                break;
            }

        }
    }
}
