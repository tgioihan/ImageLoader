package com.tgioihan.imageloader.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.decoder.GifDecoder;
import com.tgioihan.imageloader.drawable.GifDrawable;
import com.tgioihan.imageloader.drawable.MultiBitmapDrawable;
import com.tgioihan.imageloader.drawable.RecyclingBitmapDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyenxuan on 10/14/2015.
 */
public class LoaderUtilities {
    public static RecyclingBitmapDrawable getBitmapDrawable(byte[] bytes, String cacheKey, ImageCache imageCache, boolean shouldSaveCache) {
        Bitmap bitmap = convertByteArrayToBitmap(bytes, imageCache);
        if (bitmap != null) {
            RecyclingBitmapDrawable drawable = new RecyclingBitmapDrawable(imageCache.getContext().getResources(),bitmap);
            if(shouldSaveCache)
            imageCache.addBitmapToDiskCache(cacheKey, bitmap);
            return drawable;
        }
        return null;
    }

    protected static Bitmap convertByteArrayToBitmap(byte[] byteCopies, ImageCache imageCache) throws OutOfMemoryError {
        Bitmap bitmap = ImageSizeUtil.decodeBitmapFromByteArray(byteCopies, imageCache.getConfig().getDefaultImageSize().getWidth(), imageCache.getConfig().getDefaultImageSize().getHeight(), imageCache);
        if (bitmap != null) {
            return bitmap;
        }
        return null;
    }

    public static GifDrawable getGiftDrawable(byte[] bytes, String cacheKey, ImageCache imageCache, boolean shouldSaveCache) {
        if (bytes == null)
            return null;
        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read(bytes);

        GifDrawable gifDrawable = new GifDrawable(gifDecoder);
        if(shouldSaveCache)
        imageCache.addBitmapToDiskCache(cacheKey, bytes);
        return gifDrawable;
    }

    public static MultiBitmapDrawable getMultiBitmapDrawable(byte[] bytes, String cacheKey, ImageCache imageCache, boolean shouldSaveCache) {
        if (bytes == null) {
            return null;
        }
        List<Bitmap> bitmaps = decodeMultiBitmapFromByteArray(bytes, imageCache.getConfig().getDefaultImageSize().getWidth(), imageCache.getConfig().getDefaultImageSize().getHeight(), cacheKey, imageCache,shouldSaveCache);
        if (bitmaps != null) {
            return new MultiBitmapDrawable(bitmaps, imageCache.getConfig().getDefaultImageSize());
        }
        return null;
    }

    public static List<Bitmap> decodeMultiBitmapFromByteArray(byte[] bytes, int reqWidth, int reqHeight, String cacheKey, ImageCache cache, boolean shouldSaveCache) throws OutOfMemoryError {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = ImageSizeUtil.calculateInSampleSize(options, reqWidth, reqHeight);
        // If we're running on Honeycomb or newer, try to use inBitmap
        if (Utils.hasHoneycomb()) {
            ImageSizeUtil.addInBitmapOptions(options, cache);
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return tryBuildMultiBitmap(bytes, reqWidth, reqHeight, options, cacheKey, cache, true,shouldSaveCache);
    }

    private static List<Bitmap> tryBuildMultiBitmap(byte[] bytes, int reqWidth, int reqHeight, BitmapFactory.Options options, String cacheKey, ImageCache cache, boolean save, boolean shouldSaveCache) {
        Bitmap bitmap = null;
        try {
            bitmap = ImageSizeUtil.tryScaleDownBitmap(bytes, options);
            List<Bitmap> bitmaps = buildMultiBitmap(reqWidth, reqHeight, bitmap);
            if (save && shouldSaveCache)
                cache.addBitmapToDiskCache(cacheKey, bitmap);
            return bitmaps;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            DebugLog.e("out of memory ==> try scale down bitmap");
            options.inSampleSize *= 2;
            if (bitmap != null) {
                bitmap.recycle();
                DebugLog.e("out of memory ==> also recycle bitmap decoded");
            }

            return tryBuildMultiBitmap(bytes, reqWidth, reqHeight, options, cacheKey, cache, false, shouldSaveCache);
        }
    }

    private static List<Bitmap> buildMultiBitmap(int defaultWidth, int defaultHeight, Bitmap bitmap) throws OutOfMemoryError {
        List<Bitmap> bitmaps = new ArrayList<>();

        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        int y = 0;
        while (y < imgHeight) {
            int bitmapHeight = ((imgHeight - y) >= defaultHeight ? defaultHeight : (imgHeight - y));
            Bitmap bitmapResolve = Bitmap.createBitmap(bitmap, 0, y, imgWidth, bitmapHeight);
            bitmaps.add(bitmapResolve);
            y += bitmapHeight;
        }
        return bitmaps;
    }
}
