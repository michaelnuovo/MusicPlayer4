package com.example.michael.musicplayer4;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.michael.musicplayer3.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<com.example.michael.musicplayer4.SongObject> songObectList = new ArrayList<>();
    MediaPlayer mMediaPlayer = new MediaPlayer();
    SongListAdapter adapter;
    ListView songList; // Widgets cannot be instantiated until after setContent() is called
    SlidingUpPanelLayout panel;
    LinearLayout dragView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        panel.setOverlayed(false); // Panel pushes content upwards to prevent view overlap
        //panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN); // Sets panel state to hidden
        Log.v("TAG", String.valueOf(panel.getPanelState())); // Logs string value of panel state

        songList = (ListView)findViewById(R.id.song_list); // Set's reference to view object
        Cursor mCursor = PointToMetaData(); // Points cursor to meta data
        CreatSongObjectList(mCursor); // Creates list array of song objects
        adapter = new SongListAdapter(this, R.layout.listview_item_row, songObectList); // Maps song objects to list view
        songList.setAdapter(adapter); // Set target list view for adapter logic
        SetListItemClickListener(); // Listens for list item click events
        //PanelStateListener(); // Listen for panel state changes; sets global variable panelState

        //panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        //panel.getPanelState();
        //panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        //panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        //panel.setAnchorPoint(0.5f); // Inter mediate slide height
        //panel.setPanelHeight(0); // attribute version of method: "umanoPanelHeight" https://github.com/umano/AndroidSlidingUpPanel/blob/master/README.md
        //panel.setPanelHeight(0);
    }

    @Override
    public void onBackPressed() {
        if(String.valueOf(panel.getPanelState()) == "EXPANDED"){
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED); // Sets panel state to collapsed
        }
    }

    private Cursor PointToMetaData() {

        // Set URI
        Uri contentURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Set projection parameter for getContentResolver query
        String[] projection = {
                //MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Media.ALBUM,    // 0
                MediaStore.Audio.Media.ARTIST,   // 1
                MediaStore.Audio.Media.TITLE,    // 2
                MediaStore.Audio.Media.DATA,     // 3
                MediaStore.Audio.Media.DURATION, // 4
                MediaStore.Audio.Media.ALBUM_ID  // 5
        };

        // Set selection parameter for getContentResolver query
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        // Set sort order paramter
        String order = MediaStore.Audio.Media.TITLE + " ASC";

        // Run getContentResolver query
        final Cursor mCursor = getContentResolver().query(
                contentURI, projection, selection, null, order
        );

        return mCursor;

    }

    private void CreatSongObjectList(Cursor mCursor){
        // Use getCount if you want to use an array instead of a list
        // int count = mCursor.getCount();

        if (mCursor.moveToFirst()) {
            do {


                SongObject songObject = new SongObject();
                String[] albumID = {mCursor.getString(5)};
                Log.v("TAG Album Id test", String.valueOf(albumID));
                songObject.albumArtURI = GetAlbumArtURI(albumID); // Return album art URI
                songObject.album = mCursor.getString(0);
                if(mCursor.getString(1).equals("<unknown>")){songObject.artist = "Unknown Artist";}
                else {songObject.artist = mCursor.getString(1);}
                songObject.title = mCursor.getString(2);
                songObject.data = mCursor.getString(3);
                songObject.duration = mCursor.getString(4);
                songObectList.add(songObject);
                Log.v("TAG","Song object added to songObectList");

            } while (mCursor.moveToNext());
        }

        mCursor.close();
    }

    private String GetAlbumArtURI(String[] albumID){

        File file = new File(Environment.getExternalStorageDirectory() + "/Download/Song.mp3");
        SingleMediaScanner singleMediaScanner = new SingleMediaScanner(this, file);


        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                albumID,
                null
        );

        /*
        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );*/

        //return mCursor.getString(0);

        if(mCursor.moveToFirst()) {
            return mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        } else { return null; }

    }

    private void SetListItemClickListener() {

        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                //Bitmap bitmap = BitmapFactory.decodeFile(songObectList.get(arg2).albumArtURI); // Convert URI to bitmap object
                //BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap); // Set drawable object
                //dragView.setBackgroundDrawable(ob); // Set bit map to view

                LinearLayout dragView = (LinearLayout) findViewById(R.id.dragView);
                Bitmap bitmap = BitmapFactory.decodeFile(songObectList.get(arg2).albumArtURI); // Create bitmap
                Log.v("TAG bitmap",String.valueOf(bitmap)); // prints android.graphics.Bitmap@e84ede4
                Drawable drawable = new BitmapDrawable(getResources(), bitmap); // Convert bitmap to drawable
                dragView.setBackground(drawable); // Set drawable as background (layouts to not accept bitmaps as backgrounds)

                // Attempt to invoke virtual method
                // 'void android.widget.Linehow toarLayout.setBackgroundDrawable(android.graphics.drawable.Drawable)'
                // on a null object reference

                //dragView.setImageBitmap(BitmapFactory.decodeFile(songObectList.get(arg2).albumArtURI));
                //dragView.setBackgroundDrawable(BitmapFactory.decodeFile(songObectList.get(arg2).albumArtURI));
                //dragView.setBackgroundResource(BitmapFactory.decodeFile(songObectList.get(arg2).albumArtURI));

                //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


                if (String.valueOf(panel.getPanelState()) == "COLLAPSED"){
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }

                // Play clicked song
                try {
                    playSong(songObectList.get(arg2).data);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void playSong(String path) throws IllegalArgumentException,
            IllegalStateException, IOException {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();

        Log.w("TAG - ", "path: " + path);

        //path = extStorageDirectory + File.separator + path;

        Log.w("TAG - ", "path: " + path);

        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}