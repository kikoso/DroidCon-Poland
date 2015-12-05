package com.droidcon.snaphack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.droidcon.snaphack.fragment.LoginFragment;
import com.droidcon.snaphack.fragment.PhotoListFragment;
import com.droidcon.snaphack.manager.CryptoManager;
import com.droidcon.snaphack.manager.KeyManager;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 234;
    private static final int REQUEST_IMAGE_CAPTURE_ENCRYPTED = 2344;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().add(R.id.content_main, new LoginFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String fileName = System.currentTimeMillis() + "_photo";

            CryptoManager externalFileManager = new CryptoManager(this, ShApplication.getInstance().getConfiguredStorageDirectory(), new KeyManager(this).read());
            try {
                if (requestCode == REQUEST_IMAGE_CAPTURE) {
                    externalFileManager.savePhoto(imageBitmap, fileName + ".jpg");
                }
                if (requestCode == REQUEST_IMAGE_CAPTURE_ENCRYPTED) {
                    externalFileManager.savePhotoEncrypted(imageBitmap, fileName + "_encrypted.jpg");
                }
            } catch (IOException e) {
            } catch (CryptoInitializationException e) {
            } catch (KeyChainException e) {
            }
        }
    }

    public void loggedIn() {
        getFragmentManager().beginTransaction().add(R.id.content_main, new PhotoListFragment()).addToBackStack(null).commit();
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void takePhotoEncrypted() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_ENCRYPTED);
        }
    }

    @Override
    public void onBackPressed() {
        int  count = getFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getFragmentManager().popBackStackImmediate();
        }else{
            super.onBackPressed();
        }
    }
}
