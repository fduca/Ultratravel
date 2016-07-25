package com.capstone.cudaf.ultratravel.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.capstone.cudaf.ultratravel.analytics.ActionType;
import com.capstone.cudaf.ultratravel.analytics.ViewType;
import com.google.firebase.analytics.FirebaseAnalytics;

public class UltratravelBaseActivity extends AppCompatActivity {

    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    protected void trackAction(String itemName, String itemType, ActionType actionType){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, itemType);
        mFirebaseAnalytics.logEvent(actionType.getActionType(), bundle);
    }

    protected void trackView(ViewType viewType){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(viewType.getViewType(), bundle);
    }
}
