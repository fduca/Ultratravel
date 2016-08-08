package com.capstone.cudaf.ultratravel.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by cudaf on 08/08/2016.
 */

public class UltratravelWidgetService extends RemoteViewsService {
	/*
	 * So pretty simple just defining the Adapter of the listview
	 * here Adapter is ListProvider
	 * */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new FavouriteListProvider(this.getApplicationContext(), intent));
    }

}