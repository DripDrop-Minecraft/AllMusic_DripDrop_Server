package coloryr.allmusic.api

interface ISide {
    fun send(data: String?, player: String?, isplay: Boolean?)
    fun send(data: String?, isplay: Boolean?)
    val allPlayer: Int
    fun bq(data: String?)
    fun bqt(data: String?)
    fun NeedPlay(): Boolean
    fun sendHudLyric(data: String?)
    fun sendHudInfo(data: String?)
    fun sendHudList(data: String?)
    fun sendHudSaveAll()
    fun clearHud(player: String?)
    fun clearHudAll()
    fun sendMessaget(obj: Any?, Message: String?)
    fun sendMessage(obj: Any?, Message: String?)
    fun sendMessageRun(obj: Any?, Message: String?, end: String?, command: String?)
    fun sendMessageSuggest(obj: Any?, Message: String?, end: String?, command: String?)
    fun runTask(run: Runnable?)
    fun reload()
    fun checkPermission(player: String?, permission: String?): Boolean
    fun task(run: Runnable?, delay: Int)
}