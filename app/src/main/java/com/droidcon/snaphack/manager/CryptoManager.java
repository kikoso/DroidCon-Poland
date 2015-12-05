package com.droidcon.snaphack.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CryptoManager {
    private final String path;
    private final Crypto crypto;
    private final Entity entity;

    public CryptoManager(Context context, String path, String password) {
        this.path = path;
        this.crypto = new Crypto(
                new SharedPrefsBackedKeyChain(context),
                new SystemNativeCryptoLibrary());
        entity = new Entity(password);
        checkPathExists();
    }

    private void checkPathExists() {
        File file = new File(path);
        if(!file.exists())
        {
            file.mkdir();
        }
    }

    public void savePhoto(Bitmap imageBitmap, String filename) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(path + filename);
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);
        fileStream.close();
    }

    public Bitmap readPhoto(String filename) throws IOException {
        FileInputStream fileStream = new FileInputStream(path + filename);
        Bitmap bitmap = BitmapFactory.decodeStream(fileStream);
        fileStream.close();
        return bitmap;
    }

    public void savePhotoEncrypted(Bitmap imageBitmap, String filename) throws KeyChainException, CryptoInitializationException, IOException {
        FileOutputStream fileStream = new FileOutputStream(path + filename);
        OutputStream outputStream = crypto.getCipherOutputStream(fileStream, entity);
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
    }

    public Bitmap decryptPhoto(String filename) throws IOException, CryptoInitializationException, KeyChainException {
        FileInputStream fileStream = new FileInputStream(path + filename);
        InputStream inputStream = crypto.getCipherInputStream(fileStream, entity);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return bitmap;
    }
}
