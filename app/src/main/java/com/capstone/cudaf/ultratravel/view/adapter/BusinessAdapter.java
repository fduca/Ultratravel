package com.capstone.cudaf.ultratravel.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.utils.PicassoHelper;
import com.capstone.cudaf.ultratravel.view.listener.OnBusinessItemListener;
import com.capstone.cudaf.ultratravel.view.viewholder.BusinessViewHolder;

import java.util.ArrayList;


public class BusinessAdapter extends RecyclerView.Adapter<BusinessViewHolder> {
    private ArrayList<Business> mBusinesses;
    private Context mContext;
    private OnBusinessItemListener mBusinessItemClickListener;


    public BusinessAdapter(Context context, ArrayList<Business> businesses,
                           OnBusinessItemListener onBusinessItemListener) {
        this.mBusinesses = businesses;
        this.mContext = context;
        this.mBusinessItemClickListener = onBusinessItemListener;
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.business_row, parent, false);
        return new BusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {
        final Business b = mBusinesses.get(position);
        holder.mBusinessName.setText(mBusinesses.get(position).getName());
        holder.mBusinessAddress.setText(holder.createAddress(b.getLocation()));
        holder.mBusinessRatings.setText(b.getRating());
        PicassoHelper.picassoImageFromString(mContext, holder.mBusinessImage, b.getImage_url());
        holder.mBusinessImage.setContentDescription( b.getName());
        holder.mBusinessOpenings.setText(holder.createOpening(b.is_closed()));
        holder.mBusinessLayout.setBackgroundColor(holder.colorSchemeBusinessType(b.getBusinessType()));
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBusinessItemClickListener != null) {
                    mBusinessItemClickListener.onBusinessItemListener(b);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }

}
