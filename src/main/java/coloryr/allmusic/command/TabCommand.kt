package coloryr.allmusic.command

import coloryr.allmusic.AllMusic

object TabCommand {
    @JvmStatic
    fun getTabList(name: String?, arg: Array<String>): List<String> {
        val arguments: MutableList<String> = ArrayList()
        if (arg.size == 1) {
            arguments.addAll(TabCommands.NORMAL.list)
            if (AllMusic.getSearch(name) != null) {
                arguments.addAll(TabCommands.SEARCH.list)
            }
            if (AllMusic.getConfig().admin.contains(name)) {
                arguments.addAll(TabCommands.ADMIN.list)
            }
        } else if (arg[0].equals("hud", ignoreCase = true)) {
            if (arg.size == 2) {
                arguments.addAll(TabCommands.HUD.list)
            } else if (arg.size == 3) {
                if (arg[1].equals("enable", ignoreCase = true)) {
                    arguments.addAll(TabCommands.HUD_LIST.list)
                }
            }
        }
        return arguments
    }

    private enum class TabCommands(val list: List<String>) {
        NORMAL(arrayListOf("stop", "list", "vote", "nomusic", "search", "hud")),
        SEARCH(arrayListOf("select", "nextpage", "lastpage")),
        ADMIN(arrayListOf("reload", "next", "ban", "delete", "addlist", "clearlist", "initApi", "cancelApi","login")),
        HUD(arrayListOf("info", "list", "lyric", "pic")),
        HUD_LIST(arrayListOf("info", "list", "lyric", "pic", "enable", "reset"))
    }
}