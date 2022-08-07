package cn.edu.ruc.hola.benchmark

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


object Evaluate {
}

//AuthSession=YWRtaW46NjJFRjFFMjU6T5-_o6UZTK7BxVBCxaKnJIoZaxs; Version=1; Expires=Sun, 07-Aug-2022 02:16:29 GMT; Max-Age=600; Path=/; HttpOnly
//Sun, 07-Aug-2022 02:11:50 GMT;
//Sun, 07-Aug-2022 02:18:05 GMT;
//Sun, 07-Aug-2022 02:10:25 GMT
//Sun, 7-Aug-2022 02:10:04 GMT
//Sun, 7-Aug-2022 02:08:49 GMT

//[AuthSession=YWRtaW46NjJFRjFFODU6l7BGi0e1GwJs4n69UBAyYocEcnk; Version=1; Expires=Sun, 07-Aug-2022 02:18:05 GMT; Max-Age=600; Path=/; HttpOnly]
fun main() {


    var str =
        "AuthSession=YWRtaW46NjJFRjFFODU6zTFqkCWCD77WSjAN34arKjb2hP0; Version=1; Expires=Sun, 07-Aug-2022 02:18:05 GMT; Max-Age=600; Path=/; HttpOnly"

    val aa = str.substringAfter("Expires=").substringBefore(";")
    println(aa)

//    val date = sdf.parse(aa) // 1659838685000
//    println(date.time)

    val gson = Gson();

}

