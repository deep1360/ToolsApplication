package com.example.toolsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class BorrowedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed);

        recyclerView = findViewById(R.id.BorrowedView);
    }
}