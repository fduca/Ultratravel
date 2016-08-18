package com.capstone.cudaf.ultratravel.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.analytics.ActionType;
import com.capstone.cudaf.ultratravel.analytics.ViewType;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.Coordinate;
import com.capstone.cudaf.ultratravel.model.Hotel;
import com.capstone.cudaf.ultratravel.model.Location;
import com.capstone.cudaf.ultratravel.model.Museum;
import com.capstone.cudaf.ultratravel.model.Restaurant;
import com.capstone.cudaf.ultratravel.model.YelpResponse;
import com.capstone.cudaf.ultratravel.service.BusinessService;
import com.capstone.cudaf.ultratravel.service.BusinessServiceGenerator;
import com.capstone.cudaf.ultratravel.view.adapter.BusinessAdapter;
import com.capstone.cudaf.ultratravel.view.listener.OnBusinessItemListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("WeakerAccess")
public class BusinessListActivity extends UltratravelBaseActivity implements OnBusinessItemListener {

    @BindView(R.id.business_recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    protected ArrayList<Business> mBusiness;
    protected BusinessAdapter mBusinessAdapter;
    private BusinessType mTerm;
    private String mCity;
    protected String mType;
    private Coordinate mCityCenter;

    public static final String TERM_PARAM = "term";
    public static final String CITY_PARM = "city";
    public static final String TYPE_PARAM = "type";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        ButterKnife.bind(this);
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
        mTerm = (BusinessType) i.getSerializableExtra(TERM_PARAM);
        mCity = i.getStringExtra(CITY_PARM);
        mType = i.getStringExtra(TYPE_PARAM);
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
            default:
                mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
                break;
        }
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mType == null) {
            getMenuInflater().inflate(R.menu.menu_map, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            trackAction(null, null, ActionType.CLICK_MAP_VIEW);
            Intent intent = new Intent(BusinessListActivity.this, BusinessMapsActivity.class);
            intent.putExtra(BusinessMapsActivity.TERM_PARAM, mTerm);
            intent.putExtra(BusinessMapsActivity.BUSINESS_PARAM, mBusiness);
            intent.putExtra(BusinessMapsActivity.CENTER_PARAM, mCityCenter);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initViews() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_dimension));
        mRecyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }



    private void loadJSON() {
        BusinessServiceGenerator generator = new BusinessServiceGenerator(
                getString(R.string.consumer_key),
                getString(R.string.consumer_secret),
                getString(R.string.token),
                getString(R.string.token_secret)
        );
        BusinessService businessService = generator.createAPI(mTerm);
        Call<YelpResponse> call = businessService.listBusiness(mTerm.getBusinessType(), mCity);
        call.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                mBusiness = new ArrayList<>(response.body().getBusinesses());
                mCityCenter = response.body().getRegion().getCenter();
                mBusinessAdapter = new BusinessAdapter(BusinessListActivity.this,
                        mBusiness, BusinessListActivity.this);
                mRecyclerView.setAdapter(mBusinessAdapter);
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        trackView(ViewType.BUSINESS_LIST_VIEW);
    }

    public void onBusinessItemListener(Business business) {
        trackAction(business.getName(), business.getBusinessType().getBusinessType(), ActionType.CLICK_BUSINESS_ITEM);
        Intent intent = new Intent(BusinessListActivity.this, BusinessActivity.class);
        intent.putExtra(BusinessActivity.BUSINESS_PARAM, business);
        startActivity(intent);
    }
}
