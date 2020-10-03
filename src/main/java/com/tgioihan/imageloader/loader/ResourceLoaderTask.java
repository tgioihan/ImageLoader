package com.tgioihan.imageloader.loader;

import android.graphics.drawable.Drawable;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.error.HttpDownloadException;
import com.tgioihan.imageloader.error.NoMemoryException;
import com.tgioihan.imageloader.listener.ImageLoadingListener;

import java.io.IOException;

/**
 * Created by nguyenxuan on 10/14/2015.
 */
public abstract class ResourceLoaderTask<K,T extends Drawable> extends ImageLoaderTask<K,T> {

    public ResourceLoaderTask(ImageCache imageCache, K objectToLoad, ImageSize imageSize, ImageLoadingListener<K, T> imageLoadingListener) {
        super(imageCache, objectToLoad, imageSize, imageLoadingListener);
    }

    @Override
    protected boolean saveDisk() {
        return false;
    }

    @Override
    protected byte[] onLoad(K objectToLoad) throws HttpDownloadException, NoMemoryException, IOException {
        return new byte[0];
    }

    @Override
    protected boolean isLoadFromResource() {
        return true;
    }


    @Override
    protected T processByteArray(byte[] bytes, boolean shouldSaveCache) throws OutOfMemoryError {
        return null;
    }

    @Override
    protected String getCacheKey(K objectToLoad) {
        return "resource"+objectToLoad;
    }

}
