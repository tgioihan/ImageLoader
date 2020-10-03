package com.tgioihan.imageloader.loader.loaderimplement;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.drawable.MultiBitmapDrawable;
import com.tgioihan.imageloader.listener.ImageLoadingListener;
import com.tgioihan.imageloader.loader.ImageDownloaderTask;
import com.tgioihan.imageloader.util.LoaderUtilities;

/**
 * Created by nguyenxuan on 4/25/2015.
 */
public class MultiBitmapDrawableDownloader extends ImageDownloaderTask<MultiBitmapDrawable> {
    public MultiBitmapDrawableDownloader(ImageCache imageCache, String url, ImageSize imageSize, ImageLoadingListener imageLoadingListener) {
        super(imageCache, url, imageSize, imageLoadingListener);
    }

    @Override
    protected MultiBitmapDrawable processByteArray(byte[] bytes, boolean shouldSaveCache) throws OutOfMemoryError {
        return LoaderUtilities.getMultiBitmapDrawable(bytes, getCacheKey(), imageCache,shouldSaveCache);
    }

}
