package com.example.define;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }

    //set details to recycler view row
    public void setDetails (Context ctx, String title, String name, String description, String referelLink){
        //views
        TextView mTitleView = mView.findViewById(R.id.title);
        TextView mNameView = mView.findViewById(R.id.name);
        TextView mDescView = mView.findViewById(R.id.description);
//        TextView mReferelLink = mView.findViewById(R.id.linkRef);

        //set data to views
        mTitleView.setText(title);
        mNameView.setText(name);
        mDescView.setText(description);
//        mReferelLink.setText(referelLink);

    }
}
