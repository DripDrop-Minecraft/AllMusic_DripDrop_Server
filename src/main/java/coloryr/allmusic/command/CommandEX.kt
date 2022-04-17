package coloryr.allmusic.command

import coloryr.allmusic.AllMusic
import coloryr.allmusic.hud.HudUtils
import coloryr.allmusic.music.play.MusicObj
import coloryr.allmusic.music.play.MusicSearch
import coloryr.allmusic.music.play.PlayMusic
import coloryr.allmusic.music.search.SearchOBJ
import coloryr.allmusic.music.search.SearchPage
import coloryr.allmusic.utils.Function

object CommandEX {
    private fun searchMusic(sender: Any, name: String, args: Array<String?>, isDefault: Boolean) {
        MusicSearch.addSearch(MusicObj().apply {
            this.sender = sender
            this.name = name
            this.args = args
            this.isDefault = isDefault
        })
    }

    private fun addMusic(sender: Any, name: String, args: Array<String?>) {
        if (args.isNotEmpty()) {
            val musicID = returnMusicIDOrOriginalContent(args[0])
            if (Function.isInteger(musicID)) {
                tryPlayingMusicFromNetEase(musicID, name, sender)
            } else {
                myFreeMP3(musicID, name, sender)
            }
        }
    }

    private fun playMusicFromNetEase(musicID: String, name: String, sender: Any) {
        if (AllMusic.getConfig().isUseCost && AllMusic.vault != null
            && !AllMusic.vault.check(name, AllMusic.getConfig().addMusicCost)
        ) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().cost.noMoney)
            return
        }
        AllMusic.getConfig().RemoveNoMusicPlayer(name)
        if (AllMusic.side.NeedPlay()) {
            PlayMusic.addTask(MusicObj().apply {
                this.sender = musicID
                this.name = name
                this.isDefault = false
            })
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.success)
            if (AllMusic.getConfig().isUseCost && AllMusic.vault != null) {
                AllMusic.vault.cost(
                    name, AllMusic.getConfig().addMusicCost,
                    AllMusic.getMessage().cost.addMusic
                        .replace("%Cost%", "" + AllMusic.getConfig().addMusicCost)
                )
            }
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.noPlayer)
        }
    }

    private fun tryPlayingMusicFromNetEase(musicID: String, name: String, sender: Any) {
        AllMusic.side.apply {
            when {
                PlayMusic.getSize() >= AllMusic.getConfig().maxList -> sendMessage(sender, AllMusic.getMessage().addMusic.listFull)
                AllMusic.getConfig().banMusic.contains(musicID) -> sendMessage(sender, AllMusic.getMessage().addMusic.banMusic)
                PlayMusic.isHave(musicID) -> sendMessage(sender, AllMusic.getMessage().addMusic.existMusic)
                else -> playMusicFromNetEase(musicID, name, sender)
            }
        }
    }

    private fun returnMusicIDOrOriginalContent(arg: String?): String = when {
        arg.isNullOrEmpty() -> ""
        arg.contains("id=") && !arg.contains("/?userid") -> {
            if (arg.contains("&user")) {
                Function.getString(
                    arg,
                    "id=",
                    "&user"
                )
            } else {
                Function.getString(arg, "id=", null)
            }
        }
        arg.contains("song/") -> {
            if (arg.contains("/?userid")) {
                Function.getString(
                    arg,
                    "song/",
                    "/?userid"
                )
            } else {
                Function.getString(arg, "song/", null)
            }
        }
        else -> arg
    }

    private fun myFreeMP3(url: String?, name: String, sender: Any) {
        // 如果传入的参数不是网易云音乐的曲目链接或者曲目数字ID，就走外部曲目链接的解析逻辑
        // 本方法适配的是网站 http://tools.liumingye.cn/music/?page=searchPage 的歌曲链接
        // 并且不能解析播放FLAC的音源
        url?.apply {
            val msg = if (contains("listenSong.do") && contains("contentId")
                && contains("toneFlag") && contains("&resourceType=2&")
            ) {
                val obj = MusicObj().apply {
                    isUrl = true
                    this.url = url
                    this.name = name
                }
                PlayMusic.addTask(obj)
                AllMusic.getMessage().addMusic.success
            } else {
                AllMusic.getMessage().addMusic.badUrl
            }
            AllMusic.side.sendMessage(sender, msg)
        }
    }

    private fun showHelpInfo(sender: Any) {
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().help.normal.head)
        AllMusic.side.sendMessageSuggest(
            sender, AllMusic.getMessage().help.normal.base,
            AllMusic.getMessage().click.Check, "/music "
        )
        AllMusic.side.sendMessageRun(
            sender, AllMusic.getMessage().help.normal.stop,
            AllMusic.getMessage().click.This, "/music stop"
        )
        AllMusic.side.sendMessageRun(
            sender, AllMusic.getMessage().help.normal.list,
            AllMusic.getMessage().click.This, "/music list"
        )
        AllMusic.side.sendMessageRun(
            sender, AllMusic.getMessage().help.normal.vote,
            AllMusic.getMessage().click.This, "/music vote"
        )
        AllMusic.side.sendMessageRun(
            sender, AllMusic.getMessage().help.normal.noMusic,
            AllMusic.getMessage().click.This, "/music nomusic"
        )
        AllMusic.side.sendMessageSuggest(
            sender, AllMusic.getMessage().help.normal.search,
            AllMusic.getMessage().click.Check, "/music search "
        )
        AllMusic.side.sendMessageSuggest(
            sender, AllMusic.getMessage().help.normal.select,
            AllMusic.getMessage().click.Check, "/music select "
        )
        AllMusic.side.sendMessageSuggest(
            sender, AllMusic.getMessage().help.normal.hud1,
            AllMusic.getMessage().click.Check, "/music hud enable "
        )
        AllMusic.side.sendMessageSuggest(
            sender, AllMusic.getMessage().help.normal.hud2,
            AllMusic.getMessage().click.Check, "/music hud "
        )
    }

    private fun showHelpInfoForAdmin(sender: Any, name: String) {
        if (AllMusic.getConfig().admin.contains(name)) {
            AllMusic.side.sendMessageRun(
                sender, AllMusic.getMessage().help.admin.reload,
                AllMusic.getMessage().click.This, "/music reload"
            )
            AllMusic.side.sendMessageRun(
                sender, AllMusic.getMessage().help.admin.next,
                AllMusic.getMessage().click.This, "/music next"
            )
            AllMusic.side.sendMessageSuggest(
                sender, AllMusic.getMessage().help.admin.ban,
                AllMusic.getMessage().click.Check, "/music ban "
            )
            AllMusic.side.sendMessageSuggest(
                sender, AllMusic.getMessage().help.admin.delete,
                AllMusic.getMessage().click.Check, "/music delete "
            )
            AllMusic.side.sendMessageSuggest(
                sender, AllMusic.getMessage().help.admin.addList,
                AllMusic.getMessage().click.Check, "/music addlist "
            )
            AllMusic.side.sendMessageRun(
                sender, AllMusic.getMessage().help.admin.clearList,
                AllMusic.getMessage().click.This, "/music clearlist"
            )
            AllMusic.side.sendMessageRun(
                sender, AllMusic.getMessage().help.admin.login,
                AllMusic.getMessage().click.This, "/music login"
            )
        }
    }

    private fun stopMusic(sender: Any, name: String) {
        AllMusic.side.clearHud(name)
        AllMusic.side.send("[Stop]", name, false)
        HudUtils.clearHud(name)
        AllMusic.removeNowPlayPlayer(name)
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.stopPlay)
    }

    private fun getMusicList(sender: Any) {
        if (PlayMusic.nowPlayMusic == null || PlayMusic.nowPlayMusic.isNull) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.noMusic)
        } else {
            var info = AllMusic.getMessage().musicPlay.play
            info = info.replace("%MusicName%", PlayMusic.nowPlayMusic.name)
                .replace("%MusicAuthor%", PlayMusic.nowPlayMusic.author)
                .replace("%MusicAl%", PlayMusic.nowPlayMusic.al)
                .replace("%MusicAlia%", PlayMusic.nowPlayMusic.alia)
                .replace("%PlayerName%", PlayMusic.nowPlayMusic.call)
            AllMusic.side.sendMessage(sender, info)
        }
        if (PlayMusic.getSize() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.noPlay)
        } else {
            AllMusic.side.sendMessage(
                sender, AllMusic.getMessage().musicPlay.listMusic.head
                    .replace("&Count&", "" + PlayMusic.getSize())
            )
            AllMusic.side.sendMessage(sender, PlayMusic.getAllList())
        }
    }

    private fun voteToChangeMusic(sender: Any, name: String) {
        if (AllMusic.getConfig().isNeedPermission && AllMusic.side.checkPermission(name, "AllMusic.vote")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.noPermission)
            return
        }
        if (PlayMusic.getSize() == 0 && AllMusic.getConfig().playList.isEmpty()) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.noPlay)
        } else if (PlayMusic.voteTime == 0) {
            PlayMusic.voteTime = 30
            AllMusic.addVote(name)
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.doVote)
            val data = AllMusic.getMessage().vote.bq
            AllMusic.side.bq(data.replace("%PlayerName%", name))
        } else if (PlayMusic.voteTime > 0) {
            if (!AllMusic.containVote(name)) {
                AllMusic.addVote(name)
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.agree)
                var data = AllMusic.getMessage().vote.bqAgree
                data = data.replace("%PlayerName%", name)
                    .replace("%Count%", "" + AllMusic.getVoteCount())
                AllMusic.side.bq(data)
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.arAgree)
            }
        }
        AllMusic.getConfig().RemoveNoMusicPlayer(name)
    }

    private fun setNoMusic(sender: Any, name: String) {
        AllMusic.side.apply {
            send("[Stop]", name, false)
            clearHud(name)
            AllMusic.getConfig().AddNoMusicPlayer(name)
            sendMessage(sender, AllMusic.getMessage().musicPlay.noPlayMusic)
        }
    }

    private fun searchSpecifiedMusic(sender: Any, name: String, args: Array<String?>) {
        if (AllMusic.getConfig().isNeedPermission && AllMusic.side.checkPermission(name, "AllMusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer)
            return
        }
        if (AllMusic.getConfig().isUseCost && AllMusic.vault != null) {
            if (!AllMusic.vault.check(name, AllMusic.getConfig().searchCost)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().cost.noMoney)
                return
            }
            AllMusic.vault.cost(
                name, AllMusic.getConfig().addMusicCost,
                AllMusic.getMessage().cost.addMusic
                    .replace("%Cost%", "" + AllMusic.getConfig().addMusicCost)
            )
        }
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.startSearch)
        searchMusic(sender, name, args, false)
    }

    private fun selectSpecifiedMusic(sender: Any, name: String, args: Array<String?>) {
        if (AllMusic.getConfig().isNeedPermission && AllMusic.side.checkPermission(name, "AllMusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer)
            return
        }
        val obj = AllMusic.getSearch(name)
        if (obj == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noSearch)
        } else if (args[1]!!.isNotEmpty() && Function.isInteger(args[1])) {
            val a = args[1]!!.toInt()
            if (a == 0) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.errorNum)
                return
            }
            val ID = arrayOfNulls<String>(1)
            ID[0] = obj.getSong(obj.page * 10 + (a - 1))
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.chose.replace("%Num%", "" + a))
            addMusic(sender, name, ID)
            AllMusic.removeSearch(name)
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.errorNum)
        }
        return
    }

    private fun gotoNextPage(sender: Any, name: String) {
        if (AllMusic.getConfig().isNeedPermission && AllMusic.side.checkPermission(name, "AllMusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer)
            return
        }
        val obj = AllMusic.getSearch(name)
        if (obj == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noSearch)
        } else if (obj.nextPage()) {
            showSearch(sender, obj)
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.cantNext)
        }
        return
    }

    private fun gotoLastPage(sender: Any, name: String) {
        if (AllMusic.getConfig().isNeedPermission && AllMusic.side.checkPermission(name, "AllMusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer)
            return
        }
        val obj = AllMusic.getSearch(name)
        if (obj == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noSearch)
        } else if (obj.lastPage()) {
            showSearch(sender, obj)
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.cantLast)
        }
        return
    }

    private fun showHud(sender: Any, name: String, args: Array<String?>) {
        if (args.size == 1) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error)
        } else {
            if (args[1].equals("enable", ignoreCase = true)) {
                if (args.size == 3) {
                    try {
                        val pos: String = args[2]?.lowercase() ?: ""
                        val temp = HudUtils.setHudEnable(name, pos)
                        AllMusic.side.sendMessage(
                            sender, AllMusic.getMessage().hud.state
                                .replace("%State%", if (temp) "启用" else "关闭")
                                .replace("%Hud%", AllMusic.getMessage().hudList.Get(pos))
                        )
                    } catch (e: Exception) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error)
                    }
                } else {
                    val temp = HudUtils.setHudEnable(name, null)
                    AllMusic.side.sendMessage(
                        sender, AllMusic.getMessage().hud.state
                            .replace("%State%", if (temp) "启用" else "关闭")
                            .replace("%Hud%", AllMusic.getMessage().hudList.all)
                    )
                }
            } else if (args[1].equals("reset", ignoreCase = true)) {
                HudUtils.reset(name)
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.reset)
            } else if (args.size != 4) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error)
            } else {
                try {
                    val obj = HudUtils.setHudPos(name, args[1], args[2], args[3])
                    if (obj == null) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error)
                    } else {
                        val temp = AllMusic.getMessage().hud.set
                            .replace("%Hud%", args[1]!!)
                            .replace("%x%", args[2]!!)
                            .replace("%y%", args[3]!!)
                        AllMusic.side.sendMessage(sender, temp)
                    }
                } catch (e: Exception) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error)
                }
            }
        }
        return
    }

    private fun executeAdminCommands(sender: Any, name: String, args: Array<String?>) {
        AllMusic.side.apply {
            when {
                args[0].equals("next", ignoreCase = true) -> {
                    PlayMusic.musicLessTime = 1
                    sendMessage(sender, "§d[AllMusic]§2已强制切歌")
                    AllMusic.getConfig().RemoveNoMusicPlayer(name)
                }
                args[0].equals("next", ignoreCase = true) -> {
                    PlayMusic.musicLessTime = 1
                    sendMessage(sender, "§d[AllMusic]§2已强制切歌")
                    AllMusic.getConfig().RemoveNoMusicPlayer(name)
                }
                args[0].equals("reload", ignoreCase = true) -> {
                    reload()
                    sendMessage(sender, "§d[AllMusic]§2已重读配置文件")
                }
                args[0].equals("ban", ignoreCase = true) && args.size == 2 -> {
                    if (Function.isInteger(args[1])) {
                        AllMusic.getConfig().addBanID(args[1])
                        sendMessage(sender, "§d[AllMusic]§2已禁止" + args[1])
                    } else {
                        sendMessage(sender, "§d[AllMusic]§2请输入有效的ID")
                    }
                }
                args[0].equals("delete", ignoreCase = true) && args.size == 2 -> {
                    if (args[1]!!.isNotEmpty() && Function.isInteger(args[1])) {
                        val music = args[1]!!.toInt()
                        if (music == 0) {
                            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID")
                            return
                        }
                        if (music > PlayMusic.getSize()) {
                            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2序列号过大")
                            return
                        }
                        PlayMusic.remove(music - 1)
                        AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已删除序列$music")
                    } else {
                        AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID")
                    }
                }
                args[0].equals("addlist", ignoreCase = true) && args.size == 2 -> {
                    if (Function.isInteger(args[1])) {
                        AllMusic.getMusicApi().setList(args[1], sender)
                        sendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表" + args[1])
                    } else {
                        sendMessage(sender, "§d[AllMusic]§2请输入有效的音乐列表ID")
                    }
                }
                args[0].equals("clearlist", ignoreCase = true) -> {
                    AllMusic.getConfig().playList.clear()
                    AllMusic.save()
                    sendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表已清空")
                }
                args[0].equals("code", ignoreCase = true) -> AllMusic.getMusicApi().sendCode(sender)
                args[0].equals("login", ignoreCase = true) -> {
                    if (args.size != 2) {
                        sendMessage(sender, "§d[AllMusic]§c没有手机验证码")
                        return
                    }
                    sendMessage(sender, "§d[AllMusic]§d重新登录网易云账户")
                    AllMusic.getMusicApi().login(sender, args[1])
                }
                else -> Unit
            }
        }
    }

    private fun addSpecifiedMusic(sender: Any, name: String, args: Array<String?>) {
        if (AllMusic.getConfig().isNeedPermission
            && AllMusic.side.checkPermission(name, "AllMusic.addmusic")
        ) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.noPer)
        } else {
            when (AllMusic.getConfig().defaultAddMusic) {
                1 -> searchMusic(sender, name, args, true)
                0 -> addMusic(sender, name, args)
                else -> addMusic(sender, name, args)
            }
        }
    }

    @JvmStatic
    fun showSearch(sender: Any?, search: SearchPage) {
        val index = search.index
        var item: SearchOBJ
        var info: String
        AllMusic.side.sendMessage(sender, "")
        if (search.haveLastPage()) {
            AllMusic.side.sendMessageRun(
                sender, "§d[AllMusic]§2输入/music lastpage上一页",
                AllMusic.getMessage().page.last, "/music lastpage"
            )
        }
        for (a in 0 until index) {
            item = search.getRes(a + search.page * 10)
            info = AllMusic.getMessage().page.choice.replace("%index%", "" + (a + 1))
                .replace("%MusicName%", item.name)
                .replace("%MusicAuthor%", item.author)
                .replace("%MusicAl%", item.al)
            AllMusic.side.sendMessageRun(
                sender, info,
                AllMusic.getMessage().click.This, "/music select " + (a + 1)
            )
        }
        if (search.haveNextPage()) {
            AllMusic.side.sendMessageRun(
                sender, "§d[AllMusic]§2输入/music nextpage下一页",
                AllMusic.getMessage().page.next, "/music nextpage"
            )
        }
        AllMusic.side.sendMessage(sender, "")
    }

    @JvmStatic
    fun ex(sender: Any, name: String, args: Array<String?>) {
        when {
            args.isEmpty() || args[0].isNullOrEmpty() -> AllMusic.side.sendMessage(
                sender,
                AllMusic.getMessage().command.error
            )
            args[0].equals("help", ignoreCase = true) -> {
                showHelpInfo(sender)
                showHelpInfoForAdmin(sender, name)
            }
            args[0].equals("stop", ignoreCase = true) -> stopMusic(sender, name)
            args[0].equals("list", ignoreCase = true) -> getMusicList(sender)
            args[0].equals("vote", ignoreCase = true) -> voteToChangeMusic(sender, name)
            args[0].equals("nomusic", ignoreCase = true) -> setNoMusic(sender, name)
            args[0].equals("search", ignoreCase = true) && args.size >= 2 -> searchSpecifiedMusic(sender, name, args)
            args[0].equals("select", ignoreCase = true) && args.size == 2 -> selectSpecifiedMusic(sender, name, args)
            args[0].equals("nextpage", ignoreCase = true) -> gotoNextPage(sender, name)
            args[0].equals("lastpage", ignoreCase = true) -> gotoLastPage(sender, name)
            args[0].equals("hud", ignoreCase = true) -> showHud(sender, name, args)
            AllMusic.getConfig().admin.contains(name) -> executeAdminCommands(sender, name, args)
            else -> addSpecifiedMusic(sender, name, args)
        }
    }
}