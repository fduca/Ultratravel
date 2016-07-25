package com.capstone.cudaf.ultratravel.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.analytics.ViewType;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.Coordinate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
@SuppressWarnings("WeakerAccess")
public class BusinessMapsActivity extends UltratravelBaseActivity implements OnMapReadyCallback {

    private ArrayList<Business> mBusiness;
    private Coordinate mCityCenter;
    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;

    public static final String TERM_PARAM = "term";
    public static final String BUSINESS_PARAM = "business";
    public static final String CENTER_PARAM = "center";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_maps);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        BusinessType mTerm = (BusinessType) i.getSerializableExtra(TERM_PARAM);
        mBusiness = (ArrayList<Business>) i.getSerializableExtra(BUSINESS_PARAM);
        mCityCenter = (Coordinate) i.getSerializableExtra(CENTER_PARAM);
        if (mTerm != null) {
            getSupportActionBar().setTitle(mTerm.getBusinessType());
            switch (mTerm) {
                case RESTAURANT:
                    mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.pink));
                    break;
                case HOTELS:
                    mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
                    break;
                case MUSEUMS:
                    mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_list) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        trackView(ViewType.BUSINESS_MAP_VIEW);
        GoogleMap mMap = googleMap;
        if (mBusiness != null) {
            for (Business b : mBusiness) {
                LatLng location = new LatLng(b.getLocation().getCoordinate().getLatitude(),
                        b.getLocation().getCoordinate().getLongitude());
                mMap.addMarker(new MarkerOptions().position(location).title(b.getName()));
            }
            if (mCityCenter != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCityCenter.getLatitude(), mCityCenter.getLongitude()), 10.0f));
            }
        }
    }
}
