package com.capstone.cudaf.ultratravel.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.UltratravelApplication;
import com.capstone.cudaf.ultratravel.analytics.ActionType;
import com.capstone.cudaf.ultratravel.analytics.ViewType;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.City;
import com.capstone.cudaf.ultratravel.utils.PicassoHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class CityActivity extends UltratravelBaseActivity {

    @BindView(R.id.fab)
    FloatingActionButton mMainFab;
    @BindView(R.id.fab_1)
    FloatingActionButton mRestaurantFab;
    @BindView(R.id.fab_2)
    FloatingActionButton mHotelFab;
    @BindView(R.id.fab_3)
    FloatingActionButton mMuseumFab;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.anim_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.header)
    ImageView mCityHeader;
    @BindView(R.id.city_description)
    TextView mCityDescription;
    @BindView(R.id.coordinator)
    UltratravelCoordinatorLayout mCoordinatorLayout;
    City mSelectedCity;

    public static final String CITY_PARAM = "city";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FirebaseStorage mFirebaseStorage = ((UltratravelApplication) getApplication()).getStorage();
        Intent i = getIntent();
        mSelectedCity = (City) i.getSerializableExtra(CITY_PARAM);
        if (mSelectedCity != null) {
            mCollapsingToolbar.setTitle(mSelectedCity.getCity());
            StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl(getString(R.string.firebase_storage));
            storageRef.child(mSelectedCity.getMain_image()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    PicassoHelper.picassoImageFromURI(CityActivity.this, mCityHeader, uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    mCityHeader.setBackground(ContextCompat.getDrawable(CityActivity.this, R.drawable.placeholder_image));
                }
            });
            mCityHeader.setContentDescription(getString(R.string.content_descriptor_city_image, mSelectedCity.getCity()));
            mCityDescription.setText(mSelectedCity.getDescription());
            setClickListener();
        }

    }

    private void setClickListener() {
        mMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCoordinatorLayout.isFabStatus()) {
                    //Display FAB menu
                    trackAction(null, null, ActionType.CLICK_CITY_MORE);
                    expandFAB();
                    mCoordinatorLayout.setFabStatus(true);
                } else {
                    //Close FAB menu
                    hideFAB();
                    mCoordinatorLayout.setFabStatus(false);
                }
            }
        });

        mRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBusinessListActivity(BusinessType.RESTAURANT);
            }
        });

        mHotelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBusinessListActivity(BusinessType.HOTELS);
            }
        });

        mMuseumFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBusinessListActivity(BusinessType.MUSEUMS);
            }
        });
    }

    private void goToBusinessListActivity(BusinessType businessType) {
        trackAction(null, businessType.getBusinessType(), ActionType.CLICK_BUSINESS_LIST);
        Intent intent = new Intent(CityActivity.this, BusinessListActivity.class);
        intent.putExtra(BusinessListActivity.TERM_PARAM, businessType);
        intent.putExtra(BusinessListActivity.CITY_PARM, mSelectedCity.getCity());
        startActivity(intent);
    }

    private void expandFAB() {
        mRestaurantFab.setVisibility(View.VISIBLE);
        mHotelFab.setVisibility(View.VISIBLE);
        mMuseumFab.setVisibility(View.VISIBLE);
        mMainFab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_language_white_36dp));
        findViewById(R.id.overlay_fab).setVisibility(View.VISIBLE);
    }


    private void hideFAB() {
        mRestaurantFab.setVisibility(View.INVISIBLE);
        mHotelFab.setVisibility(View.INVISIBLE);
        mMuseumFab.setVisibility(View.INVISIBLE);
        mMainFab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_add_white));
        findViewById(R.id.overlay_fab).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        trackView(ViewType.CITY_DETAIL);
    }
}
