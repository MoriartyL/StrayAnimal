package com.vagrant.android.vagrant.utility;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Txm on 19/08/2017.
 */

public class Utility {
    public static void loadImageFromUrl(String path, final ImageView imageView, final Activity activity){
        HttpUtil.sendOkhttpRequest(path, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String image = response.body().string();
                Log.e("MainActivity,Image:",image);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(activity).load(image).into(imageView);
                    }
                });

            }
        });
    }



}
