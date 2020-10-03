package com.tgioihan.imageloader.decoder;

import java.util.ArrayList;
import java.util.List;

public class GifHeader {

    int[] gct = null;
    int status = GifDecoder.STATUS_OK;
    int frameCount = 0;

    GifFrame currentFrame;
    List<GifFrame> frames = new ArrayList<GifFrame>();
    // Logical screen size.
    // Full image width.
    int width;
    // Full image height.
    int height;

    // 1 : global color table flag.
    boolean gctFlag;
    // 2-4 : color resolution.
    // 5 : gct sort flag.
    // 6-8 : gct size.
    int gctSize;
    // Background color index.
    int bgIndex;
    // Pixel aspect ratio.
    int pixelAspect;
    //TODO: this is set both during reading the header and while decoding frames...
    int bgColor;
    int loopCount;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void scalDown(int scale){
        width/=scale;
        height/= scale;
    }

    public int getNumFrames() {
        return frameCount;
    }

    /**
     * Global status code of GIF data parsing.
     */
    public int getStatus() {
        return status;
    }
}
