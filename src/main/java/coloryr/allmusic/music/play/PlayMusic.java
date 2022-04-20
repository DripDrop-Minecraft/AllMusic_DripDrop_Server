package coloryr.allmusic.music.play;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.decoder.Bitstream;
import coloryr.allmusic.decoder.Header;
import coloryr.allmusic.music.api.SongInfo;
import coloryr.allmusic.music.lyric.LyricSave;
import coloryr.allmusic.music.lyric.ShowOBJ;
import coloryr.allmusic.utils.logs;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayMusic {

    public static final List<SongInfo> playList = new CopyOnWriteArrayList<>();
    public static int voteTime = 0;
    public static int musicAllTime = 0;
    public static int musicLessTime = 0;
    public static int musicNowTime = 0;
    public static SongInfo nowPlayMusic;

    public static LyricSave lyric;
    public static ShowOBJ nowLyric;
    public static int error;

    private static boolean isRun;
    private static final Queue<MusicObj> tasks = new ConcurrentLinkedQueue<>();

    public static void stop() {
        PlayMusic.clear();
        isRun = false;
    }

    public static void start() {
        Thread addT = new Thread(PlayMusic::task, "AllMusic_list");
        isRun = true;
        addT.start();
    }

    public static void addTask(MusicObj obj) {
        tasks.add(obj);
    }

    private static void task() {
        while (isRun) {
            try {
                MusicObj obj = tasks.poll();
                if (obj != null) {
                    if (obj.isUrl) {
                        addUrl(obj.url, obj.name);
                    } else {
                        addMusic((String) obj.sender, obj.name, obj.isDefault);
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                AllMusic.log.warning("歌曲处理出现问题");
                e.printStackTrace();
            }
        }
    }

    public static void addMusic(String ID, String player, boolean isList) {
        if (isHave(ID))
            return;
        String text = AllMusic.getMessage().getMusicPlay().getPlayerAdd();
        text = text.replace("%PlayerName%", player).replace("%MusicID%", ID);
        AllMusic.side.bqt(text);
        logs.logWrite("玩家：" + player + " 点歌：" + ID);
        try {
            SongInfo info = AllMusic.getMusicApi().getMusic(ID, player, isList);
            if (info != null) {
                playList.add(info);
                String data = AllMusic.getMessage().getMusicPlay().getAddMusic();
                data = data.replace("%MusicName%", info.getName())
                        .replace("%MusicAuthor%", info.getAuthor())
                        .replace("%MusicAl%", info.getAl())
                        .replace("%MusicAlia%", info.getAlia());
                AllMusic.side.bqt(data);
            } else {
                String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                AllMusic.side.bqt(data.replace("%MusicID%", ID));
            }
            if (AllMusic.getConfig().isPlayListSwitch()
                    && (PlayMusic.nowPlayMusic != null && PlayMusic.nowPlayMusic.isList())) {
                PlayMusic.musicLessTime = 1;
                if (!isList)
                    AllMusic.side.bqt(AllMusic.getMessage().getMusicPlay().getSwitch());
            }
            error = 0;
        } catch (Exception e) {
            if (isList) {
                error++;
            }
            AllMusic.log.warning("§d[AllMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }

    public static SongInfo getMusic(int index) {
        return playList.get(index);
    }

    public static int getSize() {
        return playList.size();
    }

    public static List<SongInfo> getList() {
        return new ArrayList<>(playList);
    }

    public static void clear() {
        playList.clear();
    }

    public static void remove(int index) {
        playList.remove(index);
    }

    public static String getAllList() {
        StringBuilder list = new StringBuilder();
        String a;
        SongInfo info;
        for (int i = 0; i < playList.size(); i++) {
            info = playList.get(i);
            a = AllMusic.getMessage().getMusicPlay().getListMusic().getItem();
            a = a.replace("%index%", "" + (i + 1))
                    .replace("%MusicName%", info.getName())
                    .replace("%MusicAuthor%", info.getAuthor())
                    .replace("%MusicAl%", info.getAl())
                    .replace("%MusicAlia%", info.getAlia());
            list.append(a).append("\n");
        }
        String temp = list.toString();
        return temp.substring(0, temp.length() - 1);
    }

    public static boolean isHave(String ID) {
        if (nowPlayMusic != null && nowPlayMusic.getID().equalsIgnoreCase(ID))
            return true;
        for (SongInfo item : playList) {
            if (item.getID().equalsIgnoreCase(ID))
                return true;
        }
        return false;
    }

    private static void addUrl(String arg, String player) {
        try {
            URL urlfile = new URL(arg);
            URLConnection con = urlfile.openConnection();
            // 得到音乐文件的总长度
            int b = con.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            int le = 0;
            if (h != null) {
                le = (int) h.total_ms(b);
            }
            String songName = AllMusic.getMessage().getCustom().getInfo() + getCustomSongId(arg);
            SongInfo info = new SongInfo(player, songName, arg, le);
            // 聊天栏展示“玩家xxx点歌：外部歌曲xxxxxxxx”
            AllMusic.side.bqt(AllMusic.getMessage().getMusicPlay().getPlayerAdd()
                    .replace("%PlayerName%", player).replace("%MusicID%", songName));
            logs.logWrite("玩家：" + player + " 点歌：" + songName);
            playList.add(info);
            // 聊天栏展示“音乐列表添加 外部歌曲xxxxxxxx | -- | -- |”
            AllMusic.side.bqt(AllMusic.getMessage().getMusicPlay().getAddMusic()
                    .replace("%MusicName%", info.getName())
                    .replace("%MusicAuthor%", info.getAuthor())
                    .replace("%MusicAl%", info.getAl())
                    .replace("%MusicAlia%", info.getAlia()));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }

    private static String getCustomSongId(String url) {
        String name = "";
        Pattern pattern = Pattern.compile("contentId=\\d+");
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            name = url.substring(matcher.start(), matcher.end()).replace("contentId=", "");
        }
        return name;
    }
}

