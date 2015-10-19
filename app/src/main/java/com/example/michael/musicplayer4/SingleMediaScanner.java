package com.example.michael.musicplayer4;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

import java.io.File;

public class SingleMediaScanner implements MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File mFile;

    // This class registers File F with media store

    public SingleMediaScanner(Context context, File f) {
        mFile = f;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);

        /*
        File file = new File(Environment.getExternalStorageDirectory() + "/Download");
        for(File f : file.listFiles()) {
            mMs.scanFile(f.getAbsolutePath(), null);
        }*/
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
    }

}