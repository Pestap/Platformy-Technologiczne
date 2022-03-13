import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa reprezentująca liczby, posiada pole wartość oraz pole mówiące czy jest pierwsza
 */
public class Numbers {
    private List<Integer> numberList;
    private boolean finished;
    public Numbers(){
        numberList = new ArrayList<>();
        numberList = Collections.synchronizedList(numberList);
        finished = false;
        numberList.add(100);
        numberList.add(101);
        numberList.add(102);
    }
    public synchronized int take() throws InterruptedException{
        while(numberList.isEmpty()){
            if(numberList.isEmpty() && finished){
                throw new InterruptedException();
            }
            wait();
        }

        int result = numberList.get(0);
        numberList.remove(0);

        return result;
    }
    public synchronized void put(int value){
        numberList.add(value);
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
        return numberList.isEmpty();
    }

}
