package com.capstone.cudaf.ultratravel.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.UltratravelApplication;
import com.capstone.cudaf.ultratravel.analytics.ActionType;
import com.capstone.cudaf.ultratravel.analytics.ViewType;
import com.capstone.cudaf.ultratravel.model.City;
import com.capstone.cudaf.ultratravel.utils.PicassoHelper;
import com.capstone.cudaf.ultratravel.view.viewholder.CityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity extends UltratravelBaseActivity {

    private FirebaseRecyclerAdapter<City, CityViewHolder> mFirebaseAdapter;
    private FirebaseStorage mFirebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.messages_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_dimension));
        mRecycler.setLayoutManager(layoutManager);
        DatabaseReference mFirebaseDatabaseReference = ((UltratravelApplication) getApplication()).getDatabaseReference();
        mFirebaseStorage = ((UltratravelApplication) getApplication()).getStorage();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<City,
                CityViewHolder>(
                City.class,
                R.layout.item_city,
                CityViewHolder.class,
                mFirebaseDatabaseReference.child(getString(R.string.firebase_database_reference))) {

            @Override
            protected void populateViewHolder(final CityViewHolder viewHolder,
                                              final City city, final int position) {
                StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl(getString(R.string.firebase_storage));
                storageRef.child(city.getMain_image()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        PicassoHelper.picassoImageFromURI(HomeActivity.this, viewHolder.imageView, uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        viewHolder.imageView.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.placeholder_image));
                    }
                });
                viewHolder.imageView.setContentDescription(getString(R.string.content_descriptor_city_list_image, city.getCity(), city.getCountry()));
                viewHolder.countryView.setText(city.getCountry());
                viewHolder.nameView.setText(city.getCity());
                viewHolder.currentItem = mFirebaseAdapter.getItem(position);
                viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trackAction(city.getCity(), null, ActionType.CLICK_CITY);
                        Intent intent = new Intent(HomeActivity.this, CityActivity.class);
                        intent.putExtra(CityActivity.CITY_PARAM, city);
                        startActivity(intent);

                    }

                });
            }

        };

        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.setAdapter(mFirebaseAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        trackView(ViewType.HOME_SCREEN);
    }
}
