package com.example.define;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DefineAdapter extends RecyclerView.Adapter<DefineAdapter.ViewHolder> {
    private List<Profile> profileList;
    private final int limit = 25;
//    private ArrayList<Profile> profileListFull;
    private Context context;


    private static int currentPosition = 0;

    public DefineAdapter(List<Profile> profileList) {
        this.profileList = profileList;
//        this.profileListFull = profileListFull;
    }

//    public DefineAdapter(List<Profile> profileList, Context context){
//        this.profileList = profileList;
//        this.context = context;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        progressDialog.setMessage("Pleas wait...");
//        progressDialog.show();
        Profile profile = profileList.get(position);
        holder.title.setText(profile.getpTitle());
        holder.name.setText(profile.getuName());
        holder.desc.setText(profile.getpDesc());

//        if (currentPosition == position){
//            Animation slideDown = AnimationUtils.loadAnimation(context,R.anim.anim);
//
//            holder.linearLayout.startAnimation((slideDown));
//        }
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearLayout.getVisibility() == View.GONE &&
                        holder.linearLayout1.getVisibility() == View.GONE &&
                        holder.LikeLay2.getVisibility() == View.GONE){
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.linearLayout1.setVisibility(View.VISIBLE);
                    holder.LikeLay2.setVisibility(View.VISIBLE);
                }
                else {
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.linearLayout1.setVisibility(View.GONE);
                    holder.LikeLay2.setVisibility(View.GONE);
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearLayout.setVisibility(View.GONE);
            }
        });

//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.linearLayout.setVisibility(View.VISIBLE);
//            }
//        });
//        holder.desc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.linearLayout.setVisibility(View.GONE);
//            }
//        });
//        progressBar.setVisibility(View.GONE);
//        progressDialog.dismiss();
//        if (currentPosition == position){
//            Animation slideDown = AnimationUtils.loadAnimation(context,R.anim.anim);
//            holder.linearLayout.setVisibility(View.VISIBLE);
//            holder.linearLayout.startAnimation(slideDown);
//        }
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                currentPosition = position;
//                notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (profileList.size()>limit){
            return limit;
        }
        else {
            return profileList.size();
        }
    }

//    @Override
//    public Filter getFilter() {
//        return exampleFilter;
//    }

//    private Filter exampleFilter = new Filter(){
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Profile> profileList = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0){
//                profileList.addAll(profileList);
//            }
//            else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for (Profile item : profileList){
//                    if (item.getpTitle().toLowerCase().contains(filterPattern)){
//                        profileList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = profileList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            profileList.clear();
//            profileList.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };

//    public class DefineViewHolder extends RecyclerView.ViewHolder {
//        TextView title, name , desc;
//        LinearLayout linearLayout;
//        public DefineViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.title);
//            name = itemView.findViewById(R.id.name);
//            desc = itemView.findViewById(R.id.description);
//            linearLayout = itemView.findViewById(R.id.linearLayout);
//
//        }
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, name , desc;
        LinearLayout linearLayout, linearLayout1;
        RelativeLayout LikeLay2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.description);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            linearLayout1 = itemView.findViewById(R.id.layName);
            LikeLay2 = itemView.findViewById(R.id.likeLay);
        }
    }

    public void setSearchOperation(List<Profile> newList){
        profileList = new ArrayList<>();
        profileList.addAll(newList);
        notifyDataSetChanged();
    }
}
