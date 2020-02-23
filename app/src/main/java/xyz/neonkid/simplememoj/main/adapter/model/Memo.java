package xyz.neonkid.simplememoj.main.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import xyz.neonkid.simplememoj.main.util.RealmHelper;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class Memo extends RealmObject implements Parcelable {
    public static final String FIELD_ID = "id";

    @PrimaryKey
    private int id;

    @Required
    private String title;

    @Required
    private String content;

    @Required
    private String color;

    private RealmList<MemoImage> imgs;

    @Required
    private Date created;

    @Required
    private Date modified;

    public Memo() {}

    public Memo(String title, String content) {
        this.title = title;
        this.content = content;
        this.imgs = new RealmList<>();
    }

    public Memo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        created = (Date)in.readSerializable();
        modified = (Date)in.readSerializable();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTransactionImg(MemoImage mi) {
        RealmHelper.getInstance().insertMemoImage(this, mi);
    }

    public void setTransactionImgs(List<MemoImage> imgs) {
        this.imgs = new RealmList<>();

        for (MemoImage mi : imgs)
            RealmHelper.getInstance().insertMemoImage(this, mi);
    }

    public void setCreated(Date date) {
        this.created = date;
    }

    public void setModified(Date date) {
        this.modified = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return content.length() > 100 ?
                content.substring(0, content.length() / 2) + "..." : content;
    }

    public String getColor() { return color; }

    public RealmList<MemoImage> getImgs() {
        return imgs;
    }

    public RealmResults<MemoImage> getImgsResult() {
        return RealmHelper.getInstance().getMemoImages(id);
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public static final Creator<Memo> CREATOR = new Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel source) {
            return new Memo(source);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeTypedList(imgs);
        dest.writeSerializable(created);
        dest.writeSerializable(modified);
    }
}
