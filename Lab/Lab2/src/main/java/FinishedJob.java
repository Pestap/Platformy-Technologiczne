public class FinishedJob {
    private int jobId;
    private int precision;
    private double piValue;
    private double error;
    private int iterations;


    public FinishedJob(int jobId, int precision, double piValue, double error, int iterations){
        this.error = error;
        this.iterations = iterations;
        this.jobId = jobId;
        this.piValue = piValue;
        this.precision = precision;
    }
    @Override
    public String toString(){
        return "Zadanie: " + jobId + ", precyzja: " + precision + ", wartość: " + piValue + " błąd: " + error + " liczba iteracji: " + iterations;
    }
}
