
// wątek odpowiedzialny za obliczenia
public class CalculationsThread implements Runnable{
    private Input input;
    private Output output;

    public CalculationsThread(Input input, Output output){
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
                Job toDo = input.take();
                int precision = toDo.getPrecision();
                int jobId = toDo.getJobId();
                double result =0;
                int iterations = 1;
                double error = 0;
                while(!input.getFinished()){

                    result +=  Math.pow(-1, iterations -1)/(2*iterations-1);

                    error = Math.abs(Math.PI - 4*result);
                    if(error < Math.pow(10, -precision-1)){
                        break;
                    }
                    iterations++;
                }
                if(input.getFinished()){
                    throw new InterruptedException();
                }
                result *= 4;
                System.out.println("Wątek " + Thread.currentThread().getId() + " zakończył obliczenia");
                output.put(new FinishedJob(jobId, precision, result, error, iterations));

            } catch (InterruptedException e) {
                System.out.println("CALCULATIONS FINISHED");
                output.setFinished();
                //output.printALlResults();
                break;
            }

        }
    }
}
