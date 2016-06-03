package audio.rabid.artemis.models;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by  charles  on 6/3/16.
 */

public class Artist extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;

}
