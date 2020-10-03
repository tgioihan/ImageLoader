package com.tgioihan.imageloader.loader.loaderimplement;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.loader.ImageDownloaderTask;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.drawable.RecyclingBitmapDrawable;
import com.tgioihan.imageloader.listener.ImageLoadingListener;
import com.tgioihan.imageloader.util.LoaderUtilities;


/**
 * Created by nguyenxuan on 4/25/2015.
 */
public class BitmapDrawableDownloader extends ImageDownloaderTask<RecyclingBitmapDrawable> {
    public BitmapDrawableDownloader(ImageCache imageCache, String url, ImageSize imageSize, ImageLoadingListener imageLoadingListener) {
        super(imageCache, url, imageSize, imageLoadingListener);
    }

    @Override
    protected RecyclingBitmapDrawable processByteArray(byte[] bytes, boolean shouldSaveCache) throws OutOfMemoryError {
        return LoaderUtilities.getBitmapDrawable(bytes, getCacheKey(), imageCache,shouldSaveCache);
    }

}
