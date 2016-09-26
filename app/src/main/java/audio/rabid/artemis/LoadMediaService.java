package audio.rabid.artemis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import audio.rabid.artemis.models.Track;
import io.realm.Realm;


public class LoadMediaService extends IntentService {

    public LoadMediaService() {
        super("LoadMediaService");
    }

    public static void start(Activity context, int permissionRequestCode) {
        if(getPermissions(context, permissionRequestCode)) {
            Intent intent = new Intent(context, LoadMediaService.class);
            context.startService(intent);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ContentResolver cr = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = null;
        try {
            cursor = cr.query(uri, null, null, null, null);
            if (cursor == null) {
                Log.e(LoadMediaService.class.getSimpleName(), "Problem getting media cursor");
            } else if (!cursor.moveToFirst()) {
                Log.e(LoadMediaService.class.getSimpleName(), "No media on device");
            } else {
                try {
                    JSONArray tracks = new JSONArray();
                    do {
                        boolean isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) != 0;
                        if(isMusic) {
                            tracks.put(createTrackJSONFromCursor(cursor));
                        }
                    } while (cursor.moveToNext());
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.deleteAll();
                    realm.createOrUpdateAllFromJson(Track.class, tracks);
                    realm.commitTransaction();
                }catch (JSONException e){
                    Log.e(LoadMediaService.class.getSimpleName(), "Problem loading media", e);
                }
            }
        }finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    private static JSONObject createTrackJSONFromCursor(Cursor cursor) throws JSONException {
        JSONObject artist = new JSONObject()
                .put("id", cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)))
                .put("name", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)))
                .put("sortName", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_KEY)));

        JSONObject album = new JSONObject()
                .put("id", cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)))
                .put("artist", artist)
                .put("name", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)))
                .put("sortName", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY)))
                .put("year", cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));

        return new JSONObject()
                .put("id", cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)))
                .put("uri", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))
                .put("dateAdded", 1000L*cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)))
                .put("dateModified", 1000L*cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)))
                .put("title", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)))
                .put("titleKey", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE_KEY)))
                .put("length", cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)))
                .put("trackNumber", cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)))
                .put("artist", artist)
                .put("album", album);
    }


    private static boolean getPermissions(final Activity context, int permissionRequestCode){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.permission_primer_title)
                    .setMessage(R.string.permission_primer_message)
                    .setPositiveButton(R.string.permission_primer_ok, (dialog, which) -> {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionRequestCode);
                    })
                    .setNegativeButton(R.string.permission_primer_cancel, null)
                    .create().show();

            return false;
        }else{
            return true;
        }
    }
}
