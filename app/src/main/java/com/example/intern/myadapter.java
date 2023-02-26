package com.example.intern;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>
{
    List<String> files,status;

    public myadapter(List<String> files, List<String> status) {
        this.files = files;
        this.status = status;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        String filename=files.get(position);

        if(filename.length()>15)
            filename=filename.substring(0,15)+"...";

        holder.filename.setText(filename);

        String filestatus=status.get(position);

        if(filestatus.equals("loading"))
            holder.pbar.setImageResource(R.drawable.progress);
        else
            holder.pbar.setImageResource(R.drawable.checked);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView fileicon,pbar;
        TextView filename;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            filename=itemView.findViewById(R.id.filename);
            fileicon=itemView.findViewById(R.id.fileicon);
            pbar=itemView.findViewById(R.id.pbar);

        }
    }

}
