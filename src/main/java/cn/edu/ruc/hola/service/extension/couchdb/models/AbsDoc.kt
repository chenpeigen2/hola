package cn.edu.ruc.hola.service.extension.couchdb.models

abstract class AbsDoc {
    lateinit var _id: String
    lateinit var _rev: String
    override fun toString(): String {
        return "AbsDoc(_id='$_id', _rev='$_rev')"
    }

}