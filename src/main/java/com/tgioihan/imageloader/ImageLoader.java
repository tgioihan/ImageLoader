package com.tgioihan.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.tgioihan.imageloader.loader.ImageLoaderFutureTask;
import com.tgioihan.imageloader.loader.ImageLoaderTask;
import com.tgioihan.imageloader.util.DebugLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by nguyenxuan on 10/14/2015.
 */
public class ImageLoader {
    private final ImageCache imageCache;
    ThreadPoolExecutor poolExecutor;
    private Map<String,Future> mTaskPool;
    private static ImageLoader instance;

    public ImageLoader(Context context,ImageLoaderConfig config) {
        this.poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.getMaxPoolSize());
        imageCache = new ImageCache(context, config);
        mTaskPool = new HashMap<>();
        instance = this;
    }

    public ImageCache getImageCache() {
        return imageCache;
    }

    public static ImageLoader getInstance() {
        return instance;
    }

    public static ImageLoader initial(Context context,ImageLoaderConfig config){
        ImageLoader imageLoader = new ImageLoader(context,config);
        return imageLoader;
    }

    public <K,T extends Drawable> Builder getBuilder(K objectToLoad){
        Builder<K,T> builder = new Builder<>(imageCache,objectToLoad);
        return builder;
    }

    public void apply(Builder builder){
        addTask(builder.build());
    }

    private void addTask(ImageLoaderTask imageLoaderTask){
        ImageLoaderFutureTask futureTask = new ImageLoaderFutureTask(imageLoaderTask,this);
        Future task = poolExecutor.submit(futureTask);
        mTaskPool.put(imageLoaderTask.getCacheKey(),task);
    }

    public void rejectTask(Future task){
        mTaskPool.remove(task);
    }

    public void cancelTask(String tag){
        DebugLog.d("cancel task "+ tag);
        Future task = mTaskPool.get(tag);
        if(task!=null){
            rejectTask(task);
            if(!task.isDone()){
                task.cancel(true);
            }
        }
    }
}
