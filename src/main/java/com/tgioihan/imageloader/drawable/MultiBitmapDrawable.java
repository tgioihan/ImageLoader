package com.tgioihan.imageloader.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;

import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.util.ImageSizeUtil;

import java.util.List;

public class MultiBitmapDrawable extends DrawableResponse {
    private List<Bitmap> bitmaps;
    private Paint paint;
    int drawY;
    private Rect mCoverRect;

    public MultiBitmapDrawable(List<Bitmap> bitmaps, ImageSize defaultImageSize) {
        this.bitmaps = bitmaps;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        mCoverRect = new Rect();
    }

    public String toString() {
        if (bitmaps == null) {
            return "bitmaps null";
        }
        String rs = getClass().getName() + " size " + bitmaps.size() + " with heights ";
        for (Bitmap bitmap : bitmaps) {
            rs += bitmap.getHeight() + " ";
        }
        return rs;
    }

    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmaps == null) {
            return;
        }
        drawY = 0;
        final Rect bound = getBounds();
        float ratio = (float) bound.width() / bitmaps.get(0).getWidth();
        for (Bitmap bitmap : bitmaps) {
            if(!bitmap.isRecycled()){
                mCoverRect.set(0, drawY, bound.right, drawY + (int) (bitmap.getHeight() * ratio));
                canvas.drawBitmap(bitmap, null, mCoverRect, paint);
                drawY += mCoverRect.height();
            }
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
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
        if (bitmaps == null || bitmaps.size() == 0)
            return 0;
        int count = 0;
        for (Bitmap bitmap : bitmaps) {
            count += ImageSizeUtil.getBitmapSize(bitmap);
        }
        return count;
    }

    @Override
    protected void onRecycle() {
        if(bitmaps!=null){
            for (Bitmap bitmap : bitmaps) {
                bitmap.recycle();
            }
            bitmaps.clear();;
            bitmaps = null;
        }
    }
}
