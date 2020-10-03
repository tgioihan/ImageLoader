package com.tgioihan.imageloader.listener;

import android.graphics.drawable.Drawable;

/**
 * Created by nguyenxuan on 4/22/2015.
 */
public interface ImageLoadingListener<K,T extends Drawable> {
    void onStart(K objectToLoad);

    void onLoading(K objectToLoad, float percent);

    void onLoadingFinish(K objectToLoad, T bitmaps);

    void onLoadingFail(Throwable e);
}
