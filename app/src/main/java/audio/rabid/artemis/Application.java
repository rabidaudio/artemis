package audio.rabid.artemis;

import com.squareup.leakcanary.LeakCanary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import audio.rabid.artemis.models.Album;
import audio.rabid.artemis.models.Artist;
import audio.rabid.artemis.models.Track;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rapid.decoder.BitmapDecoder;

/**
 * Created by  charles  on 6/3/16.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        BitmapDecoder.initMemoryCache(this);
        BitmapDecoder.initDiskCache(this, 50 * 1024 * 1024);

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build());

//        Realm.getDefaultInstance().executeTransaction(this::loadTestData);
    }

    private void loadTestData(Realm realm){
        try {
            realm.deleteAll();
            Scanner s = new Scanner(getAssets().open("tracks.json")).useDelimiter("\\A");
            JSONArray array = new JSONArray(s.hasNext() ? s.next() : "[]");
            Map<String, Artist> artists = new HashMap<>();
            Map<String, Album> albums = new HashMap<>();
            Map<String, Integer> trackNumbers = new HashMap<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Artist artist = artists.get(o.getString("artist"));
                if(artist == null){
                    artist = realm.createOrUpdateObjectFromJson(Artist.class, new JSONObject()
                            .put("id", artists.size()+1)
                            .put("sortName", o.getString("artist"))
                            .put("name", o.getString("artist")));
                    artists.put(o.getString("artist"), artist);
                }
                Album album = albums.get(o.getString("album"));
                if(album == null){
                    int year = (int)Math.floor(Math.random()*35) + 1980;
                    album = realm.createOrUpdateObjectFromJson(Album.class, new JSONObject()
                            .put("id", albums.size()+1)
                            .put("name", o.getString("album"))
                            .put("artist", new JSONObject()
                                    .put("id", artist.getId())
                                    .put("sortName", artist.getSortName())
                                    .put("name", artist.getName()))
                            .put("year", year));
                    albums.put(o.getString("album"), album);
                    trackNumbers.put(o.getString("album"), 0);
                }
                int trackNumber = trackNumbers.get(o.getString("album")) + 1;
                trackNumbers.put(o.getString("album"), trackNumber);
                realm.createOrUpdateObjectFromJson(Track.class, new JSONObject()
                        .put("id", i)
                        .put("title", o.getString("title"))
                        .put("artist", new JSONObject()
                                .put("id", artist.getId())
                                .put("sortName", artist.getSortName())
                                .put("name", artist.getName()))
                        .put("album", new JSONObject()
                                .put("id", album.getId())
                                .put("name", album.getName())
                                .put("artist", new JSONObject()
                                        .put("id", artist.getId())
                                        .put("sortName", artist.getName())
                                        .put("name", artist.getName()))
                                .put("year", album.getYear()))
                        .put("trackNumber", trackNumber)
                        .put("length", (int)Math.round(Math.random()*360*1000) + (180*1000)));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
