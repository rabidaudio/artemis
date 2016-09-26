package audio.rabid.artemis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
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

//        context.grantUriPermission("com.android.providers.media.MediaProvider", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
                        boolean isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) > 0;
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



                    /*

    int FIELD_TYPE_BLOB = 4;
    int FIELD_TYPE_FLOAT = 2;
    int FIELD_TYPE_INTEGER = 1;
    int FIELD_TYPE_NULL = 0;
    int FIELD_TYPE_STRING = 3;

09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/Asdf: track: 71237 : Almost Here
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 0: _id -> 1
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 1: _data -> 3
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 2: _display_name -> 3
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 3: _size -> 1
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 4: mime_type -> 3
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 5: date_added -> 1
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 6: is_drm -> 1
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 7: date_modified -> 1
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 8: title -> 3
09-26 01:55:25.163 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 9: title_key -> 3
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 10: duration -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 11: artist_id -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 12: composer -> 3
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 13: album_id -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 14: track -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 15: year -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 16: is_ringtone -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 17: is_music -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 18: is_alarm -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 19: is_notification -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 20: is_podcast -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 21: bookmark -> 0
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 22: album_artist -> 3
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 23: artist_id:1 -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 24: artist_key -> 3
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 25: artist -> 3
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 26: album_id:1 -> 1
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 27: album_key -> 3
09-26 01:55:25.164 7205-7372/audio.rabid.dev.artemis.debug D/asdf: column 28: album -> 3
                 */

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


    public static boolean getPermissions(final Activity context, int permissionRequestCode){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(context)
                        .setTitle("Allow us to access your music")
                        .setMessage("We need to access your files to be able to play your content")
                        .setPositiveButton("OK", (dialog, which) -> getPermissions(context, permissionRequestCode))
                        .setNegativeButton("Cancel", null)
                        .create().show();

            } else {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionRequestCode);
            }
            return false;
        }else{
            return true;
        }
    }
}
