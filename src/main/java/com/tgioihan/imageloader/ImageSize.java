
package com.tgioihan.imageloader;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageSize implements Parcelable {
    public static final Creator CREATOR =
            new Creator() {
                public ImageSize createFromParcel(Parcel in) {
                    return new ImageSize(in);
                }

                public ImageSize[] newArray(int size) {
                    return new ImageSize[size];
                }
            };

    public ImageSize(Parcel in) {
        width = in.readInt();
        height = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
    }

    private Integer height;
    private Integer width;

    public ImageSize(int width, int height) {

        this.width = width;
        this.height = height;
    }

    /**
     * @return The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

}
