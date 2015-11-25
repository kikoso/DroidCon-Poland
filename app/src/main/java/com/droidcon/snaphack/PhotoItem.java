package com.droidcon.snaphack;

import android.media.Image;

import lombok.Data;

@Data
public class PhotoItem {
    private final String localPath;
    private final Image image;
    private final String description;
}
