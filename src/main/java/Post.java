import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Post
 * This class creates the object of Post. It connects to the online firebase. The post
 * contains a user's name and when they posted it in connection to current time.
 */

public class Post {

    protected long epoch;
    private String name, body, timestamp;
    private SleepLog log;
    private char logLetter = ' ';

    /**
     * Post
     * The constructor accepts the users name and post body and initializes the variables.
     */
    public Post(String name, String body){
        this(name, body, null);
    }

    public Post(String name, String body, SleepLog log){
        this(System.currentTimeMillis() / 1000L, name, body, log,
                DateTimeFormatter.ofPattern("MMM dd DDD yyyy h:mm a ").format(LocalDateTime.now()) +
                        (LocalDateTime.now().getMinute() + LocalDateTime.now().getHour()*60));
    }

    public Post(long epoch, String name, String body, SleepLog log, String timestamp){
        this.epoch = epoch;
        this.name = name;
        this.body = body;
        this.log = log;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public SleepLog getLog(){
        return log;
    }

    public void setLogLetter(char letter){
        logLetter = letter;
    }

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

    public String toString(){
        LocalDateTime now = LocalDateTime.now();
        String[] currTimestamp = (DateTimeFormatter.ofPattern("MMM dd DDD yyyy h:mm a ").format(now) +
                (now.getMinute() + now.getHour()*60)).split(" ");
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

        String result = " _________________________________________________\n" + String.format("%-1s %49s", "|", "|")
                + "\n|  " + String.format("%-15s %32s", name, tempTimestamp + "  |")
                + "\n" + String.format("%-1s %49s", "|", "|");
        for(String line : trimmedBody) result += String.format("%-50s %1s", "\n|   " + line, "|");
        if(log != null) result += "\n" + String.format("%-1s %49s", "|", "|") + String.format("%-50s %1s", "\n|   " + "> Sleep.log attached." + (logLetter == ' ' ? "" : " Press [" + logLetter + "] to view."), "|");
        result +=  "\n|_________________________________________________|";



        return result;



    }

}