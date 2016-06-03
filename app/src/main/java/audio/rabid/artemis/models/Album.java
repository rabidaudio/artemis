package audio.rabid.artemis.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by  charles  on 6/3/16.
 */

public class Album extends RealmObject {

    @PrimaryKey
    private long id;

    private Artist artist;

    private String name;

    private int year;
}
