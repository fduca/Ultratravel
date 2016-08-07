package com.capstone.cudaf.ultratravel.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.analytics.ActionType;
import com.capstone.cudaf.ultratravel.analytics.ViewType;
import com.capstone.cudaf.ultratravel.contentprovider.FavouriteDataSource;
import com.capstone.cudaf.ultratravel.contentprovider.FavouriteSQLiteHelper;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.utils.ViewFieldHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class BusinessActivity extends UltratravelBaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.name_business)
    TextView mNameBusiness;
    @BindView(R.id.category_business)
    TextView mCategoryBusiness;
    @BindView(R.id.rating_business)
    TextView mRatingBusiness;
    @BindView(R.id.address_business)
    TextView mAddressBusiness;
    @BindView(R.id.reservation_business)
    Button mReservationBusiness;
    @BindView(R.id.contact_business)
    Button mContactBusiness;
    Business mSelectedBusiness;
    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.favourite_button)
    FloatingActionButton mFavouriteButton;
    boolean isFavourite;

    private static final int REQUEST_CALL_PHONE = 0;

    public static final String BUSINESS_PARAM = "business";
    private FavouriteDataSource mFavouriteDataSource;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_business);
        ButterKnife.bind(this);
        mFavouriteDataSource = new FavouriteDataSource(this);
        mFavouriteDataSource.open();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent i = getIntent();
        mSelectedBusiness = (Business) i.getSerializableExtra(BUSINESS_PARAM);
        if (mSelectedBusiness != null) {
            isFavourite = isFavourite();
            applyBusinessColor();
            if (isFavourite){
                mFavouriteButton.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(BusinessActivity.this, R.color.blue)));

            }
            populateView();
            setButtonListener();
        }
    }

    private void setButtonListener() {
        mReservationBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackAction(mSelectedBusiness.getName(), mSelectedBusiness.getBusinessType().getBusinessType(), ActionType.CLICK_RESERVATION);
                if (mSelectedBusiness.getMobile_url() != null && !mSelectedBusiness.getMobile_url().isEmpty()) {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(mSelectedBusiness.getMobile_url()));
                    startActivity(browse);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_website_provided), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mContactBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackAction(mSelectedBusiness.getName(), mSelectedBusiness.getBusinessType().getBusinessType(), ActionType.CLICK_CONTACT);
                if (mSelectedBusiness.getDisplay_phone() != null && !mSelectedBusiness.getDisplay_phone().isEmpty()) {
                    Intent browse = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + mSelectedBusiness.getDisplay_phone()));
                    if (ActivityCompat.checkSelfPermission(BusinessActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestCallPermission();
                    } else {
                        startActivity(browse);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_phone_provided), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite) {
                    removeFavourite();
                } else {
                    addFavourite();
                }

            }
        });
    }

    private void addFavourite() {
        Business favourite = mFavouriteDataSource.createFavourite(mSelectedBusiness);
        if (favourite!= null) {
            isFavourite = true;
            mFavouriteButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(BusinessActivity.this, R.color.blue)));
            Toast.makeText(getApplicationContext(), getString(R.string.saved_favourite), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.failed_saved_favourite), Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFavourite() {
        boolean result = mFavouriteDataSource.deleteFavourite(mSelectedBusiness);
        if (result){
            isFavourite = false;
            applyBusinessColor();
            Toast.makeText(getApplicationContext(), getString(R.string.removed_favourite), Toast.LENGTH_SHORT).show();
        } else {
            isFavourite = true;
            Toast.makeText(getApplicationContext(), getString(R.string.failed_removed_favourite), Toast.LENGTH_SHORT).show();
        }
    }

    private void applyBusinessColor() {
        switch (mSelectedBusiness.getBusinessType()) {
            case RESTAURANT:
                mReservationBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.pink));
                mContactBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.pink20Lighter));
                if (!isFavourite) {
                    mFavouriteButton.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pink)));
                }
                break;
            case HOTELS:
                mReservationBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
                mContactBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow20Lighter));
                if (!isFavourite) {
                    mFavouriteButton.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow)));
                }
                break;
            case MUSEUMS:
                mReservationBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
                mContactBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.purple20Lighter));
                if (!isFavourite) {
                    mFavouriteButton.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple)));
                }
                break;
            default:
                mReservationBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
                mContactBusiness.setBackgroundColor(ContextCompat.getColor(this, R.color.blue20Lighter));
                if (!isFavourite) {
                    mFavouriteButton.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue)));
                }
                break;
        }
    }

    private boolean isFavourite() {
        Business business = mFavouriteDataSource.getFavouriteByNameAndCity(mSelectedBusiness.getName(), mSelectedBusiness.getLocation().getCity());
        if (business!= null){
            mSelectedBusiness.setId(business.getId());
            return true;
        }
        return  false;
    }

    private void populateView() {
        if (mSelectedBusiness != null) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (findViewById(R.id.image_business)!= null){
                        ((ImageView)findViewById(R.id.image_business)).setBackground(new BitmapDrawable(getResources(), bitmap));
                    } else {
                        mToolbar.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            Picasso.with(this).load(mSelectedBusiness.getImage_url())
                    .placeholder(ContextCompat.getDrawable(this, R.drawable.placeholder_image))
                    .into(target);
            mNameBusiness.setText(mSelectedBusiness.getName());
            mRatingBusiness.setText(getString(R.string.rating_label, mSelectedBusiness.getRating()));
            mAddressBusiness.setText(mSelectedBusiness.getLocation().getDisplay_address().toString());
            mCategoryBusiness.setText(ViewFieldHelper.generateCategory(mSelectedBusiness.getCategories()));
        }
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            Snackbar.make(mCoordinatorLayout, R.string.permission_call_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(BusinessActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    REQUEST_CALL_PHONE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mCoordinatorLayout, R.string.permision_available_call,
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mCoordinatorLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        mFavouriteDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mFavouriteDataSource.close();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        trackView(ViewType.BUSINESS_DETAIL_VIEW);
    }
}