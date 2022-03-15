import java.util.LinkedList;

public class Output {
    private LinkedList<FinishedJob> jobs;
    private boolean finished;
    public Output(){
        jobs = new LinkedList<>();
        finished = false;
    }

    public synchronized void put(FinishedJob job){
        jobs.add(job);
    }

    public void printALlResults(){
        for(FinishedJob f : jobs){
            System.out.println(f);
        }
    }

    public void setFinished(){
        finished = true;
    }
    public boolean getFinished(){
        return finished;
    }

}
