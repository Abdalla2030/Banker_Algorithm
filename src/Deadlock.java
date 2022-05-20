package deadlock;
import java.util.Scanner;

public class Deadlock {
    public static int processNum = 10 ,resourcesNum =10;
    public int [] maxAvailable = new int[resourcesNum];
    public int [] currentAvailable = new int[resourcesNum];
    public int[][] maximum = new int[processNum][resourcesNum];
    public int[][] allocation = new int[processNum][resourcesNum];
    public int[][] need = new int[processNum][resourcesNum];
    public boolean [] finished = new boolean[processNum];   
    ////////////////////////////////////////////////
    public void inputData(int processNum,int resourcesNum,int[][]maximum,int[][]allocation){
        Scanner input = new Scanner(System.in);
        int n = 0 ; 
        System.out.println("Please Enter The Available Number of Resources: ");
        for(int i=0;i<resourcesNum;i++){
            n = input.nextInt();
            maxAvailable[i] = n;
        }      
        System.out.println("Please Enter The Maximum Number of Resources For Each Process: ");
        for(int i=0;i<processNum;i++){
            for(int j=0;j<resourcesNum;j++) {
                maximum[i][j] = input.nextInt();
            }
        }
        System.out.println("Please Enter The Allocation Number of Resources For Each Process:");
        for(int i=0;i<processNum;i++) {
            for (int j = 0; j < resourcesNum; j++){
                allocation[i][j] = input.nextInt();
            }
        }
       calculateNeed(processNum,resourcesNum,maximum,allocation,need); 
       calculateCurrentAvailable(processNum,resourcesNum,allocation,maxAvailable,currentAvailable);
       assignToFinished(processNum,finished);
    }
    ////////////////////////////////////////////////
    public void calculateNeed(int processNum,int resourcesNum,int[][]maximum, int[][]allocation,int[][]need){        
         for(int i=0;i<processNum;i++) {
            for (int j = 0; j < resourcesNum; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
    }
    ////////////////////////////////////////////////
    public void calculateCurrentAvailable(int processNum,int resourcesNum,int [][]allocation,int[]maxAvailable,int []currentAvailable){
        for(int i = 0 ; i <resourcesNum;i++){
            int sum = 0 ; 
            for(int j=0;j<processNum;j++){
              sum+= allocation[j][i]; 
            }
            currentAvailable[i] = maxAvailable[i] - sum ;
        }
    }
    ////////////////////////////////////////////////
    public void assignToFinished(int processNum,boolean [] finished){
        for(int i=0;i<processNum;i++) {
            finished[i] = false; 
        }
    }
    ////////////////////////////////////////////////
    public boolean availableRequest(int [][]need,int [][]maximum,int []currentAvailable,boolean []finished,int currentP){
        if (finished[currentP]){
            return false ;
        }
        boolean b = true;
        for(int j=0;j<resourcesNum;j++){ 
            if((need[currentP][j] > currentAvailable[j]) || (need[currentP][j] > maximum[currentP][j]) ){
                b = false;
                return b ;
            }
        }
        return b;
    }
    ////////////////////////////////////////////////
    public void addAllocToAvail(int[][]allocation,int processNum,int []currentAvailable){
        for(int j=0;j<currentAvailable.length;j++){
            currentAvailable[j] += allocation[processNum][j];
        }
    }
    ////////////////////////////////////////////////
    public void finishedProcess(int processNum,boolean []finished){
        finished[processNum] = true ; 
    }
    ////////////////////////////////////////////////
    public  boolean allFinished(boolean []finished){
        boolean b = true;
        for(int i=0; i<processNum;i++){
            if(!finished[i]){
                b = false;
                return b;
            }
        }
        return b;
    }
    //////////////////////////////////////////////////
    public boolean checkIsSafe(int [][]need,int [][]maximum,int [][]allocation,int []currentAvailable,boolean []finished){
        boolean b = true;
        String safeStateSequence = "Safe State Sequence: ";
        int count = 0 ; 
        int pNum = 0 ;
        while(true){
            if(availableRequest(need,maximum,currentAvailable,finished,pNum)){
                addAllocToAvail(allocation,pNum,currentAvailable);
                finishedProcess(pNum,finished);
                safeStateSequence += ("P"+pNum+"->");
                count=0;
            }else {
                count++; 
            }
            pNum++;
            if(pNum==processNum){
               pNum = 0 ;
            }
            if(count==processNum){
                b = false; 
                break;
            }
            if(allFinished(finished)){
                // remove last arrow (->)
                safeStateSequence = safeStateSequence.substring(0, safeStateSequence.length() - 1);
                safeStateSequence = safeStateSequence.substring(0, safeStateSequence.length() - 1);
                System.out.println(safeStateSequence);
                System.out.println("=========================");
                b = true ;
                break;
            }
        }
        assignToFinished(processNum,finished);   
       return b ; 
    }
    //////////////////////////////////////////////////
    public boolean RQ(int processRequest,int []request,int [][]need,int [][]maximum,int [][]allocation,int []currentAvailable,boolean []finished){
        boolean b = false ;
        //if (Request > MaximumResourceForEachProcess) --> Return False
        for(int i = 0 ;i<resourcesNum;i++){
            if(request[i]>maximum[processRequest][i]){
                return b; 
            }
        }
        //if (Request > InitialForResourse) --> Return False
        for(int i = 0 ;i<resourcesNum;i++){
            if(request[i]>maxAvailable[i]){
                return b; 
            }
        }
          // if (Request > Need Before Changed) --> Return False
        for(int i = 0 ;i<resourcesNum;i++){
            if(request[i]>need[processRequest][i]){
                return b; 
            }
        }
        // assume we approve the requst --> the allocation and need and currentAvailable will change
        //allocation[processRequest][i] += request[i]
        for(int i = 0 ;i<resourcesNum;i++){
            allocation[processRequest][i] += request[i];
        }
        calculateCurrentAvailable(processNum,resourcesNum,allocation,maxAvailable,currentAvailable);
        // calculate new need 
        calculateNeed(processNum,resourcesNum,maximum,allocation,need);
        ///////////////////////////////////////////////////////////////////
         b = checkIsSafe(need,maximum,allocation,currentAvailable,finished);
        //Invalid Request
         if(!b){    
            for(int i = 0 ;i<resourcesNum;i++){
                allocation[processRequest][i] -= request[i];
            }
            // return old need if invalid request (deadloack will occur)
            calculateNeed(processNum,resourcesNum,maximum,allocation,need);
            // return old currentAvailable if invalid request (deadloack will occur)
            calculateCurrentAvailable(processNum,resourcesNum,allocation,maxAvailable,currentAvailable);
        }else{
            System.out.println("=========================");
            System.out.println("System Is In Safe State.");
            System.out.println("=========================");
         }
        ////////////////////////////////////////////////////////////////////    
         return b ;    
    }
    //////////////////////////////////////////////////
    public boolean RL(int processRelease,int []release,int [][]need,int [][]maximum,int [][]allocation,int []currentAvailable,boolean []finished){
        boolean b = true ;
        // if if release > allocated resources
        // Invalid Release
         for(int i = 0 ;i<resourcesNum;i++){
            if(release[i]>allocation[processRelease][i]){
                b = false ;
                return b; 
            }
        }if(b){
            //allocation[processRequest][i] += request[i]
            for(int i = 0 ;i<resourcesNum;i++){
                 allocation[processRelease][i] -= release[i];
            }
            // update currentAvailable
            calculateCurrentAvailable(processNum,resourcesNum,allocation,maxAvailable,currentAvailable);
            // calculate new need 
            calculateNeed(processNum,resourcesNum,maximum,allocation,need);
            System.out.println("=========================");
            System.out.println("Valid Release.");
            System.out.println("=========================");
            System.out.print("The Current Allocation of Process ( "+processRelease+" ) After Release is : ");
            for(int i = 0 ;i<resourcesNum;i++){
                System.out.print(allocation[processRelease][i]+" ");
            }
             System.out.println("                          ");
            System.out.println("=========================");
        }             
        return b ;       
    }
    //////////////////////////////////////////////////
    // Recover
    // First we Release The All Allocation of P0->P1->P2->etc
    public void Recover(int [][]need,int [][]maximum,int [][]allocation,int []currentAvailable,boolean []finished){
        boolean b = false ;
        int count = 0 ; 
        b = checkIsSafe(need,maximum,allocation,currentAvailable,finished);
        // Recover is applied if the system is in the unsafe state
        if (b==false){
        while(!b && count<processNum){
            int [] releaseResources = new int[resourcesNum];
            for(int i = 0;i<resourcesNum;i++){
                 releaseResources[i] = allocation[count][i];
            }
            b = RL(count,releaseResources,need,maximum,allocation,currentAvailable,finished);
            b = checkIsSafe(need,maximum,allocation,currentAvailable,finished);
            calculateCurrentAvailable(processNum,resourcesNum,allocation,maxAvailable,currentAvailable);
            if(b){
                break ;      
            }else{
                count++; 
            } 
        }
        }
    }

}