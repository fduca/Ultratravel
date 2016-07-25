package com.capstone.cudaf.ultratravel.view.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.model.City;

public class CityViewHolder extends RecyclerView.ViewHolder {

    public TextView countryView;
    public ImageView imageView;
    public TextView nameView;
    private View mItemView;
    public CardView mCardView;
    public City currentItem;

    public CityViewHolder(final View itemView) {
        super(itemView);
        mItemView = itemView;
        imageView = (ImageView) itemView.findViewById(R.id.img);
        countryView = (TextView) itemView.findViewById(R.id.country);
        nameView = (TextView) itemView.findViewById(R.id.name);
        mCardView = (CardView) itemView.findViewById(R.id.cv);
    }
}
