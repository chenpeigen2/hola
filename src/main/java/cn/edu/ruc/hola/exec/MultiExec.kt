package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.config.ServerNode

/**
 * exec with multi holder
 * for instance : one container holds 10 couch database
 */
class MultiExec(config: Config) : Exec(config) {

    private val cnt = 10

    override fun serverNodes(): List<ServerNode> {
        val snods = mutableListOf<ServerNode>()
        ServerHelper.sids.forEach { sid ->
            repeat(cnt) {
                snods.add(ServerNode(sid, "fake$it"))
            }
        }
        return snods
    }
}