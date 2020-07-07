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

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView recyclerViewFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        recyclerViewFav = findViewById(R.id.FavouriteRecyclerView);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArrayList<ToolClass> favList = new ArrayList<>();
        final RecAdapter adapter = new RecAdapter(favList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewFav.setLayoutManager(manager);
        recyclerViewFav.setAdapter(adapter);

        db.collection("favourites").whereEqualTo("userid",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("tools").document(document.getString("toolid")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        final DocumentSnapshot toolDoc = task.getResult();
                                        db.collection("users").document(toolDoc.getString("user_id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot userDoc = task.getResult();
                                                ToolClass fav = new ToolClass(toolDoc.getId(),toolDoc.getString("tool_name"),toolDoc.getString("tool_desc"),
                                                        toolDoc.getString("user_id"),userDoc.getString("username"),userDoc.getString("email"),toolDoc.getBoolean("available"));

                                                favList.add(fav);
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