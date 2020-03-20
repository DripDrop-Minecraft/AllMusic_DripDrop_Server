package Color_yr.ALLMusic.MusicAPI;

import Color_yr.ALLMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.ALLMusic.MusicAPI.SongLyric.LyricDo;
import Color_yr.ALLMusic.MusicAPI.SongSearch.SearchPage;

public interface IMusic {
    SongInfo GetMusic(String ID, String player, boolean isList);

    String GetPlayUrl(String ID);

    void SetList(String ID, Object sender);

    LyricDo getLyric(String ID);

    SearchPage Search(String[] name);

    String GetListMusic();
}
