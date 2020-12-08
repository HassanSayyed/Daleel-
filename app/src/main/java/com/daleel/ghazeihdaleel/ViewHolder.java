package com.daleel.ghazeihdaleel;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    private ViewHolder.ClickListner mClickListner;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //item Click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListner.onItemClicked(v, getAdapterPosition());
            }
        });
    }

     //set detials to RV rows
    public void setDetails (Context ctx, String pic, String name, String phone){
        TextView rNameView = mView.findViewById(R.id.RowNname);
        TextView rPhoneView = mView.findViewById(R.id.RowPhone);
        CircleImageView rcircleImageView = mView.findViewById(R.id.RowImage);

        rNameView.setText(name);
        rPhoneView.setText(phone);
        Picasso.get().load(pic).into(rcircleImageView);
    }

    //interface to sendCallbacks
    public interface ClickListner{
        void onItemClicked(View view, int position);
    }

    public void setOnClickListner(ViewHolder.ClickListner clickListner){
        mClickListner = clickListner;

    }
}
