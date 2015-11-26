package com.droidcon.snaphack;

import android.graphics.Bitmap;

import com.dropbox.sync.android.DbxFileInfo;

import lombok.Data;

@Data
public class PhotoItem {
    private final DbxFileInfo info;
    private final Bitmap image;

    public PhotoItem(DbxFileInfo info, Bitmap image) {
        this.info = info;
        this.image = image;
    }
}
