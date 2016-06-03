package audio.rabid.artemis;

import android.net.Uri;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by  charles  on 6/3/16.
 */

public class Track extends RealmObject {

    @PrimaryKey
    private long id;

    private String artist;
    private String album;
    private String title;
    private long length;

    @Required
    private String uri;

    public long getId() {
        return id;
    }

    public Uri getUri(){
        return Uri.parse(uri);
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public long getLength() {
        return length;
    }
}
