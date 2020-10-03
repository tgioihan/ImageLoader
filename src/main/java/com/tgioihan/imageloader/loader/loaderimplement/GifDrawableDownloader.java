package com.tgioihan.imageloader.loader.loaderimplement;


import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.drawable.GifDrawable;
import com.tgioihan.imageloader.listener.GifListenner;
import com.tgioihan.imageloader.loader.ImageDownloaderTask;
import com.tgioihan.imageloader.util.LoaderUtilities;

/**
 * Created by nguyenxuan on 4/26/2015.
 */
public class GifDrawableDownloader extends ImageDownloaderTask<GifDrawable> {
    public GifDrawableDownloader(ImageCache imageCache, String url, ImageSize imageSize, GifListenner<String,GifDrawable> imageLoadingListener) {
        super(imageCache, url, imageSize, imageLoadingListener);
    }

    @Override
    protected GifDrawable processByteArray(byte[] bytes, boolean shouldSaveCache) throws OutOfMemoryError {
        return LoaderUtilities.getGiftDrawable(bytes,getCacheKey(),imageCache,shouldSaveCache);
    }

}