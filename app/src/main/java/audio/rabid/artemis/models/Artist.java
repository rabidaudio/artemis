package audio.rabid.artemis.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by  charles  on 6/3/16.
 */

public class Artist extends RealmObject {

    public Artist(){}

    public Artist(String name){
        this.name = name;
    }

    @PrimaryKey
    private long id;

    private String name;

    private String sortName;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSortName(){
        return name;
    }

    public long getAlbumCount(){
        return Realm.getDefaultInstance().where(Album.class).equalTo("artist.id", id).count();
    }

//    public RealmResults<Album> getAlbums(){
//        return Realm.getDefaultInstance().where(Album.class).equalTo("artist.id", id).findAll();
//    }

    public static rx.Observable<RealmResults<Artist>> getAllBySortName(){
        return Realm.getDefaultInstance().where(Artist.class)
                .findAllSortedAsync("sortName", Sort.ASCENDING)
                .asObservable()
                .filter(RealmResults::isLoaded)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
