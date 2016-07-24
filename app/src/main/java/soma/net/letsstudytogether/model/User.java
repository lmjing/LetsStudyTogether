package soma.net.letsstudytogether.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lmjin_000 on 2016-07-14.
 */
public class User implements Parcelable {
    String name;
    String id;
    String email;
    String phone;
    int type;
    String registerid;

    public String getRegisterid() {
        return registerid;
    }

    public void setRegisterid(String registerid) {
        this.registerid = registerid;
    }

    public String getPhone() {
        return phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public User(){ }

    public User(Parcel in) { //parcel 데이터로 전달된 것을 객체형으로 만드는 생성자
        name = in.readString();
        id = in.readString();
        email = in.readString();
        phone = in.readString();
        type = in.readInt();
        registerid = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() { //parcel 데이터를 객체로 다시 만들어 주는 단계.
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {   //객체를 parcel 데이터로 만드는 단계
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeInt(type);
        dest.writeString(registerid);
    }
}