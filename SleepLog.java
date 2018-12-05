/**
 * SleepLog
 * This class allows the user to create a sleep log that contains a user's hours of sleep,
 * the number of sleep cycles they went through, how good of a circadian rhythm they had,
 * and also how their rate of sleep was according to the sleep cycles.
 *
 *
 */

import java.text.DecimalFormat;
import java.util.*;
public class SleepLog {
    private int numDays;
    private double[] numHours = null;
    private String stringHours;
    private String stringHoursArr;
    private Integer[][] hours = null;

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

    public String getStringHoursArr(){
        return stringHoursArr;
    }


    public SleepLog(String tag){
        Scanner s = new Scanner(System.in);
        boolean numberD = false;
        while(!numberD) {
            try {
                System.out.print("\n   > How many nights would you like to enter? ");
                numDays = Integer.parseInt(s.nextLine());
                numberD = true;

            }catch(NumberFormatException e){
                System.out.println("    > zEnter in an integer.");
            }
        }
        hours = new Integer[numDays][2];
        System.out.println("\n   > Enter in standard military time. Ex: 5:35 PM = 1735.");
        for(int k = 0; k<numDays; k++){
            boolean sleep = false;
            boolean wake = false;
            while(!sleep) {
                try {
                    System.out.print("   > Night " + (k+1) + " bedtime: ");
                    hours[k][0] = Integer.parseInt(s.nextLine());
                    if(hours[k][0]<0 || hours[k][0]>2400){
                        throw new NumberFormatException();
                    }
                    sleep = true;
                }catch(NumberFormatException e){
                    System.out.println("    > Error: Enter proper military time (0000-2400).");
                }
            }
            while(!wake) {
                try {
                    System.out.print("   > Night " + (k+1) + " wake time: ");
                    hours[k][1] = Integer.parseInt(s.nextLine());
                    wake = true;
                }catch(NumberFormatException e){
                    System.out.println("Error: Enter proper military time (0000-2400).");
                }
            }
        }
        stringHours = calcNumHours();
        stringHoursArr = calcStringHoursArr();

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

    private String calcStringHoursArr(){
        stringHoursArr = "";
        for(Integer[] row : hours){
            for(Integer col : row){
                stringHoursArr += col.toString() + "-";
            }
            stringHoursArr += "/";
        }
        return stringHoursArr;
    }

    private void fromStringHoursArr(){
        hours = new Integer[numDays][2];
        String[] rows = stringHoursArr.split("/");

        for(int row = 0; row < hours.length; row++){
            for(int col = 0; col < hours[0].length; col++){
                hours[row][col] = Integer.parseInt(rows[row].split("-")[col]);
            }
        }
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
        if(hours == null) fromStringHoursArr();
        if(numDays == 1) return 100 + "%";
        int difference = 0;
        double cperc;
        for (int k = 0; k<numDays; k++){
            for(int a = 0; a<numDays; a++) {
                for (int i = 0; i < 2; i++) {
                    int hour1 = (hours[k][i]) / 100;
                    int hour2 = (hours[a][i]) / 100;
                    if(hour1<(hour2+2) && hour1>(hour2-2)) {
                        difference += 1;
                    }else if(hour1<(hour2+4) && hour1>(hour2-4)){
                        difference +=2;
                    }else{
                        difference +=4;
                    }
                }
            }
        }
        DecimalFormat df = new DecimalFormat("##.00");
        int rec = numDays*numDays;
        int total = (difference - rec)*2;
        if(total>0){
            cperc = 100-total;
        }else{
            cperc = 100-(Math.abs(total));
        }
        df.format(cperc);
        return cperc +"%";
    }


    private String toHoursMin(double d){
        String s = "";
        s += (int)d + "h ";
        s += (int)((d - (int)d)*60) + "m";

        return s;
    }
    /**
     *toString
     *This method shows sleep cycles and circadian rhythm. It is formatted so that it is in similar
     *style to the Post object.
     */
    public String toString(String nickname){
        DecimalFormat df = new DecimalFormat("##.#");
        sCycles();
        double totalHours = 0;
        for(int i = 0; i < numHours.length; i++) totalHours += numHours[i];
        totalHours /= numDays;

        String result = " _________________________________________________\n" + String.format("%-1s %49s", "|", "|") + "\n" +
                String.format("%-30s %21s", "| " + nickname + "'s Sleep.log", "  |\n") + String.format("%-1s %49s", "|", "|") + "\n" +
                String.format("%-40s %10s", "| Average sleep per night: " + toHoursMin(totalHours), "|") + "\n" +
                String.format("%-40s %10s", "| Average sleep cycles per night: " + df.format(sCycles()/(double)numDays), "|")
        + "\n" +
                String.format("%-40s %10s", "| Circadian rhythm stability: "+ cCycle(), "|") + "\n" + String.format("%-1s %49s", "|", "|");
        for(int i = 0; i<numHours.length; i++)
                result += "\n" + String.format("%-30s %20s", "| Night " + (i + 1) + ": " + toHoursMin(numHours[i]) + " slept", "|");


        result +=  "\n|_________________________________________________|";
        return result;
    }


}