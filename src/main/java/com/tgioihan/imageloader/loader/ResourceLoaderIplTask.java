package com.tgioihan.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.drawable.RecyclingBitmapDrawable;
import com.tgioihan.imageloader.listener.ImageLoadingListener;
import com.tgioihan.imageloader.util.DebugLog;

public class ResourceLoaderIplTask extends ResourceLoaderTask<Integer, RecyclingBitmapDrawable> {

    public ResourceLoaderIplTask(ImageCache imageCache, Integer objectToLoad, ImageSize imageSize, ImageLoadingListener<Integer, RecyclingBitmapDrawable> imageLoadingListener) {
        super(imageCache, objectToLoad, imageSize, imageLoadingListener);
    }

    @Override
    protected RecyclingBitmapDrawable onLoadResource(Integer objectToLoad) {
        Bitmap bitmap = BitmapFactory.decodeResource(imageCache.getContext().getResources(), objectToLoad);
        DebugLog.d("bitmap "+ bitmap.getWidth());
        return new RecyclingBitmapDrawable(imageCache.getContext().getResources(),bitmap);
    }
}
