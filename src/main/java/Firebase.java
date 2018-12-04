import java.io.IOException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.gson.Gson;

public class Firebase {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private CountDownLatch counter;
    private DatabaseReference.CompletionListener writeListener;

    public Firebase(String dbName, String acctPath){
        try {
            FileInputStream serviceAccount = new FileInputStream(acctPath);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://" + dbName + ".firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        catch (IOException e){
            System.out.println("Error in database initialization.");
            System.out.print(e.getMessage());
            System.exit(1);
        }

        writeListener = (de, dr) -> counter.countDown();

//        //TODO: use to detect new posts
//        readListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                tempValue = dataSnapshot.getValue(Post.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
    }

    public void child(String path){
        //TODO: check for recursion
        ref = ref.child(path);
    }

    public void parent(){
        ref = ref.getParent();
    }

    public void root(){
        ref = ref.getRoot();
    }

    public void setPath(String path){
        // Ex: folder/subfolder/key
        ref = database.getReference(path);
    }

    public String getPath(){
        return "Path: " + ref;
    }


    public static int count;
    public static ArrayList<Post> posts;
    public ArrayList<Post> readPosts(){
        Gson gson = new Gson();

        posts = new ArrayList();
        count = 99;
        ref.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                count = (int)snapshot.getChildrenCount();

                for (DataSnapshot child : snapshot.getChildren()) {
                    posts.add(gson.fromJson(gson.toJson(child.getValue()), Post.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.print("Read cancelled");
            }
        });

        // Wait until all posts collected.
        while(posts.size() < count){System.out.print("");} // Honestly don't know why I need a print here but that's the only way it works
        return posts;
    }

//    public Post read(String path){
//        database.getReference(path).addListenerForSingleValueEvent(readListener);
//        return tempValue;
//    }

    public void write(Post post){
        counter = new CountDownLatch(1);
        ref.child("posts").child(Long.toString(post.epoch)).setValue(post, writeListener);
        try {
            counter.await();
        }
        catch (InterruptedException e) {
            System.out.print("Connection to database interrupted. Data not saved.");
        }
    }


    public void write(String path, Post post){
        counter = new CountDownLatch(1);
        database.getReference(path).child(Long.toString(post.epoch)).setValue(post, writeListener);
        try {
            counter.await();
        }
        catch (InterruptedException e) {
            System.out.print("Connection to database interrupted. Data not saved.");
        }
    }

    public void write(ArrayList<Post> list){
        //TODO: improve efficiency
        for(Post post : list) {
            counter = new CountDownLatch(1);
            ref.child(Long.toString(post.epoch)).setValue(post, writeListener);
            try {
                counter.await();
            } catch (InterruptedException e) {
                System.out.print("Connection to database interrupted. Data not saved.");
            }
        }
    }

    public void write(HashMap<String, Post> map){
        //TODO: improve efficiency
        for(String path : map.keySet()) {
            counter = new CountDownLatch(1);
            database.getReference(path).child(Long.toString(map.get(path).epoch)).setValue(map.get(path), writeListener);
            try {
                counter.await();
            } catch (InterruptedException e) {
                System.out.print("Connection to database interrupted. Data not saved.");
            }
        }
    }
}
