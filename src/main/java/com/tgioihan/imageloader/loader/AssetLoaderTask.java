package com.tgioihan.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.drawable.RecyclingBitmapDrawable;
import com.tgioihan.imageloader.listener.ImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;

public class AssetLoaderTask extends ResourceLoaderTask<String, RecyclingBitmapDrawable> {

    public AssetLoaderTask(ImageCache imageCache, String objectToLoad, ImageSize imageSize, ImageLoadingListener<String, RecyclingBitmapDrawable> imageLoadingListener) {
        super(imageCache, objectToLoad, imageSize, imageLoadingListener);
    }

    @Override
    protected RecyclingBitmapDrawable onLoadResource(String objectToLoad) {
        try {
            InputStream inputStream = imageCache.getContext().getAssets().open(objectToLoad);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return new RecyclingBitmapDrawable(imageCache.getContext().getResources(),bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
