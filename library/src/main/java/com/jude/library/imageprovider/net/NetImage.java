package com.jude.library.imageprovider.net;

import java.io.Serializable;

/**
 * Created by Mr.Jude on 2015/2/23.
 */
public class NetImage implements Serializable {
    private String smallImage;
    private String largeImage;
    private int width;
    private int height;

    public NetImage(String smallImage, String largeImage, int width,int height) {
        this.height = height;
        this.largeImage = largeImage;
        this.smallImage = smallImage;
        this.width = width;
    }

    public int getHeight() {

        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
