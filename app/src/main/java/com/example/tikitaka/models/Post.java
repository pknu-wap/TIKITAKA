package com.example.tikitaka.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String title;
    public String body;
    public String affirmative;
    public String opposition;
    public String reference;
    public String date;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String body, String affirmative, String opposition, String reference, String date) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.affirmative=affirmative;
        this.opposition=opposition;
        this.reference=reference;
        this.date=date;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("affirmative", affirmative);
        result.put("opposition",opposition);
        result.put("reference",reference);
        result.put("date",date);
        result.put("stars", stars);


        return result;
    }
    // [END post_to_map]

}
// [END post_class]
