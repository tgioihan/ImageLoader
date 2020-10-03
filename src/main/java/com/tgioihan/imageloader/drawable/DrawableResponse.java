package com.tgioihan.imageloader.drawable;

import android.graphics.drawable.Drawable;

import com.tgioihan.imageloader.util.DebugLog;


/**
 * Created by nguyenxuan on 4/25/2015.
 */
public abstract class DrawableResponse extends Drawable implements RecyclingBitmapInterface {
    private int mDisplayRefCount;
    private boolean mHasBeenDisplayed;
    private int mCacheRefCount;

    /**
     * Notify the drawable that the displayed state has changed. Internally a
     * count is kept so that the drawable knows when it is no longer being
     * displayed.
     *
     * @param isDisplayed - Whether the drawable is being displayed or not
     */
    public void setIsDisplayed(boolean isDisplayed) {
        //BEGIN_INCLUDE(set_is_displayed)
        synchronized (this) {
            if (isDisplayed) {
                mDisplayRefCount++;
                mHasBeenDisplayed = true;
            } else {
                mDisplayRefCount--;
            }
        }

        // Check to see if recycle() can be called
        checkState();
        //END_INCLUDE(set_is_displayed)
    }

    /**
     * Notify the drawable that the cache state has changed. Internally a count
     * is kept so that the drawable knows when it is no longer being cached.
     *
     * @param isCached - Whether the drawable is being cached or not
     */
    public void setIsCached(boolean isCached) {
        //BEGIN_INCLUDE(set_is_cached)
        synchronized (this) {
            if (isCached) {
                mCacheRefCount++;
            } else {
                mCacheRefCount--;
            }
        }

        // Check to see if recycle() can be called
        checkState();
        //END_INCLUDE(set_is_cached)
    }

    private synchronized void checkState() {
        //BEGIN_INCLUDE(check_state)
        // If the drawable cache and display ref counts = 0, and this drawable
        // has been displayed, then recycle
        if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed ) {
            DebugLog.d("recycle multi bitmap with bitmaps ");
            onRecycle();
        }
        //END_INCLUDE(check_state)
    }

    protected abstract void onRecycle();
}
