package com.example.define;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> implements Filterable {
    private List<Profile> profileListFull;


    public searchAdapter(List<Profile> profileList) {
        profileListFull = profileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Profile> profileList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                profileList.addAll(profileListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Profile item : profileListFull){
                    if (item.getpTitle().toLowerCase().contains(filterPattern)){
                        profileList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = profileList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
