/**
 * SleepLog
 * This class allows the user to create a sleep log that contains a user's hours of sleep,
 * the number of sleep cycles they went through, how good of a circadian rhythm they had,
 * and also how their rate of sleep was according to the sleep cycles.
 *
 *
 * SLEEPLOGS can currently be created, but cannot be serialized (uploaded) to Firebase.
 * This is a known bug and seems to be a limitation with compatibility between the
 * datatypes in use in the sleeplog class.
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.*;
public class SleepLog {
    private int numDays;
    private double[] numHours = null;
    private String stringHours;
    private Integer[][] hours;

    /**
     * SleepLog
     * This constructor allows the user to input the number of days they'd like to record,
     * and stores them in an array. It also links to the calcNumHours method so that the number
     * of hours they slept is also stored in an array. It also does data validation for all input.
     */
    public int getNumDays(){
        return numDays;
    }

    public String getStringHours(){
        return stringHours;
    }


    public SleepLog(String tag){
        Scanner s = new Scanner(System.in);
        
        boolean numberD = false;
        while(!numberD) {
            try {
                System.out.print("How many nights would you like to enter? ");
                numDays = Integer.parseInt(s.nextLine());
                numberD = true;

            }catch(NumberFormatException e){
                System.out.println("Enter in an integer.");
            }
        }
		
        hours = new Integer[numDays][2];
        System.out.println("Enter in standard military time. Ex: 5:35 PM = 1735.");
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
					if(hours[k][0]<0 || hours[k][0]>2400){
                        throw new NumberFormatException();
                    }
                    wake = true;
                }catch(NumberFormatException e){
                    System.out.println("Error: Enter proper military time.");
                }
            }
        }

        stringHours = calcNumHours();

    }

    /**
     * calcNumHours
     * This method calculates how many hours a user slept each night. It is then returned
     * to the constructor to initialize the numHours array.
     */
    private String calcNumHours() {
        String stringHours = "";
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
            
            stringHours += "" + sum + "-";
        }
        return stringHours;
    }

    /**
     * sCycles
     * This method calculates how many sleep cycles were achieved in total for all nights.
     */
    private int sCycles(){
        if(numHours == null) {
            String[] temp = stringHours.split("-");
            numHours = new double[temp.length];
            for(int i = 0; i < temp.length; i++) numHours[i] = Double.parseDouble(temp[i]);
        }
        int sleepC = 0;
        for (int k =0; k<numDays; k++){
            sleepC += (numHours[k] * 60)/90;
        }
        return sleepC;
    }

    /**
     * cCycle
     * This method checks how close each sleep time and wake time were on every night.
     * By calculating this, it checks to see if there is any kind of rhythm. The rhythm is
     * then averaged out and shown as a percentage.
     */
    private String cCycle(){
        int[] difference = {0,0};
		double cperc;
        for (int k = 0; k<numDays; k++){
			for(int a =0; a<numDays; a++){
				for(int i = 0; i<2; i++) {
					int hour1 = (hours[k][i]) / 100;
					int hour2 = (hours[a][i]) / 100;
					if(hour1>12 && hour2>12 || hour1<12 && hour2<12){
						difference[i] += Math.abs(hour1-hour2);
						
					}else if(hour1>12){
						difference[i] += Math.abs(24-hour1) + hour2;
					}else{
						difference[i] += Math.abs(24-hour2) + hour1;
					}
				}
			}
        }
        DecimalFormat df = new DecimalFormat("##.#");
        double recommended = numDays*2.0*numDays;
        cperc =Math.abs(100- (((difference[0]/recommended) + (difference[1]/recommended))/2.0)*100);

        df.format(cperc);
        return cperc +"%";

    }

    /**
     * rateOfSleep
     * This method uses the sCycles to check if enough sleep cycles were made. Then rates
     * the sleep as too little, enough, or not enough.
     */
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
    /**
     *toString
     *This method shows sleep cycles and circadian rhythm. It is formatted so that it is in similar
     *style to the Post object.
     */
    public String toString(String nickname){
        String result = (" _________________________________________________\n" +
                "| You slept for " + sCycles() + " sleep cycles in " + numDays + " days"+ "\t\t\t\t\t  |"+
                "\n| Circadian rhythm synchronization  "+ cCycle()+"\t\t\t\t  |");
        for(int i = 0; i<numHours.length; i++) {
            if(numHours[i]>=10) {
                result += "\n| Slept " + numHours[i] + " hours on night " + (i + 1)
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
