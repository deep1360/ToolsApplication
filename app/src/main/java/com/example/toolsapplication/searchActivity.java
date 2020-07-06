package com.example.toolsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class searchActivity extends AppCompatActivity {

    EditText editText;
    Button BtnSearch;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = findViewById(R.id.tool_search);
        BtnSearch = findViewById(R.id.searchToolButton);

        recyclerView = findViewById(R.id.searchListView);
        final ArrayList<ToolClass> ToolList = new ArrayList<>();
        final RecAdapter adapter = new RecAdapter(ToolList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        BtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().trim().equals("")){
                    ToolList.clear();
                    BtnSearch.setEnabled(false);
                    String search_value = editText.getText().toString().trim().toUpperCase();

                    final FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("tools")
                            .whereEqualTo("tool_name",search_value)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        BtnSearch.setEnabled(true);
                                        if(task.getResult().size()>0){
                                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                                db.collection("users").document(document.getString("user_id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            DocumentSnapshot userDoc = task.getResult();
                                                            String username = userDoc.getString("username");
                                                            String email = userDoc.getString("email");
                                                            ToolClass tool = new ToolClass(document.getId(),document.getString("tool_name"),
                                                                    document.getString("tool_desc"),document.getString("user_id"),
                                                                    username,email,document.getBoolean("available"));
                                                            ToolList.add(tool);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                });
                                            }

                                            adapter.setOnItemClickListner(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                                    int position = viewHolder.getAdapterPosition();
                                                    Intent intent = new Intent(searchActivity.this,AvailabilityActivity.class);
                                                    intent.putExtra("id",ToolList.get(position).getId());
                                                    intent.putExtra("toolname",ToolList.get(position).getTooname());
                                                    intent.putExtra("username",ToolList.get(position).getUsername());
                                                    intent.putExtra("useremail",ToolList.get(position).getUseremail());
                                                    intent.putExtra("available",ToolList.get(position).getAvailable());
                                                    intent.putExtra("userid",ToolList.get(position).getUserid());
                                                    intent.putExtra("desc",ToolList.get(position).getDescription());
                                                    startActivity(intent);

                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(searchActivity.this,"Tool Not Found!",Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        BtnSearch.setEnabled(true);
                                        Toast.makeText(searchActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}