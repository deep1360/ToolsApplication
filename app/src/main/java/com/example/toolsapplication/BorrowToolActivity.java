package com.example.toolsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BorrowToolActivity extends AppCompatActivity {

    String ID, USERID, TOOLNAME, USERNAME, DETAILS;

    TextView toolName, ownerName;

    EditText dateOfReturn;
    Button borrowbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_tool);

        dateOfReturn = findViewById(R.id.returnDate);
        toolName = findViewById(R.id.ToolName);
        ownerName = findViewById(R.id.OwnerName);
        borrowbtn = findViewById(R.id.borrowToolButton);

        if (getIntent()!=null) {
            ID = getIntent().getStringExtra("id");
            TOOLNAME = getIntent().getStringExtra("toolname");
            USERNAME = getIntent().getStringExtra("username");
            USERID = getIntent().getStringExtra("userid");
            DETAILS = getIntent().getStringExtra("desc");

            toolName.setText(TOOLNAME);
            ownerName.setText(USERNAME);

            borrowbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dateOfReturn.getText().toString().trim().equals("")) {
                        dateOfReturn.setError("Enter Date!");
                        dateOfReturn.requestFocus();
                    } else {
                        String date = dateOfReturn.getText().toString().trim();
                        try {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date current_date = sdf.parse(sdf.format(new Date()));

                            if (date1.equals(current_date) | date1.after(current_date)) {
                               Toast.makeText(BorrowToolActivity.this,"Allow To Borrow",Toast.LENGTH_LONG).show();
                            } else {
                                dateOfReturn.setError("Enter Valid Date");
                                dateOfReturn.requestFocus();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}