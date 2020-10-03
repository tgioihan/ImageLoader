package com.tgioihan.imageloader.loader;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.error.HttpDownloadException;
import com.tgioihan.imageloader.error.NoMemoryException;
import com.tgioihan.imageloader.listener.ImageLoadingListener;
import com.tgioihan.imageloader.util.DebugLog;

import java.io.IOException;

/**
 * Created by nguyenxuan on 10/14/2015.
 */
public abstract class ImageLoaderTask<K, T extends Drawable> implements Runnable {

    protected ImageCache imageCache;
    protected K objectToLoad;
    protected ImageSize imageSize;

    protected ImageLoadingListener<K, T> imageLoadingListener;
    protected Handler dispatcher;
    protected volatile Boolean running = false;


    protected abstract byte[] onLoad(K objectToLoad) throws HttpDownloadException, NoMemoryException, IOException;

    protected abstract T onLoadResource(K objectToLoad);

    protected abstract T processByteArray(byte[] bytes, boolean shouldSaveCache) throws OutOfMemoryError;

    protected abstract String getCacheKey(K objectToLoad);

    public ImageLoaderTask(ImageCache imageCache, K objectToLoad, ImageSize imageSize, ImageLoadingListener<K, T> imageLoadingListener) {
        this.imageCache = imageCache;
        this.objectToLoad = objectToLoad;
        if (imageSize == null) {
            this.imageSize = imageCache.getConfig().getDefaultImageSize();
        } else {
            this.imageSize = imageSize;
        }
        this.imageLoadingListener = imageLoadingListener;
        dispatcher = new Handler();
    }

    @Override
    public void run() {
        running = true;
        if (running) {
            doInBackground();
        }
    }

    private final void doInBackground() {
        DebugLog.d("imageloaer start ");
        postDownloadStart();
        if (!running) {
            return;
        }
        T bitmapDownloaded = (T) imageCache.getBitmapFromMemCache(getCacheKey(objectToLoad));
        if (bitmapDownloaded != null) {
            DebugLog.d("hit cache size from memory cache ");
            if (!running) {
                return;
            }
            if (imageLoadingListener != null) {
                postDownloadUpdate(100);
                postDownloadFinish(bitmapDownloaded);
            }
        } else {

            try {
                boolean loadFromDisk = true;
                if(!isLoadFromResource()){
                    bitmapDownloaded = load(loadFromDisk);
                }else{
                    bitmapDownloaded = onLoadResource(objectToLoad);
                }
                if (bitmapDownloaded == null) {
                    postDownloadError(new Exception("canload load drawable "));
                } else {
                    imageCache.addBitmapToMemoryCache(getCacheKey(objectToLoad), bitmapDownloaded);
                    if(saveDisk() && bitmapDownloaded instanceof BitmapDrawable && imageCache.getConfig().isDiskCacheEnable()){
                        imageCache.addBitmapToDiskCache(getCacheKey(objectToLoad),((BitmapDrawable) bitmapDownloaded).getBitmap());
                    }
                    if (imageLoadingListener != null) {
                        postDownloadUpdate(100);
                        postDownloadFinish(bitmapDownloaded);
                    }
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                postDownloadError(e);
            } catch (Exception e) {
                e.printStackTrace();
                postDownloadError(e);
            } catch (HttpDownloadException e) {
                e.printStackTrace();
            } catch (NoMemoryException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean saveDisk() {
        return true;
    }

    public T load(boolean loadFromDisk) throws HttpDownloadException, NoMemoryException, IOException {
        byte[] bytes = imageCache.getByteArrayFromDiskCache(getCacheKey(objectToLoad));
        if (bytes == null) {
            bytes = onLoad(objectToLoad);
            loadFromDisk = false;
        }
        return processByteArray(bytes,!loadFromDisk);
    }

    protected boolean isLoadFromResource() {
        return false;
    }


    private void postDownloadError(final Throwable e) {
        if (running) {
            dispatcher.post(new Runnable() {
                @Override
                public void run() {
                    if (!running) {
                        return;
                    }
                    imageLoadingListener.onLoadingFail(e);
                }
            });

        }

    }

    private void postDownloadFinish(final T drawable) {
        if (running) {
            dispatcher.post(new Runnable() {
                @Override
                public void run() {
                    if (running && imageLoadingListener != null){
                        DebugLog.d("1");
                        imageLoadingListener.onLoadingFinish(objectToLoad, drawable);
                    }

                }
            });

        }
    }

    protected void postDownloadUpdate(final float percent) {
        if (running) {
            dispatcher.post(new Runnable() {
                @Override
                public void run() {
                    if (running && imageLoadingListener != null)
                        imageLoadingListener.onLoading(objectToLoad, percent);
                }
            });
        }

    }

    private void postDownloadStart() {
        if (running) {
            dispatcher.post(new Runnable() {
                @Override
                public void run() {
                    if (imageLoadingListener != null) {
                        if (running)
                            imageLoadingListener.onStart(objectToLoad);
                    }
                }
            });
        }
    }


    public synchronized void setRunning(Boolean running) {
        this.running = running;
        if (!running) {
            imageLoadingListener = null;
            onCancel();
        }
    }

    protected synchronized void onCancel() {

    }

    public String getCacheKey(){
        return getCacheKey(objectToLoad);
    }

    public synchronized void setImageLoadingListener(ImageLoadingListener<K, T> imageLoadingListener) {
        this.imageLoadingListener = imageLoadingListener;
    }

    public synchronized Boolean getRunning() {
        return running;
    }
}