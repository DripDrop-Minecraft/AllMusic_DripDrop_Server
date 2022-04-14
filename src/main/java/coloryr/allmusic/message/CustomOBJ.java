package coloryr.allmusic.message;

public class CustomOBJ {
    private String Info;

    public CustomOBJ() {
        Info = "外部歌曲";
    }

    public boolean check() {
        return Info == null;
    }

    public String getInfo() {
        return Info;
    }
}
