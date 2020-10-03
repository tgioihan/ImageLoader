package com.tgioihan.imageloader;

import android.graphics.drawable.Drawable;

import com.tgioihan.imageloader.loader.ImageDownloaderTask;

import java.util.Map;

/**
 * Created by nguyenxuan on 10/14/2015.
 */
public class DownLoadBuilder<T extends Drawable> extends Builder<String, T> {
    private Map<String, String> headers;

    public DownLoadBuilder(ImageCache imageCache, String url) {
        super(imageCache, url);
    }

    public Builder setHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public ImageDownloaderTask build() {
        ImageDownloaderTask imageDownloader = (ImageDownloaderTask) super.build();
        imageDownloader.setHeaders(headers);

        return imageDownloader;
    }
}
