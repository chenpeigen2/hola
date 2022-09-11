package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.fake.FakeConfig

/**
 * exec with multi holder
 * for instance : one container holds 10 couch database
 */
class MultiExec(config: Config) : Exec(config) {

    private val cnt = FakeConfig.count

    override fun serverNodes(): List<ServerNode> {
        if (snodes != null) return snodes as MutableList<ServerNode>
        snodes = mutableListOf()
        ServerHelper.sids.forEach { sid ->
            repeat(cnt) {
                snodes!!.add(ServerNode(sid, "${FakeConfig.databaseName}$it"))
            }
        }
        return snodes as MutableList<ServerNode>
    }
}