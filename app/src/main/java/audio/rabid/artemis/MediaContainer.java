package audio.rabid.artemis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by  charles  on 6/4/16.
 */

public class MediaContainer {

    ContentResolver contentResolver;

    public static boolean getPermissions(final Activity context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(context)
                        .setTitle("Allow us to access your music")
                        .setMessage("We need to access your files to be able to play your content")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getPermissions(context);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create().show();

            } else {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
            return false;
        }else{
            return true;
        }
    }

    public void getArtists(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

//        context.grantUriPermission("com.android.providers.media.MediaProvider", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            cursor.getColumnIndex(MediaStore.Audio.Media.CONTENT_TYPE);

            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                // ...process entry...


                Log.d("Asdf", "track: "+thisId+" : "+thisTitle);

            } while (cursor.moveToNext());
        }
        if(cursor != null) {
            cursor.close();
        }
    }

    public MediaContainer(ContentResolver contentResolver){
        this.contentResolver = contentResolver;


        /*
        Uri contentUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
         */

    }

}
