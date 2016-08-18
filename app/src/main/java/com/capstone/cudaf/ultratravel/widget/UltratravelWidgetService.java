package com.capstone.cudaf.ultratravel.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class UltratravelWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new FavouriteListProvider(this.getApplicationContext(), intent));
    }

}