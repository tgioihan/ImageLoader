package com.tgioihan.imageloader;

import android.graphics.drawable.Drawable;

import com.tgioihan.imageloader.drawable.GifDrawable;
import com.tgioihan.imageloader.drawable.MultiBitmapDrawable;
import com.tgioihan.imageloader.drawable.RecyclingBitmapDrawable;
import com.tgioihan.imageloader.listener.GifListenner;
import com.tgioihan.imageloader.listener.ImageLoadingListener;
import com.tgioihan.imageloader.loader.AssetLoaderTask;
import com.tgioihan.imageloader.loader.ImageLoaderTask;
import com.tgioihan.imageloader.loader.ResourceLoaderIplTask;
import com.tgioihan.imageloader.loader.loaderimplement.BitmapDrawableDownloader;
import com.tgioihan.imageloader.loader.loaderimplement.GifDrawableDownloader;
import com.tgioihan.imageloader.loader.loaderimplement.MultiBitmapDrawableDownloader;


/**
 * Created by nguyenxuan on 4/25/2015.
 */
public class Builder<K, T extends Drawable> {
    protected boolean asBitmap;
    protected boolean asMultiBitmap;
    protected boolean asGif;
    protected boolean asResouce;
    protected boolean asAssetResouce;
    protected ImageCache imageCache;
    protected K objectToLoad;
    protected ImageSize imageSize;
    protected ImageLoadingListener imageLoadingListener;


    public Builder(ImageCache imageCache, K url) {
        this.imageCache = imageCache;
        this.objectToLoad = url;
    }

    public Builder setImageCache(ImageCache imageCache) {
        this.imageCache = imageCache;
        return this;
    }

    public Builder setObjectToLoad(K url) {
        this.objectToLoad = url;
        return this;
    }

    public Builder setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
        return this;
    }

    public Builder asBitmap(ImageLoadingListener<K, RecyclingBitmapDrawable> imageLoadingListener) {
        asBitmap = true;
        asMultiBitmap = false;
        asAssetResouce = false;
        asGif = false;
        asResouce = false;
        this.imageLoadingListener = imageLoadingListener;
        return this;
    }

    public Builder asMultiBitmap(ImageLoadingListener<K, MultiBitmapDrawable> imageLoadingListener) {
        asMultiBitmap = true;
        asBitmap = false;
        asGif = false;
        asResouce = false;
        asAssetResouce = false;
        this.imageLoadingListener = imageLoadingListener;
        return this;
    }

    public Builder asGif(GifListenner<K, GifDrawable> imageLoadingListener) {
        asMultiBitmap = false;
        asBitmap = false;
        asGif = true;
        asResouce = false;
        asAssetResouce = false;
        this.imageLoadingListener = imageLoadingListener;
        return this;
    }

    public Builder asResource(ImageLoadingListener<K, T> imageLoadingListener) {
        asMultiBitmap = false;
        asBitmap = false;
        asGif = false;
        asResouce = true;
        asAssetResouce = false;
        this.imageLoadingListener = imageLoadingListener;
        return this;
    }

    public Builder asAssetResource(ImageLoadingListener<String, RecyclingBitmapDrawable> imageLoadingListener) {
        asMultiBitmap = false;
        asBitmap = false;
        asGif = false;
        asResouce = false;
        asAssetResouce = true;
        this.imageLoadingListener = imageLoadingListener;
        return this;
    }

    public ImageLoaderTask build() {
        if (imageSize == null) {
            this.imageSize = imageCache.getConfig().getDefaultImageSize();
        }
        ImageLoaderTask imageDownloader;
        if (asMultiBitmap) {
            imageDownloader = new MultiBitmapDrawableDownloader(imageCache, (String) objectToLoad, imageSize, imageLoadingListener);
        } else if (asGif) {
            imageDownloader = new GifDrawableDownloader(imageCache, (String) objectToLoad, imageSize, (GifListenner<String, GifDrawable>) imageLoadingListener);
        } else if (asAssetResouce) {
            imageDownloader = new AssetLoaderTask(imageCache, (String) objectToLoad, imageSize, imageLoadingListener);
        } else if (asResouce) {
            imageDownloader = new ResourceLoaderIplTask(imageCache, (Integer) objectToLoad, imageSize, imageLoadingListener);
        } else {
            imageDownloader = new BitmapDrawableDownloader(imageCache, (String) objectToLoad, imageSize, imageLoadingListener);
        }

        return imageDownloader;
    }
}
