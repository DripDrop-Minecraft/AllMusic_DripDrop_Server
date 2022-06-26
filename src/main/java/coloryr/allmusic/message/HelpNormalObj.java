package coloryr.allmusic.message;

public class HelpNormalObj {
    private String Head;
    private String Base;
    private String Stop;
    private String List;
    private String Vote;
    private String NoMusic;
    private String Search;
    private String Select;
    private String Hud1;
    private String Hud2;
    private String Hud3;

    public HelpNormalObj() {
        Head = "§d[AllMusic]§2帮助手册";
        Base = "§d[AllMusic]§2点歌：/music [网易云音乐歌曲ID或链接/MyFreeMP3网站歌曲链接]";
        Stop = "§d[AllMusic]§2停止播放歌曲：/music stop";
        List = "§d[AllMusic]§2查看歌曲队列：/music list";
        Vote = "§d[AllMusic]§2投票切歌：/music vote";
        NoMusic = "§d[AllMusic]§2不再参与点歌：/music nomusic ";
        Search = "§d[AllMusic]§2在网易云曲库中搜索歌曲：/music search [歌名]";
        Select = "§d[AllMusic]§2选择歌曲：/music select [歌曲序列号]";
        Hud1 = "§d[AllMusic]§2启用或关闭Hud：/music hud enable [位置]";
        Hud2 = "§d[AllMusic]§2设置某个Hud的位置：/music hud [位置] [x] [y]";
        Hud3 = "§d[AllMusic]§2使用/music hud picsize [尺寸] 设置图片尺寸";
    }

    public boolean check(){
        if(Head == null)
            return true;
        if(Base == null)
            return true;
        if(Stop == null)
            return true;
        if(List == null)
            return true;
        if(Vote == null)
            return true;
        if(NoMusic == null)
            return true;
        if(Search == null)
            return true;
        if(Select == null)
            return true;
        if(Hud1 == null)
            return true;
        if(Hud2 == null)
            return true;

        return false;
    }

    public String getBase() {
        return Base;
    }

    public String getHud1() {
        return Hud1;
    }

    public String getHud2() {
        return Hud2;
    }

    public String getList() {
        return List;
    }

    public String getNoMusic() {
        return NoMusic;
    }

    public String getSearch() {
        return Search;
    }

    public String getSelect() {
        return Select;
    }

    public String getStop() {
        return Stop;
    }

    public String getVote() {
        return Vote;
    }

    public String getHead() {
        return Head;
    }
}
