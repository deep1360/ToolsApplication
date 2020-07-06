package com.example.toolsapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private ArrayList<ToolClass> ToolList;
    private View.OnClickListener Listener;


    public RecAdapter(ArrayList<ToolClass> ToolList) {
        this.ToolList = ToolList;
    }

    public void setOnItemClickListner(View.OnClickListener itemClickListner)
    {
        Listener = itemClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tool_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.toolname.setText(ToolList.get(position).getTooname());
        holder.username.setText(ToolList.get(position).getUsername());
        holder.description.setText(ToolList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return ToolList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView toolname, username, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            toolname = itemView.findViewById(R.id.ToolNameView);
            username = itemView.findViewById(R.id.ToolOwnerView);
            description = itemView.findViewById(R.id.ToolDesc);

            itemView.setTag(this);
            itemView.setOnClickListener(Listener);



        }
    }
}
