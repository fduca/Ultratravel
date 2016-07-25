package com.capstone.cudaf.ultratravel.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

public class UltratravelCoordinatorLayout extends CoordinatorLayout {


    private boolean fabStatus = false;

    public UltratravelCoordinatorLayout(Context context) {
        super(context);
    }

    public UltratravelCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UltratravelCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isFabStatus() {
        return !fabStatus;
    }

    public void setFabStatus(boolean fabStatus) {
        this.fabStatus = fabStatus;
    }
}
