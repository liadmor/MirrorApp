package com.example.mirrorapp;

public class Sticker {
    private final String name;
    private final String liad;
//    private final int imageResource;
//    private final String imageUrl;
    private final String keywords[];


//    public Sticker(String name, int imageResource, String imageUrl, String keywords[]) {
//        this.name = name;
//        this.imageResource = imageResource;
//        this.imageUrl = imageUrl;
//        this.keywords = keywords;
//    }

    public Sticker(String name, String liad, String keywords[]) {
        this.name = name;
        this.liad = liad;
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public String getLiad() {
        return liad;
    }

//
//    public int getImageResource() {
//        return imageResource;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }

    public String [] getKeywords() { return keywords; }
}
