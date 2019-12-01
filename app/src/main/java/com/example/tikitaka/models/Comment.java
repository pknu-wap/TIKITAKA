package com.example.tikitaka.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    public String date;
    public String opinion;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text, String date, String opinion) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.date = date;
        this.opinion = opinion;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("date",date);
        result.put("opinion",opinion);
        result.put("text", text);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
// [END comment_class]
