package soma.net.letsstudytogether.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mijeong on 2016-07-17.
 */
public class Study implements Parcelable{
    public int getStudyid() {
        return studyid;
    }

    public void setStudyid(int studyid) {
        this.studyid = studyid;
    }

    public String getStudytype() {
        return studytype;
    }

    public void setStudytype(String studytype) {
        this.studytype = studytype;
    }

    int studyid;
    String writer;
    String title;
    String studytype;
    String startdate;
    String enddate;
    String starttime;
    String endtime;
    int member;
    String location;
    int day;
    String description;
    String email;
    String phone;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return studytype;
    }

    public void setType(String type) {
        this.studytype = type;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(studyid);
        parcel.writeString(writer);
        parcel.writeString(title);
        parcel.writeString(studytype);
        parcel.writeString(startdate);
        parcel.writeString(enddate);
        parcel.writeString(starttime);
        parcel.writeString(endtime);
        parcel.writeInt(member);
        parcel.writeString(location);
        parcel.writeInt(day);
        parcel.writeString(description);
        parcel.writeString(email);
        parcel.writeString(phone);
    }
    public Study(){
        member = 0;
        description = null;
        startdate = null;
        enddate = null;
        starttime = null;
        endtime = null;
    }

    public Study(Parcel in) { //parcel 데이터로 전달된 것을 객체형으로 만드는 생성자
        studyid = in.readInt();
        writer = in.readString();
        title = in.readString();
        studytype = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        starttime = in.readString();
        endtime = in.readString();
        member = in.readInt();
        location = in.readString();
        day = in.readInt();
        description = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    public static final Creator<Study> CREATOR = new Creator<Study>() { //parcel 데이터를 객체로 다시 만들어 주는 단계.
        @Override
        public Study createFromParcel(Parcel in) {
            return new Study(in);
        }

        @Override
        public Study[] newArray(int size) {
            return new Study[size];
        }
    };
}
