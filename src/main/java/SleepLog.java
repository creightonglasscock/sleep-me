import java.util.*;
import java.text.*;
public class SleepLog {
    private int numDays;
    private double[] numHours;
    private int[][] hours;

    //This doesn't have to be like this but it was an idea I was having, anyone can make changes.
    //Maybe make it so that the user must input a number between 0 and 2400.
    public SleepLog(int numDays){
        Scanner s = new Scanner(System.in);
        this.numDays = numDays;
        hours = new int[numDays][2];
        System.out.println("Enter in standard military time. Ex: 1735");
        for(int k = 0; k<numDays; k++){
            boolean sleep = false;
            boolean wake = false;
            while(!sleep) {
                try {
                    System.out.print("On night " + (k+1) + " I fell asleep at: ");
                    hours[k][0] = Integer.parseInt(s.nextLine());
                    if(hours[k][0]<0 || hours[k][0]>2400){
                        throw new NumberFormatException();
                    }
                    sleep = true;
                }catch(NumberFormatException e){
                    System.out.println("Error: Enter proper military time(0000-2400).");
                }
            }
            while(!wake) {
                try {
                    System.out.print("On night " + (k+1) + " I woke up at: ");
                    hours[k][1] = Integer.parseInt(s.nextLine());
                    wake = true;
                }catch(NumberFormatException e){
                    System.out.println("Error: Enter proper military time.");
                }
            }
        }
        numHours = getNumHours();
    }
    //number of hours slept each night put into an array.
    private double[] getNumHours() {
        numHours = new double[numDays];
        DecimalFormat df = new DecimalFormat("##.##");
        for(int k =0; k<numDays; k++){
            double sum = 0;
            int shour = (hours[k][0])/100;
            int sminute = (hours[k][0])%100;
            int whour = (hours[k][1])/100;
            int wminute = (hours[k][1])%100;
            if(shour>whour){
                sum+=(24-shour)+whour;
            }else{
                sum += (whour-shour);
            }
            if(sminute>wminute){
                sum += ((60-sminute) + wminute)/60.0;
            }

            numHours[k] = sum;
        }
        return numHours;
    }
    //number of sleep cycles went through in total.
    private int sCycles(){
        int sleepC = 0;
        for (int k =0; k<numDays; k++){
            sleepC += (numHours[k] * 60)/90;
        }
        return sleepC;
    }

    //how good their circadian rhythms were.
    private String cCycle(){
        int[] difference = {0,0};

        for (int k = 0; k<numDays-1; k++){
            for(int i = 0; i<2; i++) {
                int hour1 = (hours[k][i]) / 100;
                int hour2 = (hours[k + 1][i]) / 100;
                if(hour1>12 && hour2>12 || hour1<12 && hour2<12){
                    difference[i] += Math.abs(hour1-hour2);
                }else if(hour1>12){
                    difference[i] += Math.abs(24-hour1) + hour2;
                }else{
                    difference[i] += Math.abs(24-hour2) + hour1;
                }
            }
        }
        double recommended = numDays*2.0;
        double cperc = 100-(((difference[0]/recommended) + (difference[1]/recommended))/2.0)*100;
        DecimalFormat df = new DecimalFormat("##.#");
        df.format(cperc);
        if(cperc<=25){
            return cperc +"%";
        }else if (cperc<=50){
            return cperc+"%";
        }else if (cperc<=75){
            return cperc +"%";
        }else{
            return cperc +"%";
        }
    }
    // will rate the sleep from that week, use statistics to rate the sleep as good or bad.
    private String rateOfSleep(){
        int recommended = numDays*5;
        if(sCycles()<recommended){
            return "Not enough sleep cycles. Try sleeping a bit more.";
        }else if (sCycles() == recommended){
            return "Perfect amount of sleep cycles!";
        }else{
            return "More sleep cycles than recommended. Try sleeping a bit less.";
        }

    }
    // This could potentially be where the chart John wanted could go. Otherwise, John go wild on how you want to
    //display the info. Has to include the sCycle, cCycle, and rateOfSleep method plus the numHours has the hours
    //they slept written as 7.4 or 4.8 so if you want to change it to h hours and m minutes you can.

     public String toString(){
        String result = (" _________________________________________________\n" +
                        "| You slept " + sCycles() + " Cyles in " + numDays + " days"+ "\t\t\t\t\t  |"+
                        "\n| Your Circadian rhythm was "+ cCycle()+"\t\t\t\t  |");
        for(int i = 0; i<numHours.length; i++) {
            if(numHours[i]>=10) {
                result += "\n| Slept " + numHours[i] + " night " + (i + 1)
                        + String.format("%31s", "|");
            }else{
                result += "\n| Slept " + numHours[i] + " night " + (i + 1)
                        + String.format("%32.5s", "|");
            }
        }
        result +=  "\n|_________________________________________________|";
        return result;


    }
    
    

}
