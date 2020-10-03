package com.tgioihan.imageloader.listener;

import android.graphics.drawable.Drawable;

public abstract class SimpleLoadingListener<K,T extends Drawable> implements ImageLoadingListener<K,T> {
    @Override
    public void onStart(K objectToLoad) {

    }

    @Override
    public void onLoading(K objectToLoad, float percent) {

    }


    @Override
    public void onLoadingFail(Throwable e) {

    }
}
