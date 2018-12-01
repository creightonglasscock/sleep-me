/**
 * Post
 * This class creates the object of Post. It connects to the online firebase. The post
 * contains a user's name and when they posted it in connection to current time.
 */

import org.joda.time.Minutes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Post {

    protected long epoch;
    private String name, body, timestamp;
    private SleepLog log;

    /**
     * Post
     * The constructor accepts the users name and post body and initializes the variables.
     */
    public Post(String name, String body){
        this(name, body, null);
    }

    /**
     * Post
     * This constructor accepts a the users name, post body, and a SleepLong object.
     */
    public Post(String name, String body, SleepLog log){
        this(System.currentTimeMillis() / 1000L, name, body, log,
                DateTimeFormatter.ofPattern("MMM dd DD yyyy h:mm a ").format(LocalDateTime.now()) + LocalDateTime.now().getHour()*60+LocalDateTime.now().getMinute());
    }

    public Post(long epoch, String name, String body, SleepLog log, String timestamp){
        this.epoch = epoch;
        this.name = name;
        this.body = body;
        this.log = log;
        this.timestamp = timestamp;
    }

    /**
     * getName
     * This method is to be able to return the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     *getBody
     * This method is to be able to return the post's message.
     */
    public String getBody() {
        return body;
    }

    /**
     *getTimestamp
     * This method is to be able to see when the post was made.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * getLog
     * This method is to be able to see the SleepLog and accesses the SleepLog class.
     */
    public SleepLog getLog(){
        return log;
    }

    /**
     *daysAgo
     * This method allows the time stamp to work in accordance to current time. Using a
     * package called Joda Time. It is able to calculate if it was made today, yesterday, or
     * any day of the week before yesterday.
     */
    private String daysAgo(String currentDay, int daysAgo){
        if(daysAgo == 0) return "Today";
        if(daysAgo == 1) return "Yesterday";
        if(daysAgo >= 7) return null;



        String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int dayNum = 0;
        for(int i = 0; i < days.length; i++) if(currentDay.equalsIgnoreCase(days[i])) dayNum = i;
        while(daysAgo > 0){
            if(dayNum == 0) dayNum = 6;
            else dayNum--;
            daysAgo--;
        }

        return days[dayNum];
    }

    /**
     * trimBody
     * This method formats the post to a general box of 49 character in length. The
     * strings are made into shorter strings.
     */
    private ArrayList<String> trimBody(){
        if(body.length() < 43) return new ArrayList(Arrays.asList(body));

        String tempBody = body;
        ArrayList<String> lines = new ArrayList();
        String line = "";
        String[] words = body.split(" ");
        int len = 0;

        while(!(tempBody.equals(""))) {
            for (int i = 0; i < words.length; i++) {
                if (len + words[i].length() <= 43) {
                    len += words[i].length() + 1;
                    line += words[i] + " ";
                } else break;
            }
            lines.add(line);
            if(tempBody.indexOf(line) == -1) break;
            else{
                try {
                    tempBody = tempBody.split(line)[1];
                }catch(ArrayIndexOutOfBoundsException a){
                    // This was the last line.
                }
            }
            words = tempBody.split(" ");
            len = 0;
            line = "";
        }

        return lines;



    }

    /**
     * toString
     * This method allows the post to be made visible in a formatted way. Using the previosuly
     * formatted strings it is able to fit inside a set space with the user's name at the top
     * and the time stamp in the corner. The width is set however, the height of the post is not set.
     */
    public String toString(){
        LocalDateTime now = LocalDateTime.now();
        String[] currTimestamp = (DateTimeFormatter.ofPattern("MMM dd DD yyyy h:mm a ").format(now) +
                LocalDateTime.now().getHour()*60+LocalDateTime.now().getMinute()).split(" ");
        String currDate = currTimestamp[0] + " " + currTimestamp[1];
        String currDay = currTimestamp[2];
        String currYear = currTimestamp[3];
        String currTime = currTimestamp[4] + " " + currTimestamp[5];
        String currMin = currTimestamp[6];
        String currDayOfWeek = now.getDayOfWeek().toString();

        String[] postTimestamp = timestamp.split(" ");
        String postDate = postTimestamp[0] + " " + postTimestamp[1];
        String postDay = postTimestamp[2];
        String postYear = postTimestamp[3];
        String postTime = postTimestamp[4] + " " + postTimestamp[5];
        String postMin = postTimestamp[6];

        int minDiff = Integer.parseInt(currMin) - Integer.parseInt(postMin);
        String tempTimestamp;



        int dayDiff = Integer.parseInt(currDay) - Integer.parseInt(postDay);
        String day = daysAgo(currDayOfWeek, dayDiff);
        if(day == null || !(currYear.equals(postYear))) day = postDate;
        if(!(currYear.equals(postYear))) day += ", " + postYear;
        tempTimestamp = day + " at " + postTime;
        if(day.matches("Today")){
            if(minDiff <= 1) tempTimestamp = "Just now";
            else if(minDiff < 60) tempTimestamp = minDiff + "m ago";
            else tempTimestamp = (minDiff/60) + "h ago";
        }

        ArrayList<String> trimmedBody = trimBody();
        /**
         * This allows the post to be surrounded by lines and separates each post from one another.
         */
        //TODO: fiddle with format numbers a bit for different lengths.
        //TODO: make sure to set a nickname limit.
        String result = " _________________________________________________\n" + String.format("%-1s %49s", "|", "|")
                + "\n|  " + String.format("%-15s %32s", name, tempTimestamp + "  |")
                + "\n" + String.format("%-1s %49s", "|", "|");
        for(String line : trimmedBody) result += String.format("%-50s %1s", "\n|   " + line, "|");
        result +=  "\n|_________________________________________________|";



        return result;



    }

}
