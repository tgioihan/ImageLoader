package com.tgioihan.imageloader.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;

import com.tgioihan.imageloader.decoder.GifDecoder;
import com.tgioihan.imageloader.util.DebugLog;


/**
 * Created by nguyenxuan on 4/26/2015.
 */
public class GifDrawable extends DrawableResponse {
    private  Paint paint;
    private GifDecoder decoder;
    private GifFrameLoader gifFrameLoader;
    private boolean visible;
    private Rect mCoverRect;

    public GifDrawable( GifDecoder decoder) {
        this.decoder = decoder;
        mCoverRect = new Rect();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
    }

    public void setGifFrameLoader(GifFrameLoader gifFrameLoader){
        if(this.gifFrameLoader!=null){
            this.gifFrameLoader.stop();
        }
        this.gifFrameLoader = gifFrameLoader;
        gifFrameLoader.setDecoder(decoder);
        gifFrameLoader.setGifFrameLoaderInterface(new GifFrameLoader.GifFrameLoaderInterface() {
            @Override
            public void onFrameReady() {
                invalidateSelf();
            }
        });
        start();
    }

    public void start() {
        if(visible&& gifFrameLoader!=null){
            gifFrameLoader.start();
        }
    }

    public void stop(){
//        gifFrameLoader.stop();
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        this.visible = visible;
        if (!visible) {
            stop();
        } else  {
            invalidateSelf();
        }
        return super.setVisible(visible, restart);

    }

    @Override
    public void draw(Canvas canvas) {
        if(gifFrameLoader == null){
            return;
        }
        final Rect bound = getBounds();

        final Bitmap bitmap = gifFrameLoader.getCurrentFrameBitmap();
        if(bitmap!=null&&!bitmap.isRecycled()){
            float ratio = (float) bound.width() / bitmap.getWidth();
            mCoverRect.set(0, 0, bound.right, 0 + (int) (bitmap.getHeight() * ratio));
            canvas.drawBitmap(bitmap, null, mCoverRect, paint);
        }
        gifFrameLoader.loadNextFrame();
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public int getBitmapSize() {
        if (decoder  == null)
            return 0;

        return decoder.getData().length;
    }

    @Override
    protected void onRecycle() {
        DebugLog.d("Gifdrawable should recycle");
        gifFrameLoader.release();
        gifFrameLoader = null;
        decoder.clear();
        decoder = null;
    }

}
