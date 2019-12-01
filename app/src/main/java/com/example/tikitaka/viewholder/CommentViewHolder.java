package com.example.tikitaka.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tikitaka.R;
import com.example.tikitaka.models.Comment;
import com.example.tikitaka.models.Post;

import org.w3c.dom.Text;


public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView textView;
    public TextView dateView;
    public TextView opinionView;

    public CommentViewHolder(View itemView) {
        super(itemView);

        authorView = itemView.findViewById(R.id.commentAuthor);
        starView = itemView.findViewById(R.id.star_comment);
        numStarsView = itemView.findViewById(R.id.postNumStars_comment);
        textView = itemView.findViewById(R.id.commentBody);
        dateView = itemView.findViewById(R.id.commentDate);
        opinionView = itemView.findViewById(R.id.commentOpinion);
    }

    public void bindToPost(Comment comment, View.OnClickListener starClickListener) {

        authorView.setText(comment.author);
        numStarsView.setText(String.valueOf(comment.starCount));
        textView.setText(comment.text);
        dateView.setText(comment.date);
        opinionView.setText(comment.opinion);

        starView.setOnClickListener(starClickListener);
    }
}
