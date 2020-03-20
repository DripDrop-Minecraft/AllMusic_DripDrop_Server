package Color_yr.ALLMusic.Message;

public class AddMusicOBJ {
    private String ListFull;
    private String BanMusic;
    private String ExistMusic;
    private String Success;
    private String NoPlayer;
    private String NoID;

    public AddMusicOBJ() {
        ListFull = "§d[ALLMusic]§c错误，队列已满";
        BanMusic = "§d[ALLMusic]§c错误，这首歌被禁点了";
        ExistMusic = "§d[ALLMusic]§c错误，这首歌已经存在了";
        Success = "§d[ALLMusic]§2点歌成功";
        NoPlayer = "§d[ALLMusic]§c没有播放的玩家";
        NoID = "§d[ALLmusic]§c错误，请输入歌曲数字ID";
    }

    public String getNoID() {
        return NoID;
    }

    public String getNoPlayer() {
        return NoPlayer;
    }

    public String getSuccess() {
        return Success;
    }

    public String getExistMusic() {
        return ExistMusic;
    }

    public String getBanMusic() {
        return BanMusic;
    }

    public String getListFull() {
        return ListFull;
    }
}
