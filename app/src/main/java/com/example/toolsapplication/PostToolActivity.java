package com.example.toolsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostToolActivity extends AppCompatActivity {

    EditText editToolName, ediDesc;

    Button PostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tool);

        editToolName = findViewById(R.id.editTextToolName);
        ediDesc = findViewById(R.id.editTextDesc);

        PostButton = findViewById(R.id.buttonPost);
        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editToolName.getText().toString().trim().equals("")) {
                    editToolName.setError("Empty Field!");
                } else if (ediDesc.getText().toString().trim().equals("")) {
                    ediDesc.setError("Empty Field!");
                } else {
                    String tool_name = editToolName.getText().toString().trim().toUpperCase();
                    String tool_desc = ediDesc.getText().toString().trim();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Map<String, Object> tool = new HashMap<>();
                    tool.put("tool_name", tool_name);
                    tool.put("tool_desc", tool_desc);
                    tool.put("user_id", uid);
                    tool.put("available",true);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("tools")
                            .add(tool)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(PostToolActivity.this,"Tool Posted Successfully!",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostToolActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
}