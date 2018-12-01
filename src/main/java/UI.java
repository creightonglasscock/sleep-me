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
        //System.out.println("A Glasscock-Chapple-Herrera-Christensen enterprise");
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
            System.out.print("\n\t\t\t   Enter name: ");
            name = s.nextLine();
            if (name.length() > 15) System.out.println("\t   (Must be shorter than 15 characters.)");
        }while(name.length() > 15);
        for(int i = 0; i < 70; i++) System.out.println();
        return name;

    }

//    public static void printName(String name){
//        System.out.printf("%" + (name.length()), "\t\t Welcome, " + name);
//    }

    public static char getPageOption(int pageNum, int size, SleepLog log){
        Scanner s = new Scanner(System.in);

        System.out.printf("%34s", "[" + (pageNum > 1 ? "<" : " ") + "]  Page "
                + pageNum + "  [" + (pageNum < size/3 + size%3 ? ">" : " ") + "]\n");
        System.out.printf("%15s %31s", "[R]efresh","[M]ake post\n");
        System.out.printf("%34s", "[V]iew sleep log\n");
        System.out.println();
        System.out.printf("%25s", " ");

        return s.next().toUpperCase().charAt(0);

    }

    public static Post makePost(String name){
        Scanner s = new Scanner(System.in);
        SleepLog log = null;

        for(int i = 0; i < 70; i++) System.out.println();
        System.out.println(" \\_______________________________________________/");
        System.out.print("\n  Write post: ");
        String body = s.nextLine();

        System.out.print("\n  Attach a Sleep Log? [y/n] ");
        if(s.next().toUpperCase().charAt(0) == 'Y') log = new SleepLog();

        System.out.print("\n  Uploading post...  ");
        return new Post(name, body, log);

    }

    public static void finishPostNotif(){
        System.out.println("post uploaded.\n\n \\_______________________________________________/");

    }


}
