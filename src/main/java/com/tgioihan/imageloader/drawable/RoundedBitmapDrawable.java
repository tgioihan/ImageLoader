package com.tgioihan.imageloader.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.view.Gravity;

import com.tgioihan.imageloader.util.ImageSizeUtil;


/**
 * Created by nguyenxuan on 5/5/2015.
 */
public class RoundedBitmapDrawable extends DrawableResponse {
    private static final int DEFAULT_PAINT_FLAGS =
            Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG;
    Bitmap mBitmap;
    private int mTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
    private int mGravity = Gravity.FILL;
    private Paint mPaint = new Paint(DEFAULT_PAINT_FLAGS);
    private BitmapShader mBitmapShader;
    private float mCornerRadius;

    final Rect mDstRect = new Rect();   // Gravity.apply() sets this
    final RectF mDstRectF = new RectF();

    private boolean mApplyGravity = true;

    // These are scaled to match the target density.
    private int mBitmapWidth;
    private int mBitmapHeight;


    public RoundedBitmapDrawable( Bitmap bitmap) {
        mBitmap = bitmap;
        if (mBitmap != null) {
            computeBitmapSize();
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        } else {
            mBitmapWidth = mBitmapHeight = -1;
        }
    }

    /**
     * Returns the paint used to render this drawable.
     */
    public final Paint getPaint() {
        return mPaint;
    }

    /**
     * Returns the bitmap used by this drawable to render. May be null.
     */
    public final Bitmap getBitmap() {
        return mBitmap;
    }

    private void computeBitmapSize() {
        mBitmapWidth = mBitmap.getScaledWidth(mTargetDensity);
        mBitmapHeight = mBitmap.getScaledHeight(mTargetDensity);
    }

    /**
     * Set the density scale at which this drawable will be rendered. This
     * method assumes the drawable will be rendered at the same density as the
     * specified canvas.
     *
     * @param canvas The Canvas from which the density scale must be obtained.
     *
     * @see Bitmap#setDensity(int)
     * @see Bitmap#getDensity()
     */
    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    /**
     * Set the density scale at which this drawable will be rendered.
     *
     * @param metrics The DisplayMetrics indicating the density scale for this drawable.
     *
     * @see Bitmap#setDensity(int)
     * @see Bitmap#getDensity()
     */
    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    /**
     * Set the density at which this drawable will be rendered.
     *
     * @param density The density scale for this drawable.
     *
     * @see Bitmap#setDensity(int)
     * @see Bitmap#getDensity()
     */
    public void setTargetDensity(int density) {
        if (mTargetDensity != density) {
            mTargetDensity = density == 0 ? DisplayMetrics.DENSITY_DEFAULT : density;
            if (mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    /**
     * Get the gravity used to position/stretch the bitmap within its bounds.
     *
     * @return the gravity applied to the bitmap
     *
     * @see Gravity
     */
    public int getGravity() {
        return mGravity;
    }

    /**
     * Set the gravity used to position/stretch the bitmap within its bounds.
     *
     * @param gravity the gravity
     *
     * @see Gravity
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            mApplyGravity = true;
            invalidateSelf();
        }
    }

    /**
     * Enables or disables the mipmap hint for this drawable's bitmap.
     * See {@link Bitmap#setHasMipMap(boolean)} for more information.
     *
     * If the bitmap is null, or the current API version does not support setting a mipmap hint,
     * calling this method has no effect.
     *
     * @param mipMap True if the bitmap should use mipmaps, false otherwise.
     *
     * @see #hasMipMap()
     */
    public void setMipMap(boolean mipMap) {
        throw new UnsupportedOperationException(); // must be overridden in subclasses
    }

    /**
     * Indicates whether the mipmap hint is enabled on this drawable's bitmap.
     *
     * @return True if the mipmap hint is set, false otherwise. If the bitmap
     *         is null, this method always returns false.
     *
     * @see #setMipMap(boolean)
     */
    public boolean hasMipMap() {
        throw new UnsupportedOperationException(); // must be overridden in subclasses
    }

    /**
     * Enables or disables anti-aliasing for this drawable. Anti-aliasing affects
     * the edges of the bitmap only so it applies only when the drawable is rotated.
     *
     * @param aa True if the bitmap should be anti-aliased, false otherwise.
     *
     * @see #hasAntiAlias()
     */
    public void setAntiAlias(boolean aa) {
        mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    /**
     * Indicates whether anti-aliasing is enabled for this drawable.
     *
     * @return True if anti-aliasing is enabled, false otherwise.
     *
     * @see #setAntiAlias(boolean)
     */
    public boolean hasAntiAlias() {
        return mPaint.isAntiAlias();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mPaint.setDither(dither);
        invalidateSelf();
    }

    void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight,
                            Rect bounds, Rect outRect) {
        throw new UnsupportedOperationException();
    }

    void updateDstRect() {
        if (mApplyGravity) {
            gravityCompatApply(mGravity, mBitmapWidth, mBitmapHeight,
                    getBounds(), mDstRect);
            mDstRectF.set(mDstRect);
            mApplyGravity = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        final Bitmap bitmap = mBitmap;
        if (bitmap == null) {
            return;
        }

        updateDstRect();

        final Paint paint = mPaint;
        final Shader shader = paint.getShader();
        if (shader == null) {
            canvas.drawBitmap(bitmap, null, mDstRect, paint);
        } else {
            canvas.drawRoundRect(mDstRectF, mCornerRadius, mCornerRadius, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        final int oldAlpha = mPaint.getAlpha();
        if (alpha != oldAlpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public int getAlpha() {
        return mPaint.getAlpha();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
        invalidateSelf();
    }

    public ColorFilter getColorFilter() {
        return mPaint.getColorFilter();
    }

    /**
     * Sets the corner radius to be applied when drawing the bitmap.
     */
    public void setCornerRadius(float cornerRadius) {
        if (isGreaterThanZero(cornerRadius)) {
            mPaint.setShader(mBitmapShader);
        } else {
            mPaint.setShader(null);
        }
        mCornerRadius = cornerRadius;
    }

    /**
     * @return The corner radius applied when drawing the bitmap.
     */
    public float getCornerRadius() {
        return mCornerRadius;
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    @Override
    public int getOpacity() {
        if (mGravity != Gravity.FILL) {
            return PixelFormat.TRANSLUCENT;
        }
        Bitmap bm = mBitmap;
        return (bm == null
                || bm.hasAlpha()
                || mPaint.getAlpha() < 255
                || isGreaterThanZero(mCornerRadius))
                ? PixelFormat.TRANSLUCENT : PixelFormat.OPAQUE;
    }

    private static boolean isGreaterThanZero(float toCompare) {
        return Float.compare(toCompare, +0.0f) > 0;
    }

    @Override
    protected void onRecycle() {
        if(mBitmap!=null && !mBitmap.isRecycled()){
            mBitmap.recycle();
        }
    }

    @Override
    public int getBitmapSize() {
        return mBitmap!=null? ImageSizeUtil.getBitmapSize(mBitmap):0;
    }
}
