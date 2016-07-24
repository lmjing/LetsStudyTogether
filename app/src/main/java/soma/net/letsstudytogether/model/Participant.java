package soma.net.letsstudytogether.model;

/**
 * Created by inbiz02 on 2016-01-13.
 */
public class Participant {
    int study;
    String name;
    String userid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getStudy() {
        return study;
    }

    public void setStudy(int study) {
        this.study = study;
    }
}
