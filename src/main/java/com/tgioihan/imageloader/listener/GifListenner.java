package com.tgioihan.imageloader.listener;

import android.graphics.drawable.Drawable;

import com.tgioihan.imageloader.drawable.GifDrawable;


/**
 * Created by nguyenxuan on 4/26/2015.
 */
public abstract class GifListenner<K,T extends Drawable> implements ImageLoadingListener<K,T> {
    public abstract void onGifReady(K objectToLoad,GifDrawable drawable);
    @Override
    public void onLoadingFinish(K objectToLoad, T drawable) {
        onGifReady(objectToLoad,(GifDrawable)drawable);
    }
}
