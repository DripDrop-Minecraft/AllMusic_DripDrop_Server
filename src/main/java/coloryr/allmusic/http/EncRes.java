package coloryr.allmusic.http;

public class EncRes {
    public String params;
    public String encSecKey;

    public EncRes(String params, String encSecKey) {
        this.encSecKey = encSecKey;
        this.params = params;
    }
}
