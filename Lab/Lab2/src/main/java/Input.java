import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasa reprezentująca liczby, posiada pole wartość oraz pole mówiące czy jest pierwsza
 */
public class Input {
    private LinkedList<Job> jobList;
    private boolean finished;
    public Input(){
        finished = false;
        jobList = new LinkedList<>();
    }
    public synchronized Job take() throws InterruptedException{
        while(jobList.isEmpty()){
            if(jobList.isEmpty() && finished){
                throw new InterruptedException();
            }
            wait();
        }

        Job result = jobList.pop();

        return result;
    }
    public synchronized void put(int id, int precision){
        jobList.add(new Job(precision, id));
        notify();
    }
    public synchronized void setFinished(){
        finished = true;
        notifyAll();
    }
    public boolean getFinished(){
        return finished;
    }
    public boolean isEmpty(){
        return jobList.isEmpty();
    }

}
