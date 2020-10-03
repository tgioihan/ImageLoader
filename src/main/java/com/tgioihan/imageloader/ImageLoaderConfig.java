package com.tgioihan.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.tgioihan.imageloader.util.DebugLog;

import java.io.File;

public class ImageLoaderConfig {
    // Default memory cache size in kilobytes
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 4; // 5MB

    // Default disk cache size in bytes
    private static final long DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    private static final int DEFAULT_INSAMPLE_SIZE = 2;
    // Compression settings when writing images to disk cache
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;
    private static final int DISK_CACHE_INDEX = 0;

    // Constants to easily toggle various caches
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;

    private int memoryCacheSize;
    private Long diskCachSize;
    private String diskCacheDir;
    private Bitmap.CompressFormat compressFormat;
    private int compressQuality;
    private int diskCacheIndex;
    private boolean memoryCachEnable;
    private boolean diskCacheEnable;
    private ImageSize defaultImageSize;
    private int inSampleSize;
    private int maxPoolSize;

    public boolean isDebugable() {
        return isDebugable;
    }

    private boolean isDebugable;

    public void setDebugEnable(boolean isDebugable) {
        this.isDebugable = isDebugable;
        DebugLog.setEnable(isDebugable);
    }

    public static int getDefaultMemCacheSize() {
        return DEFAULT_MEM_CACHE_SIZE;
    }

    public int getMemoryCacheSize() {
        return memoryCacheSize;
    }

    public Long getDiskCachSize() {
        return diskCachSize;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return compressFormat;
    }

    public int getCompressQuality() {
        return compressQuality;
    }

    public int getDiskCacheIndex() {
        return diskCacheIndex;
    }

    public boolean isMemoryCachEnable() {
        return memoryCachEnable;
    }

    public boolean isDiskCacheEnable() {
        return diskCacheEnable;
    }

    public void setMemoryCacheSize(int memoryCacheSize) {
        this.memoryCacheSize = memoryCacheSize;
    }

    public void setDiskCachSize(Long diskCachSize) {
        this.diskCachSize = diskCachSize;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }

    public void setDiskCacheIndex(int diskCacheIndex) {
        this.diskCacheIndex = diskCacheIndex;
    }

    public void setMemoryCachEnable(boolean memoryCachEnable) {
        this.memoryCachEnable = memoryCachEnable;
    }

    public void setDiskCacheEnable(boolean diskCacheEnable) {
        this.diskCacheEnable = diskCacheEnable;
    }

    public String getDiskCacheDir() {
        return diskCacheDir;
    }

    public void setDiskCacheDir(String diskCacheDir) {
        this.diskCacheDir = diskCacheDir;
    }

    public ImageSize getDefaultImageSize() {
        return defaultImageSize;
    }

    public void setDefaultImageSize(ImageSize defaultImageSize) {
        this.defaultImageSize = defaultImageSize;
    }

    public int getInSampleSize() {
        return inSampleSize;
    }

    public void setInSampleSize(int inSampleSize) {
        this.inSampleSize = inSampleSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public static class Builder {
        private static final boolean DEFAULT_DEBUG = false;
        private static final int DEFAULT_POOLSIZE = 15;
        private Integer memoryCache;
        private Long diskCachSize;
        private Bitmap.CompressFormat compressFormat;
        private Integer compressQuality;
        private Integer diskCacheIndex;
        private Boolean memoryCachEnable;
        private Boolean diskCacheEnable;
        private String diskCacheDir;
        private ImageSize defaultImageSize;
        private String baseCacheDir;
        private int defaultMemoryCacheSize;
        private Integer inSampleSize;
        private Boolean isDebugable;
        private Integer maxPoolSize;

        public Builder(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            ImageSize imageSize = new ImageSize(width, height);
            defaultImageSize = imageSize;
            baseCacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            defaultMemoryCacheSize = maxMemory / 8;
        }

        public Builder setDebugEnable(boolean isDebugable) {
            this.isDebugable = isDebugable;
            return this;
        }

        public Builder setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder setInSampleSize(int inSampleSize) {
            this.inSampleSize = inSampleSize;
            return this;
        }


        public Builder setDiskCacheDir(String diskCacheDir) {
            this.diskCacheDir = baseCacheDir + diskCacheDir + File.separator;
            return this;
        }

        public Builder setMemoryCacheSize(int memoryCache) {
            this.memoryCache = memoryCache;
            return this;
        }

        public Builder setDiskCachSize(long diskCachSize) {
            this.diskCachSize = diskCachSize;
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            this.compressFormat = compressFormat;
            return this;
        }

        public Builder setCompressQuality(int compressQuality) {
            this.compressQuality = compressQuality;
            return this;
        }

        public Builder setDiskCacheIndex(int diskCacheIndex) {
            this.diskCacheIndex = diskCacheIndex;
            return this;
        }

        public Builder setMemoryCachEnable(boolean memoryCachEnable) {
            this.memoryCachEnable = memoryCachEnable;
            return this;
        }

        public Builder setDiskCacheEnable(boolean diskCacheEnable) {
            this.diskCacheEnable = diskCacheEnable;
            return this;
        }

        public ImageLoaderConfig build() {
            ImageLoaderConfig config = new ImageLoaderConfig();
            config.setMemoryCacheSize(memoryCache == null ? defaultMemoryCacheSize : memoryCache);
            config.setDiskCachSize(diskCachSize == null ? DEFAULT_DISK_CACHE_SIZE : diskCachSize);
            config.setCompressFormat(compressFormat == null ? DEFAULT_COMPRESS_FORMAT : compressFormat);
            config.setCompressQuality(compressQuality == null ? DEFAULT_COMPRESS_QUALITY : compressQuality);
            config.setDiskCacheIndex(diskCacheIndex == null ? DISK_CACHE_INDEX : diskCacheIndex);
            config.setMemoryCachEnable(memoryCachEnable == null ? DEFAULT_MEM_CACHE_ENABLED : memoryCachEnable);
            config.setDiskCacheEnable(diskCacheEnable == null ? DEFAULT_DISK_CACHE_ENABLED : diskCacheEnable);
            config.setDiskCacheDir(diskCacheDir);
            config.setDefaultImageSize(defaultImageSize);
            config.setInSampleSize(inSampleSize == null ? DEFAULT_INSAMPLE_SIZE : inSampleSize);
            config.setDebugEnable(isDebugable == null ? DEFAULT_DEBUG : isDebugable);
            config.setMaxPoolSize(maxPoolSize == null ? DEFAULT_POOLSIZE : maxPoolSize);
            return config;
        }
    }
}
