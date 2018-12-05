import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Main {
//TODO: FIX TOO MANY POSTS RENDERING ISSUE
//TODO: FIX PAGE NAV FLUB
//TODO: FIX SLEEPLOG CIRCADIAN RHYTHM 100%
    public static void main(String[] args){
        final String DATABASE_NAME = "sleep-me";
        final String ACCT_PATH = "sleep-me.json";

        Firebase db = new Firebase(DATABASE_NAME, ACCT_PATH);



        UI.printLogo();

        //db.setPath("posts");

        String name = UI.getName();

        int size = 0;
        int page = 1;

        boolean on = true;


        //db.write(new Post("Abe Lincoln", "Four score and seven years ago our fathers brought forth on this continent a new nation.")); //43 chars line?
        ArrayList<Post> posts;

        //System.out.println(db.getPath() + "\n");

//        HashMap<Character, String> opts = new HashMap();
//        int page = 1;
//        opts.put('P', "View next page [");
//        opts.put('L', "View sleep log");
//        opts.put('P', "Make post");
//        UI.getOption(opts);

        while(on){
            UI.newPage();
            UI.printLogo(name);
            posts = db.readPosts();
            size = posts.size();
            Collections.reverse(posts);
            ArrayList<Post> logPosts = new ArrayList<>();
            try {
                for (int i = 3*(page-1); i < 3*(page-1) + 3; i++) {
                    if(posts.get(i).getLog() != null){
                        posts.get(i).setLogLetter((char)('A' + logPosts.size()));
                        logPosts.add(posts.get(i));
                    }
                    System.out.println(posts.get(i) + "\n");

                }

            } catch(IndexOutOfBoundsException e){}
            char ans  = UI.getPageOption(page, size, null);
            if(ans == 'M') {db.write(UI.makePost(name)); UI.finishPostNotif();}
            if(ans == '<' && page > 1) page --;
            if(ans == '>' && page < size/3 + size%3) page ++;
            if(ans == 'R') page = 1;
            if(ans == 'A' && logPosts.size() > 0) {UI.newPage(); UI.printLogo(); System.out.println(logPosts.get(0).getLog().toString(logPosts.get(0).getName())); UI.getConfirm();}
            if(ans == 'B' && logPosts.size() > 0) {UI.newPage(); UI.printLogo(); System.out.println(logPosts.get(1).getLog().toString(logPosts.get(1).getName())); UI.getConfirm();}
            if(ans == 'C' && logPosts.size() > 0) {UI.newPage(); UI.printLogo(); System.out.println(logPosts.get(2).getLog().toString(logPosts.get(2).getName())); UI.getConfirm();}
            if(ans == 'E') {
                UI.newPage();
                UI.printLogo();
                System.out.println("\n\t\t\tThank you for using Sleep.me!");
                try {
                    TimeUnit.SECONDS.sleep(2);
                }catch (Exception e) {}

                on = false;
            }
        }



    }
}