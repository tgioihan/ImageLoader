package com.tgioihan.imageloader.loader;

import com.tgioihan.imageloader.ImageLoader;

import java.util.concurrent.FutureTask;

public class ImageLoaderFutureTask extends FutureTask<Void> {
    private ImageLoaderTask runnable;
    private ImageLoader imageLoader;

    public ImageLoaderFutureTask(ImageLoaderTask runnable, ImageLoader result) {
        super(runnable, null);
        this.runnable = runnable;
        this.imageLoader = result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        runnable.setRunning(false);
        imageLoader.rejectTask(this);
        return super.cancel(mayInterruptIfRunning);
    }

    @Override
    public void run() {
        super.run();
        imageLoader.rejectTask(this);
    }
}
