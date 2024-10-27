package com.umermahar.smbinolmialdistribution

data class MainState(
    val passRate: String = "70.0",
    val noOfSubjects: String = "5",
    val probability: Double? = null,
    val errorRes: Int? = null
)

sealed interface MainEvent {
    data class OnPassRateChange(val percentage: String): MainEvent
    data class OnNoOfSubjectChange(val num: String): MainEvent
    data object OnCalculateButtonClick: MainEvent
}