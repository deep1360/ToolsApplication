package com.example.toolsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BorrowedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed);

        recyclerView = findViewById(R.id.BorrowedView);

        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArrayList<ToolClass> borrowList = new ArrayList<>();
        final RecAdapter adapter = new RecAdapter(borrowList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        db.collection("borrows").whereEqualTo("user_id",currentUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("tools").document(document.getString("tool_id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        final DocumentSnapshot toolDoc = task.getResult();
                                        db.collection("users").document(toolDoc.getString("user_id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot userDoc = task.getResult();
                                                ToolClass fav = new ToolClass(toolDoc.getId(),toolDoc.getString("tool_name"),toolDoc.getString("tool_desc"),
                                                        toolDoc.getString("user_id"),userDoc.getString("username"),userDoc.getString("email"),toolDoc.getBoolean("available"));

                                                borrowList.add(fav);
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}