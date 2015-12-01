package com.droidcon.snaphack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FacebookConcealManager {
    private final String path;
    private final Crypto crypto;
    private final Entity entity;

    public FacebookConcealManager(Context context) {
        path = context.getFilesDir().getPath();
        this.crypto = new Crypto(
                new SharedPrefsBackedKeyChain(context),
                new SystemNativeCryptoLibrary());
        entity = new Entity("nick");
    }

    public void savePhoto(Bitmap imageBitmap, String filename) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(path + filename);
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);
        fileStream.close();
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
