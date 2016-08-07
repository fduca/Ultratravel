package com.capstone.cudaf.ultratravel.view.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.Location;

public class BusinessViewHolder extends RecyclerView.ViewHolder {
    public TextView mBusinessName;
    public TextView mBusinessAddress;
    public TextView mBusinessRatings;
    public TextView mBusinessOpenings;
    public ImageView mBusinessImage;
    public LinearLayout mBusinessLayout;
    private View mItemView;
    public CardView mCardView;

    public BusinessViewHolder(View itemView) {
        super(itemView);
        this.mItemView = itemView;
        mBusinessName = (TextView) itemView.findViewById(R.id.business_name);
        mBusinessAddress = (TextView) itemView.findViewById(R.id.business_address);
        mBusinessRatings = (TextView) itemView.findViewById(R.id.business_ratings);
        mBusinessOpenings = (TextView) itemView.findViewById(R.id.business_openings);
        mBusinessImage = (ImageView) itemView.findViewById(R.id.business_image);
        mBusinessLayout = (LinearLayout) itemView.findViewById(R.id.main_layout_business);
        mCardView = (CardView) itemView.findViewById(R.id.business_cv);
    }

    public String createOpening(Boolean closed) {
        if (closed != null) {
            return closed ? mItemView.getResources().getString(R.string.close) : mItemView.getResources().getString(R.string.open);
        } else {
            return mItemView.getResources().getString(R.string.na);
        }
    }

    public int colorSchemeBusinessType(BusinessType businessType) {
        switch (businessType) {
            case RESTAURANT:
                return ContextCompat.getColor(itemView.getContext(), R.color.pink);
            case HOTELS:
                return ContextCompat.getColor(itemView.getContext(), R.color.yellow);
            case MUSEUMS:
                return ContextCompat.getColor(itemView.getContext(), R.color.purple);
        }
        return ContextCompat.getColor(itemView.getContext(), R.color.blue);
    }
}
