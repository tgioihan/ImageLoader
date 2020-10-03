package com.tgioihan.imageloader.drawable;

import android.graphics.Bitmap;
import android.os.Handler;

import com.tgioihan.imageloader.decoder.GifDecoder;


/**
 * Created by nguyenxuan on 4/26/2015.
 */
public class GifFrameLoader {
    private GifDecoder decoder;
    private Handler handler;
    private Runnable runner ;
    private Bitmap currentFrameBitmap;
    private boolean stop;
    private GifFrameLoaderInterface gifFrameLoaderInterface;

    public GifDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(GifDecoder decoder) {
        this.decoder = decoder;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Runnable getRunner() {
        return runner;
    }

    public void setRunner(Runnable runner) {
        this.runner = runner;
    }

    public void setCurrentFrameBitmap(Bitmap currentFrameBitmap) {
        this.currentFrameBitmap = currentFrameBitmap;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public GifFrameLoaderInterface getGifFrameLoaderInterface() {
        return gifFrameLoaderInterface;
    }

    public void setGifFrameLoaderInterface(GifFrameLoaderInterface gifFrameLoaderInterface) {
        this.gifFrameLoaderInterface = gifFrameLoaderInterface;
    }

    public interface GifFrameLoaderInterface{
        public void onFrameReady();
    }

    public GifFrameLoader() {
        handler = new Handler();
        runner = new Runnable() {
            @Override
            public void run() {
                currentFrameBitmap = decoder.getNextFrame();
                gifFrameLoaderInterface.onFrameReady();
            }
        };
    }

    public GifFrameLoader(final GifDecoder decoder,GifFrameLoaderInterface listener) {
        this.decoder = decoder;
        this.gifFrameLoaderInterface = listener;
        handler = new Handler();
        stop = false;
        runner = new Runnable() {
            @Override
            public void run() {
                currentFrameBitmap = decoder.getNextFrame();
                gifFrameLoaderInterface.onFrameReady();
            }
        };
    }

    public void start(){
//        stop = false;
        loadNextFrame();
    }

    public void stop(){
//        stop = true;
        handler.removeCallbacks(runner);
    }

    public void loadNextFrame(){
        if(decoder!=null){
            decoder.advance();
            handler.postDelayed(runner,decoder.getDelay(decoder.getCurrentFrameIndex()));
        }
    }

    public Bitmap getCurrentFrameBitmap() {
        return currentFrameBitmap;
    }

    public void release() {
        if(currentFrameBitmap!=null){
            currentFrameBitmap.recycle();
            currentFrameBitmap = null;
        }
        handler.removeCallbacks(runner);
        handler = null;
        runner = null;
        gifFrameLoaderInterface = null;
    }
}
