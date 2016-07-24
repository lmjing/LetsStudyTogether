package soma.net.letsstudytogether.model;

/**
 * Created by mijeong on 2016-07-21.
 */
public class UserTime {
    String id;
    String installtime;
    String playtime;
    String endtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstalltime() {
        return installtime;
    }

    public void setInstalltime(String installtime) {
        this.installtime = installtime;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
