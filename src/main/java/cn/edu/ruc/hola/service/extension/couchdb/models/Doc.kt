package cn.edu.ruc.hola.service.extension.couchdb.models

class Doc : AbsDoc() {
    var code = -1
    lateinit var message: String
    lateinit var data: Data

    class Data {
        var id = 0
       lateinit var name: String
        lateinit  var bfirstletter: String
        lateinit var logo: String
        lateinit var country: String
        var countryid = -1
    }
}