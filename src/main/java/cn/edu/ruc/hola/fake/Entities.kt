package cn.edu.ruc.hola.fake

import java.util.Date

data class Insurance(
    val medicalCareId: String,
    val medicalCareSummary: String,
    val medicalCarePrice: Int,
    val reimbursementRate: Float,
    val date: Date
)

data class Medical(
    val _id: String,
    val medicalId: String,
    val uid: String,
    val medicalCare: List<String>
)

data class HealthInfo(
    val heartRate: Int,
    val bloodPressure: Int
)

data class Health(
    val uid: String,
    val name: String,
    val date: Date,
    val health: HealthInfo,
    val medicalId: String
)