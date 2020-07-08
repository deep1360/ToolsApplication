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
                        final String date = dateOfReturn.getText().toString().trim();
                        try {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date current_date = sdf.parse(sdf.format(new Date()));

                            if (date1.equals(current_date) | date1.after(current_date)) {
                                borrowbtn.setEnabled(false);
                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                
                                Map<String, Object> tool = new HashMap<>();
                                tool.put("tool_name", TOOLNAME);
                                tool.put("tool_desc", DETAILS);
                                tool.put("user_id", USERID);
                                tool.put("available",false);

                                db.collection("tools").document(ID).set(tool).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            Map<String, Object> borrowData = new HashMap<>();
                                            borrowData.put("tool_id", ID);
                                            borrowData.put("user_id", myId);
                                            borrowData.put("owner_id", USERID);
                                            borrowData.put("return_date",date);

                                            db.collection("borrows").add(borrowData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()){
                                                        borrowbtn.setEnabled(true);
                                                        Toast.makeText(BorrowToolActivity.this,"You have borrowed tool!",Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(BorrowToolActivity.this,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
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