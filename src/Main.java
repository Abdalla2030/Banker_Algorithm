package deadlock;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){     
        Deadlock deadlock = new Deadlock();
        Scanner input = new Scanner(System.in);
        System.out.print("Please Enter The Number of Processes: ");
        deadlock.processNum = input.nextInt();
        System.out.print("Please Enter The Number of Resources: ");
        deadlock.resourcesNum = input.nextInt();
        ///////////////////////////////////////////////////////////
        deadlock.inputData(deadlock.processNum,deadlock.resourcesNum,deadlock.maximum,deadlock.allocation);
        boolean checkInSafeState =deadlock.checkIsSafe(deadlock.need,deadlock.maximum,deadlock.allocation,deadlock.currentAvailable,deadlock.finished);
        if(checkInSafeState){
            System.out.println("=========================");
            System.out.println("System Is In Safe State.");
            System.out.println("=========================");
        }
        else{
            System.out.println("=========================");
            System.out.println("System Is In Unsafe State.");
            System.out.println("=========================");
        }
        ////////////////////////////////////////////////////////
        while(true){
            System.out.print("Enter (RQ) or (RL) or (Recover) or (Quit) : ");
            String s = input.next();
            if(s.toLowerCase().equals("rq")){
              System.out.print("Enter The Process Number : ");
              int processRequest = input.nextInt();
              System.out.print("Enter The Resources Request : ");
              int [] request = new int[deadlock.resourcesNum];
              for(int i=0;i<deadlock.resourcesNum;i++){
                  request[i] = input.nextInt();
              }
              if(!deadlock.RQ(processRequest,request,deadlock.need,deadlock.maximum,deadlock.allocation,deadlock.currentAvailable,deadlock.finished)){
                System.out.println("=========================");
                System.out.println("Invalid Request.");
                System.out.println("=========================");
              }
            }
            else if(s.toLowerCase().equals("rl")){
              System.out.print("Enter The Process Number : ");
              int processRequest = input.nextInt();
              System.out.print("Enter The Resources Release : ");
              int [] release = new int[deadlock.resourcesNum];
              for(int i=0;i<deadlock.resourcesNum;i++){
                  release[i] = input.nextInt();
              }
              if(!deadlock.RL(processRequest,release,deadlock.need,deadlock.maximum,deadlock.allocation,deadlock.currentAvailable,deadlock.finished)){
                System.out.println("=========================");
                System.out.println("Invalid Release.");
                System.out.println("=========================");
              }
            }
            else if(s.toLowerCase().equals("recover")){
                if(checkInSafeState==false){
                     deadlock.Recover(deadlock.need,deadlock.maximum,deadlock.allocation,deadlock.currentAvailable,deadlock.finished);
                }else{
                     System.out.println("=========================");
                     System.out.println("Recover Is Applied Only IF The System In Unsafe State.");
                     System.out.println("=========================");
                }
            }
            else{
                break; 
            }
        }
        /////////////////////////////////////////////////////////       
    }    
}