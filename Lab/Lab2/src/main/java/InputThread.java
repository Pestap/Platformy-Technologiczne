import java.util.List;
import java.util.Scanner;

public class InputThread implements Runnable{
    private Scanner input;
    private Numbers numbers;
    public InputThread(Scanner input, Numbers numbers){
        this.input = input;
        this.numbers = numbers;
    }
    @Override
    public void run(){
        while(true){
            String cmd = input.nextLine();
            if(cmd.equals("quit")){
                numbers.setFinished(); // ustwiamy flage w numbers
                System.out.println("INPUT FINISHED");
                break;
            }else{
                try{
                    int toPut = Integer.parseInt(cmd);
                    numbers.put(Integer.valueOf(cmd));
                }catch (NumberFormatException nfe){
                    System.out.println("Not a number or a valid command!");
                    continue;
                }


            }
        }
    }
}
