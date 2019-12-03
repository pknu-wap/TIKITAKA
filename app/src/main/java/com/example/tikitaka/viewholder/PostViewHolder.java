package com.example.tikitaka.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tikitaka.R;
import com.example.tikitaka.models.Post;


public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView goodView;
    public TextView numGoodView;
    public ImageView badView;
    public TextView numbadView;
    public TextView bodyView;
    public TextView dateView;
    public TextView affirmativeView;
    public TextView oppositionView;
    public TextView referenceView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        goodView = itemView.findViewById(R.id.Good);
        numGoodView = itemView.findViewById(R.id.postNumGood);
        badView = itemView.findViewById(R.id.Bad);
        numbadView = itemView.findViewById(R.id.postNumBad);
        bodyView = itemView.findViewById(R.id.postBody);
        dateView = itemView.findViewById(R.id.postDate);
        affirmativeView = itemView.findViewById(R.id.postAffirmative);
        oppositionView = itemView.findViewById(R.id.postOpposition);
        referenceView = itemView.findViewById(R.id.postReference);
    }

    public void bindToPost(Post post, View.OnClickListener GoodClickListener, View.OnClickListener BadClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numGoodView.setText(String.valueOf(post.goodCount));
        numbadView.setText(String.valueOf(post.badCount));
        bodyView.setText(post.body);
        dateView.setText(post.date);
        affirmativeView.setText(post.affirmative);
        oppositionView.setText(post.opposition);
        referenceView.setText(post.reference);

        goodView.setOnClickListener(GoodClickListener);
        badView.setOnClickListener(BadClickListener);
    }
}
