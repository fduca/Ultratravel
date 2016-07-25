package com.capstone.cudaf.ultratravel.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.capstone.cudaf.ultratravel.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * This helper will handle the caching to disk for all the images in the application
 */
public class PicassoHelper {

    public static void picassoImageFromString(final Context context, final ImageView destination, final String url) {
        Picasso.with(context).load(url).fit().centerCrop().networkPolicy(NetworkPolicy.OFFLINE)
                .into(destination, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again if the cache fails
                        Picasso.with(context).load(url).fit().centerCrop()
                                .error(ContextCompat.getDrawable(context, R.drawable.placeholder_image))
                                .into(destination, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                    }
                });
    }

    public static void picassoImageFromURI(final Context context, final ImageView destination, final Uri uri) {
        Picasso.with(context).load(uri).fit().centerCrop().networkPolicy(NetworkPolicy.OFFLINE)
                .into(destination, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again if the cache fails
                        Picasso.with(context).load(uri).fit().centerCrop()
                                .error(ContextCompat.getDrawable(context, R.drawable.placeholder_image))
                                .into(destination, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                    }
                });
    }

}
