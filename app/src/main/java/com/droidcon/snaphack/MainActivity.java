package com.droidcon.snaphack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LINK_TO_DBX = 43;
    private static final int REQUEST_IMAGE_CAPTURE = 234;
    private static final String TAG = "SnapHack";
    private DropboxManager dropboxManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().replace(R.id.content_main, new LoginFragment()).commit();
        dropboxManager = ShApplication.getInstance().getDropboxManager();
        dropboxManager.startLink(this, REQUEST_LINK_TO_DBX);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            dropboxManager.savePhoto(imageBitmap);
        }

        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "We're good");
                dropboxManager.initialise();
            } else {
                Log.e(TAG, "Can't link to Dropbox");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void loggedIn() {
        getFragmentManager().beginTransaction().replace(R.id.content_main, new ListFragment()).commit();
    }

    public void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
