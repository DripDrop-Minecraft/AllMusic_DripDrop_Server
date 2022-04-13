package coloryr.allmusic.message;

public class CustomOBJ {
    private String Info;

    public CustomOBJ() {
        Info = "来自网易云音乐之外的曲目";
    }

    public boolean check(){
        if(Info == null)
            return true;

        return false;
    }

    public String getInfo() {
        return Info;
    }
}
