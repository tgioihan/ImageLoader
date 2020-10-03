package com.tgioihan.imageloader.error;

/**
 * Created by nguyenxuan on 4/25/2015.
 */
public class HttpDownloadException extends Throwable {
    private int status;

    public HttpDownloadException(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return "http response error with status "+status;
    }
}
