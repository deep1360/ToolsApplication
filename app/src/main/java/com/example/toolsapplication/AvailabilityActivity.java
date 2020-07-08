package com.example.toolsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AvailabilityActivity extends AppCompatActivity {

    TextView Tool_Name,Tool_Owner,Owner_Email,Tool_Status;

    Button borrowBtn, favButton;

    String toolid = "", tname = "", tuser = "", temail = "", userid = "", desc = "";
    boolean tstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        Tool_Name = findViewById(R.id.toolName);
        Tool_Owner = findViewById(R.id.toolOwner);
        Owner_Email = findViewById(R.id.Emailtoolowner);
        Tool_Status = findViewById(R.id.ToolStatus);
        borrowBtn = findViewById(R.id.toolBorrowButton);
        favButton = findViewById(R.id.AddButtonFavorite);


        if(getIntent()!=null){
            toolid = getIntent().getStringExtra("id");
            tname = getIntent().getStringExtra("toolname");
            tuser = getIntent().getStringExtra("username");
            temail = getIntent().getStringExtra("useremail");
            userid = getIntent().getStringExtra("userid");
            tstatus = getIntent().getBooleanExtra("available",false);
            desc = getIntent().getStringExtra("desc");

            Tool_Name.setText(tname);
            Owner_Email.setText(temail);
            Tool_Owner.setText(tuser);

            if(tstatus){
                Tool_Status.setText("Available");
            }
            else {
                Tool_Status.setText("Not Available");
                borrowBtn.setEnabled(false);
            }

        }

        borrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvailabilityActivity.this,BorrowToolActivity.class);
                intent.putExtra("id",toolid);
                intent.putExtra("toolname",tname);
                intent.putExtra("username",tuser);
                intent.putExtra("useremail",temail);
                intent.putExtra("available",tstatus);
                intent.putExtra("userid",userid);
                intent.putExtra("desc",desc);
                startActivity(intent);
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favButton.setEnabled(false);
                final String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("favourites")
                        .whereEqualTo("userid",myid).whereEqualTo("toolid",toolid)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size()>0){
                            favButton.setEnabled(true);
                            Toast.makeText(AvailabilityActivity.this,"Tool Is Already In Favourites!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Map<String, Object> fav = new HashMap<>();
                            fav.put("userid", myid);
                            fav.put("toolid", toolid);

                            db.collection("favourites")
                                    .add(fav)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            favButton.setEnabled(true);
                                            Toast.makeText(AvailabilityActivity.this,"Added To Favourite",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            favButton.setEnabled(true);
                                            Toast.makeText(AvailabilityActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });

            }
        });


    }
}