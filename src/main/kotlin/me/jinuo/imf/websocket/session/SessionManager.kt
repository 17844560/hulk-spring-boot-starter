package me.jinuo.imf.websocket.session

/**
 * @author frank
 * @date 2019-10-26 15:10
 * @desc
 **/
interface SessionManager<S,T:Session> {

    /**
     * 获取全部SESSION
     * @return
     */
    fun getSession(): Collection<T>

    /**
     * 获取SESSION
     * @param id
     * @return
     */
    fun getSession(id: Long): T?

    /**
     * 创建 SESSION
     * @param websocket
     * @return
     */
    fun createSession(websocket: S): T

    /**
     * 销毁SESSION
     * @param session
     * @return
     */
    fun destroySession(session: T)

    /**
     *
     */
    fun addSession(session: T)

    /**
     * 获取下一个发包序号
     */
    fun nextSn():Int

    fun bind(session:Session, identity:Any)

}