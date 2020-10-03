package com.tgioihan.imageloader.loader;

import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.tgioihan.imageloader.ImageCache;
import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.error.HttpDownloadException;
import com.tgioihan.imageloader.error.NoMemoryException;
import com.tgioihan.imageloader.listener.ImageLoadingListener;
import com.tgioihan.imageloader.util.DebugLog;
import com.tgioihan.imageloader.util.StorageUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nguyenxuan on 4/22/2015.
 */
public abstract class ImageDownloaderTask<T extends Drawable> extends ImageLoaderTask<String, T> {
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    protected Map<String, String> headers;

    public ImageDownloaderTask(ImageCache imageCache, String url, ImageSize imageSize, ImageLoadingListener<String, T> imageLoadingListener) {
        super(imageCache, url, imageSize, imageLoadingListener);
    }

    @Override
    protected T onLoadResource(String objectToLoad) {
        return null;
    }

    @Override
    protected byte[] onLoad(String objectToLoad) throws HttpDownloadException, NoMemoryException, IOException {
        return download(objectToLoad);
    }

    @Override
    protected String getCacheKey(String objectToLoad) {
        return objectToLoad;
    }

    /**
     * Reads the contents of HttpEntity into a byte[].
     */
    private byte[] entityToBytes(HttpEntity entity, long totalSize) throws IOException, OutOfMemoryError {
        byte[] buffer;
        ByteArrayOutputStream bytes = null;
        try {
            bytes= new ByteArrayOutputStream();
            InputStream in = entity.getContent();
            if (in == null) {
                return null;
            }
            buffer = new byte[IO_BUFFER_SIZE];
            int count;
            int downloadedBytes = 0;
            while (running && (count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
                downloadedBytes += count;
                float percent = downloadedBytes * 100 / totalSize;
                postDownloadUpdate(percent);
            }
            if (!running) {
                bytes.flush();
                bytes.close();
                return null;
            }
            return bytes.toByteArray();
        } finally {
            if(bytes!=null){
                bytes.close();
            }
            try {
                // Close the InputStream and release the resources by "consuming the content".
                entity.consumeContent();
            } catch (IOException e) {
                // This can happen if there was an exception above that left the entity in
                // an invalid state.
                DebugLog.e("Error occured when calling consumingContent");
            }

        }
    }

    private void configHeader(HttpGet httpGet) {
        final Map<String, String> headerParams = getHeaders();
        Set<String> keys = headerParams.keySet();
        for (String key : keys) {
            httpGet.addHeader(key, headerParams.get(key));
        }
    }

    protected Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
        }
        return headers;
    }

    private byte[] download(String url) throws IOException, NoMemoryException, HttpDownloadException {
        if (url == null) {
            setRunning(false);
            return null;
        }
        disableConnectionReuseIfNecessary();
        AndroidHttpClient client = AndroidHttpClient.newInstance("DownloadTask");
        HttpGet httpGet = new HttpGet(url);
        configHeader(httpGet);
        HttpResponse response;
        try {
            response = client.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == 302) {
                String newUrl = response.getHeaders("location")[0].getValue();
                client.close();
                return download(newUrl);
            } else if (status != 200) {
                throw new HttpDownloadException(status);
            }
            long totalSize = response.getEntity().getContentLength();
            long storage = StorageUtils.getAvailableStorage();
            DebugLog.d("storage:" + storage + " totalSize:" + totalSize);

            if (totalSize > storage) {
                throw new NoMemoryException("SD card no memory.");
            }
            if (!running) {
                client.close();
                return null;
            }
            byte[] byteCopies = entityToBytes(response.getEntity(), totalSize);
            client.close();
            return byteCopies;
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            throw e;
        } finally {
            client.close();
        }
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
