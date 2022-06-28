package cn.edu.ruc.hola.models.couchdb

abstract class AbsDoc {
    lateinit var _id: String
    lateinit var _rev: String
    override fun toString(): String {
        return "AbsDoc(_id='$_id', _rev='$_rev')"
    }

}