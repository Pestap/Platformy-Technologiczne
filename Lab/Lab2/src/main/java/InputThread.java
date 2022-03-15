import java.util.List;
import java.util.Scanner;

public class InputThread implements Runnable{
    private Scanner input;
    private Input jobs;
    private int startId;
    public InputThread(Scanner input, Input jobs, int startId){
        this.input = input;
        this.jobs = jobs;
        this.startId = startId;
    }
    @Override
    public void run(){
        int jobId = startId;
        while(true){
            String cmd = input.nextLine();
            if(cmd.equals("quit")){
                jobs.setFinished(); // ustwiamy flage w numbers
                System.out.println("INPUT FINISHED");
                break;
            }else{
                try{
                    int toPut = Integer.parseInt(cmd);
                    jobs.put(jobId, Integer.valueOf(cmd));
                    jobId++;
                }catch (NumberFormatException nfe){
                    System.out.println("Not a number or a valid command!");
                    continue;
                }


            }
        }
    }
}
