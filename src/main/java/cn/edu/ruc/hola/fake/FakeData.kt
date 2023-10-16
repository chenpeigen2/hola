package cn.edu.ruc.hola.fake

import cn.edu.ruc.hola.models.couchdb.Doc
import com.google.gson.Gson
import java.util.*

object FakeData {

    @JvmStatic
    fun fakeData(doc: Doc): String {
//        "_id": "${doc._id}",
        return """
    {
      "code": 0,
      "message": "success",
      "data": {
        "id": ${doc.data.id},
        "name": "${doc.data.name}",
        "bfirstletter": "${doc.data.bfirstletter}",
        "logo": "${doc.data.logo}",
        "country": "${doc.data.country}",
        "countryid": ${doc.data.countryid}
      }
    }    
        """.trimIndent()
    }

    @JvmStatic
    fun fakeStuData(classId: String, gender: Int, uid: String, nickName: String): String {
        val student = Student(uid, nickName, classId, gender, Date())
        return Gson().toJson(student)
    }

    @JvmStatic
    fun fakeSocialData(socialId: String, gender: Int, uid: String, nickName: String): String {
        val social = Social(uid, nickName, gender, socialId)
        return Gson().toJson(social)
    }

    @JvmStatic
    fun basicFake(cnt: Int, threshold: Float, conspired: Boolean): MutableList<String> {
        val trusted = (cnt * threshold).toInt()
        val fake = cnt - trusted

        val l = mutableListOf<String>()
        val uuid = UUID.randomUUID().toString();
        var d = Doc()
        d._id = uuid
        d.data = Doc.Data()
        d.data.id = 0
        d.data.name = "Peter"
        d.data.bfirstletter = ""
        d.data.logo = ""
        d.data.country = "China"
        d.data.countryid = 0

        // fake for common
        for (i in trusted downTo 1) {
            l.add(fakeData(d))
        }
        println(l.size)

        // fake for faken
        if (conspired) {
            d = Doc()
            d._id = uuid
            d.data = Doc.Data()
            d.data.id = 1 // conspiracy to id 1
            d.data.name = ""
            d.data.bfirstletter = ""
            d.data.logo = ""
            d.data.country = "China"
            d.data.countryid = 0
            for (i in fake downTo 1) {
                l.add(fakeData(d))
            }
        } else {

            for (i in fake downTo 1) {

                val r = Random()
                var v = r.nextInt(fake + 10)
                if (v == 0) v += 1;

                d = Doc()
                d._id = uuid
                d.data = Doc.Data()
                d.data.id = v
                d.data.name = ""
                d.data.bfirstletter = ""
                d.data.logo = ""
                d.data.country = "China"
                d.data.countryid = 0

                l.add(fakeData(d))
            }
        }

        return l;
    }
}

fun main() {
    val fakeData = FakeData.basicFake(26, 0.8f, false)
    println(fakeData[0])
}