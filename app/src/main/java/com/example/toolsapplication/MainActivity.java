package com.example.toolsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button PostButton, SearchButton, FavButton, BorrowedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PostButton = findViewById(R.id.post_tool);
        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postIntent = new Intent(MainActivity.this,PostToolActivity.class);
                startActivity(postIntent);
            }
        });

        SearchButton = findViewById(R.id.search_tool);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MainActivity.this,searchActivity.class);
                startActivity(searchIntent);
            }
        });

        BorrowedButton = findViewById(R.id.borrowed_tool);
        BorrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent borrowedIntent = new Intent(MainActivity.this,BorrowedActivity.class);
                startActivity(borrowedIntent);
            }
        });

        FavButton = findViewById(R.id.favorite_tool);
        FavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favIntent = new Intent(MainActivity.this,FavouriteActivity.class);
                startActivity(favIntent);
            }
        });

    }
}