import java.util.*;
public class SleepLog {
    private int numDays;
    private double[] numHours;
    private String[][] hours;
    private Scanner s = new Scanner(System.in);
    //This doesn't have to be like this but it was an idea I was having, anyone can make changes.
    public SleepLog(int numDays){
        this.numDays = numDays;
        hours = new String[numDays][2];
        System.out.println("Enter in standard military time. Ex: 17:35");
        for(int k = 0; k<numDays; k++){
            System.out.print("When did you fall asleep " + k + " days ago?");
            hours[k][0] = s.nextLine();
            System.out.print("When did you wake up " + k + " days ago?");
            hours[k][1]= s.nextLine();
        }
        numHours = new double[numDays];
    }
    //number of hours slept each night, using split or string tokenizer we can count hour and minute slept
    public double[] getNumHours() {
        return numHours
    }
    //number of sleep cycles went through in total.
    private int sCycles(){
    int sleepC = 0;
    return sleepC;
    }
    //number of rem cycles went through in total.
    private int rCycle(){
    int remC = 0;
    return remC;
    }
    //number of cicadian cycles went through in total.
    private int cCycle(){
        int circadianC = 0;
        return circadianC;
    }
    // will rate the sleep from that week, use statistics to rate the sleep as good or bad.
    private String rateOfSleep(){
        return "sleep was very long but not deep.";
    }
    // I don't know how to get the nickname from main so I just wrote FIND NAME and this could potentially be where
    // the chart John wanted could go. I'm still working on sleep cycles but I was thinking this could be the general format.
    public String toString(){
        String name = "FIND NAME";
        StringBuilder st = new StringBuilder(name + "'s SleepLog\n");
        for(int k =0; k<numDays; k++){
            int daysago = numDays - k-1;
            string dayTotal = (daysago + " days ago you slept from" + hours[k][0] + " to " + hours[k][1] +".\n");
            st.append(dayTotal);
        }
        st.append(sCycles() + " sleep cycles were completed.\n" + rCycle() + "REM cycles were completed.\n" + cCycle() +
                "Circadian cycles were completed. \n " + name +"'s sleep was " + rateOfSleep() + ".\n");
        return st.toString();

    }
}
