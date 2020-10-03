package com.tgioihan.imageloader.view;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;

import com.tgioihan.imageloader.drawable.RecyclingBitmapInterface;
import com.tgioihan.imageloader.util.DebugLog;

public class RecyclingViewDrawableHelper {

    private View view;

    public RecyclingViewDrawableHelper(View view) {
        this.view = view;
    }

    public void onDetachedFromWindow() {
        // This has been detached from Window, so clear the drawable
        view.setBackgroundDrawable(null);
    }


    public void setBackgroundDrawable(Drawable drawable) {
        // Keep hold of previous Drawable
        final Drawable previousDrawable = view.getBackground();
        DebugLog.d("");
        if (previousDrawable != drawable) {
            // Notify new Drawable that it is being displayed
            notifyDrawable(drawable, true);

            // Notify old Drawable so it is no longer being displayed
            notifyDrawable(previousDrawable, false);
        }

    }

    /**
     * Notifies the drawable that it's displayed state has changed.
     *
     * @param drawable
     * @param isDisplayed
     */
    private void notifyDrawable(Drawable drawable, final boolean isDisplayed) {
        if (drawable instanceof RecyclingBitmapInterface) {
            // The drawable is a CountingBitmapDrawable, so notify it
            ((RecyclingBitmapInterface) drawable).setIsDisplayed(isDisplayed);
        } else if (drawable instanceof LayerDrawable) {
            // The drawable is a LayerDrawable, so recurse on each layer
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
                notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
            }
        }
    }
}
