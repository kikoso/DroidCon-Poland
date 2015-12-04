package com.droidcon.snaphack.model;

import android.graphics.Bitmap;

import lombok.Data;

@Data
public class PhotoItem {
    private final Bitmap image;
    private final String filename;

    public PhotoItem(Bitmap image, String filename) {
        this.image = image;
        this.filename = filename;
    }
}
