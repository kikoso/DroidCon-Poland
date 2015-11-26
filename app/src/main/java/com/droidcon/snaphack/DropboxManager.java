package com.droidcon.snaphack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxSyncStatus;

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
    private List<DropboxManagerListener> listeners = new ArrayList<>();

    public void addListener(DropboxManagerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DropboxManagerListener listener) {
        listeners.remove(listener);
    }

    private DbxFileSystem.PathListener pathListener = new DbxFileSystem.PathListener() {
        @Override
        public void onPathChange(DbxFileSystem dbxFileSystem, DbxPath dbxPath, DbxFileSystem.PathListener.Mode mode) {
            Log.d(TAG, "Noticed that the filesystem changed...");
            onFileSystemChanged();
        }
    };

    private void onFileSystemChanged() {
        for (DropboxManagerListener listener : listeners) {
            listener.onFileSystemChanged();
        }
    }

    private DbxFileSystem.SyncStatusListener syncStatusListener = new DbxFileSystem.SyncStatusListener() {
        @Override
        public void onSyncStatusChange(DbxFileSystem fs) {
            Log.d(TAG, "onSyncStatusChange");
            DbxSyncStatus fsStatus = null;
            try {
                fsStatus = fs.getSyncStatus();
            } catch (DbxException e) {
                e.printStackTrace();
            }
            if (fsStatus.anyInProgress()) {
                // Show syncing indictor
            }
            onFileSystemChanged();
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
            fileSystem.addSyncStatusListener(syncStatusListener);
        } catch (DbxException.Unauthorized unauthorized) {
            unauthorized.printStackTrace();
            Log.e(TAG, "Failed Filesystem initialised ok");
            return;
        }
        Log.d(TAG, "Filesystem initialised ok");
    }

    public List<PhotoItem> getPhotos() {
        List<PhotoItem> items = new ArrayList<>();
        try {
            for (DbxFileInfo info : fileSystem.listFolder(DbxPath.ROOT)) {
                DbxFile file = null;
                try {
                    Log.i(TAG, "Opening file "+info.path.getName());
                    file = fileSystem.open(info.path);
                    Log.i(TAG, "Opened file "+info.path.getName());
                    Log.i(TAG, "file "+info.path.getName() + " status: "+file.getSyncStatus().toString());
                    if (file.getSyncStatus().isCached) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeStream(file.getReadStream());
                        items.add(new PhotoItem(info, bitmap));
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.getLocalizedMessage());
                } finally {
                    if (file != null) {
                        file.close();
                    }
                }
            }

        } catch (DbxException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to retrieve files");
        } catch (IOException e) {
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
                        Log.i(TAG, "Closing creation");
                        out.close();
                        file.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Closing failed: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        } catch (DbxException e) {
            Log.e(TAG, "Closing failed: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
