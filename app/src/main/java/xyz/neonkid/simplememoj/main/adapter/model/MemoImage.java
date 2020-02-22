package xyz.neonkid.simplememoj.main.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Neon K.I.D on 2/16/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoImage extends RealmObject implements Parcelable {
    public static final String FIELD_ID = "id";

    @PrimaryKey
    private int id;

    @Required
    private String uri;

    @LinkingObjects("imgs")
    public final RealmResults<Memo> memo = null;

    public MemoImage() {}

    public MemoImage(String uri) {
        this.uri = uri;
    }

    public MemoImage(Parcel in) {
        this.id = in.readInt();
        this.uri = in.readString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getId() { return id; }

    public String getUri() { return uri; }

    public static final Creator<MemoImage> CREATOR = new Creator<MemoImage>() {
        @Override
        public MemoImage createFromParcel(Parcel source) {
            return new MemoImage(source);
        }

        @Override
        public MemoImage[] newArray(int size) {
            return new MemoImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uri);
    }
}
