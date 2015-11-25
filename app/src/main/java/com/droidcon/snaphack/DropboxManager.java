package com.droidcon.snaphack;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class DropboxManager {
    public static final String APP_KEY = "xzprcyai1qh7dpk";
    public static final String APP_SECRET = "2y5g0nmdejmder6";
    private static final String TAG = "DropboxManager";

    @Getter
    private final DbxAccountManager dbxAccountManager;
    private DbxFileSystem fileSystem;

    private DbxFileSystem.PathListener pathListener = new DbxFileSystem.PathListener() {
        @Override
        public void onPathChange(DbxFileSystem dbxFileSystem, DbxPath dbxPath, DbxFileSystem.PathListener.Mode mode) {
            Log.d(TAG, "Noticed that the filesystem changed...");
        }
    };

    public DropboxManager(Context appContext) {
        dbxAccountManager = DbxAccountManager.getInstance(appContext, APP_KEY, APP_SECRET);
    }

    public void initialise() {
        initFileSystem();
    }

    public void startLink(MainActivity mainActivity, int requestLinkToDbx) {
        if (!dbxAccountManager.hasLinkedAccount()) {
            dbxAccountManager.startLink(mainActivity, requestLinkToDbx);
        } else {
            initialise();
        }
    }

    private void initFileSystem() {
        try {
            fileSystem = DbxFileSystem.forAccount(dbxAccountManager.getLinkedAccount());
            fileSystem.addPathListener(pathListener, DbxPath.ROOT, DbxFileSystem.PathListener.Mode.PATH_ONLY);
        } catch (DbxException.Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    public List<DbxFileInfo> getPhotos() {
        List<DbxFileInfo> items = new ArrayList<>();
        try {
            return fileSystem.listFolder(DbxPath.ROOT);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void savePhoto(Bitmap imageBitmap) {
        try {
            DbxFile file = fileSystem.create(new DbxPath(DbxPath.ROOT, System.currentTimeMillis() + ".jpg"));
            FileOutputStream out = null;
            try {
                out = file.getWriteStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
}
