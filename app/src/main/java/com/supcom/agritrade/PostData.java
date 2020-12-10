package com.supcom.agritrade;

public class PostData {

    private String name;
    private int imageResourceId;

    private PostData(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}

