

import java.util.HashMap;
import java.util.Scanner;

public class UI {


    public static void printLogo(){

        System.out.println("    _____ _                                      \n" +
                        "   / ____| |                                     \n" +
                        "  | (___ | | ___  ___ _ __        _ __ ___   ___ \n" +
                        "   \\___ \\| |/ _ \\/ _ \\ '_ \\      | '_ ` _ \\ / _ \\\n" +
                        "   ____) | |  __/  __/ |_) |  _  | | | | | |  __/\n" +
                        "  |_____/|_|\\___|\\___| .__/  (_) |_| |_| |_|\\___|\n" +
                        "                     | |                         \n" +
                        "                     |_|                         ");
    }

    public static void printLogo(String name){
        printLogo();
        System.out.println();
        for(int i = 0; i < 21 - name.length()/2; i ++) System.out.print(" ");
        System.out.println("Welcome, " + name);
    }

    public static char getOption(HashMap<Character, String> opts){
        Scanner s = new Scanner(System.in);

        System.out.print("[ ");
        for(char key : opts.keySet()) System.out.print("'" + key + "': " + opts.get(key) + ", ");
        System.out.println();

        return s.next().toUpperCase().charAt(0);
    }

    public static String getName(){
        Scanner s = new Scanner(System.in);
        String name;
        do {
            System.out.print("\nEnter name: ");
            name = s.nextLine();
            if (name.length() > 15) System.out.println("(Must be shorter than 15 characters.)");
        }while(name.length() > 15);
        newPage();
        return name;

    }

    public static void newPage(){
        for(int i = 0; i < 45; i++) System.out.println();
    }

//    public static void printName(String name){
//        System.out.printf("%" + (name.length()), "\t\t Welcome, " + name);
//    }

    public static char getPageOption(int pageNum, int size, SleepLog log){
        Scanner s = new Scanner(System.in);

        System.out.printf("%34s", "[" + (pageNum > 1 ? "<" : " ") + "]  Page "
                + pageNum + "  [" + (pageNum < size/3 + size%3 ? ">" : " ") + "]\n");
        System.out.printf("%15s %31s", "[R]efresh","[M]ake post\n");
        System.out.printf("%29s", "[E]xit\n");
        System.out.println();
        System.out.printf("%25s", " ");

        return s.next().toUpperCase().charAt(0);

    }

    public static void getConfirm(){
        System.out.println();
        System.out.printf("%34s", "[R]eturn to posts");
        System.out.println("\n");
        System.out.printf("%25s", " ");
        (new Scanner(System.in)).next();
    }

    public static Post makePost(String name){
        Scanner s = new Scanner(System.in);
        SleepLog log = null;

        newPage();
        printLogo();
        System.out.print("\n  Write post: \n\n  ");
        String body = s.nextLine();

        System.out.print("\n  Attach Sleep.log? [y/n] ");
        if(s.next().toUpperCase().charAt(0) == 'Y'){
            log = new SleepLog("log");
            System.out.print("\n  Sleep.log completed. Preview Sleep.log? [y/n] ");
            if(s.next().toUpperCase().charAt(0) == 'Y') System.out.println(log.toString(name));
        }
        Post p = new Post(name, body, log);
        System.out.print("\n  Post completed. Preview post? [y/n] ");
        if(s.next().toUpperCase().charAt(0) == 'Y') System.out.println(p);
        System.out.print("\n  Uploading post... ");
        return p;

    }

    public static void finishPostNotif(){
        Scanner s = new Scanner(System.in);
        System.out.println("post uploaded.");
        do {
            System.out.print("\n  Continue? [y/n] ");
        }while(s.next().toUpperCase().charAt(0) != 'Y');


    }


}
