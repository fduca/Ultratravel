package com.capstone.cudaf.ultratravel.view;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.analytics.ActionType;
import com.capstone.cudaf.ultratravel.analytics.ViewType;
import com.capstone.cudaf.ultratravel.contentprovider.FavouriteSQLiteHelper;
import com.capstone.cudaf.ultratravel.contentprovider.UltratravelContentObserver;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.utils.ViewFieldHelper;
import com.capstone.cudaf.ultratravel.view.listener.OnSQLDataChangedListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class BusinessActivity extends UltratravelBaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, LoaderManager.LoaderCallbacks<Cursor>, OnSQLDataChangedListener {

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
    private UltratravelContentObserver mContentObserver;

    private static final int REQUEST_CALL_PHONE = 0;
    private static final int LOADER_ID = 0x02;

    public static final String BUSINESS_PARAM = "business";
    private static final Uri URI_FAVOURITE = Uri.parse("content://com.capstone.cudaf.ultratravel.contentprovider/favourites/item");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_business);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mContentObserver = new UltratravelContentObserver(new Handler(), this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent i = getIntent();
        mSelectedBusiness = (Business) i.getSerializableExtra(BUSINESS_PARAM);
        if (mSelectedBusiness != null) {
            applyBusinessColor();
            populateView();
            setButtonListener();
        }
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().
                registerContentObserver(
                        URI_FAVOURITE,
                        true,
                        mContentObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().
                unregisterContentObserver(mContentObserver);
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
        ContentValues values = new ContentValues();
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_NAME, mSelectedBusiness.getName());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_RATINGS, mSelectedBusiness.getRating());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_ADDRESS, mSelectedBusiness.getLocation().getDisplay_address().toString().replace("[", "").replace("]", ""));
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_SHORT_ADDRESS, mSelectedBusiness.getLocation().getDisplay_address().get(0));
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_CITY, mSelectedBusiness.getLocation().getCity());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_CATEGORY, ViewFieldHelper.generateCategory(mSelectedBusiness.getCategories()));
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_PHONE, mSelectedBusiness.getDisplay_phone());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_IMAGE, mSelectedBusiness.getImage_url());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_MOBILE_URL, mSelectedBusiness.getMobile_url());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_TYPE, mSelectedBusiness.getBusinessType().getBusinessType());
        try {
            getContentResolver().insert(URI_FAVOURITE, values);
        } catch (SQLException exception) {
            Toast.makeText(getApplicationContext(), getString(R.string.failed_saved_favourite), Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFavourite() {
        try {
            getContentResolver().delete(URI_FAVOURITE, null, new String[]{"" + mSelectedBusiness.getId()});
        } catch (SQLException exception) {
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

    private void populateView() {
        if (mSelectedBusiness != null) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (findViewById(R.id.image_business) != null) {
                        ((ImageView) findViewById(R.id.image_business)).setBackground(new BitmapDrawable(getResources(), bitmap));
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
    protected void onStart() {
        super.onStart();
        trackView(ViewType.BUSINESS_DETAIL_VIEW);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(
                        this,
                        URI_FAVOURITE,
                        null, null, new String[]{mSelectedBusiness.getName(), mSelectedBusiness.getLocation().getCity()}, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        isFavourite = false;
        data.moveToFirst();
        while (!data.isAfterLast()) {
            mSelectedBusiness.setId(data.getInt(0));
            isFavourite = true;
            mFavouriteButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(BusinessActivity.this, R.color.blue)));
            data.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void updateUI(Uri uri) {
        long result = ContentUris.parseId(uri);
        if (result > 0) {
            isFavourite = !isFavourite;
            if (isFavourite) {
                mFavouriteButton.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(BusinessActivity.this, R.color.blue)));
                Toast.makeText(getApplicationContext(), getString(R.string.saved_favourite), Toast.LENGTH_SHORT).show();
            } else {
                applyBusinessColor();
                Toast.makeText(getApplicationContext(), getString(R.string.removed_favourite), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.something_wrong_favourites), Toast.LENGTH_SHORT).show();
        }
    }
}