package com.chestnut.codeSnippet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.Toast;

import com.chestnut.ui.R;

public class RatingBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(RatingBarActivity.this, "rating: " + rating + ", fromUser: " + fromUser, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
