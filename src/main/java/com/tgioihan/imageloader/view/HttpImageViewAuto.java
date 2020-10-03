package com.tgioihan.imageloader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.tgioihan.imageloader.ImageSize;
import com.tgioihan.imageloader.util.DebugLog;

public class HttpImageViewAuto extends AutoRecyclingImageView {
    private ImageSize imageSize;
    private String url;

    public HttpImageViewAuto(Context context) {
        super(context);
    }

    public HttpImageViewAuto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(imageSize==null||(imageSize.getHeight()==0)){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else{
            int width = getDefaultSize(0, widthMeasureSpec);
            int height = getDefaultSize(0, heightMeasureSpec);
            float ratio = (float)width/imageSize.getWidth();
            int realHeight = (int) (imageSize.getHeight()*ratio);
            setMeasuredDimension(width, realHeight);
            if(getDrawable()!=null){
                DebugLog.d("width " + width + " realHeight " + realHeight);
                getDrawable().setBounds(0,0,width,realHeight);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
        requestLayout();
    }

    public void setUrl(String url) {

        this.url = url;
    }
}
