package com.umermahar.smbinolmialdistribution

/**
 * Represents the state of a calculation process for determining the probability of passing
 * a certain number of subjects based on a given pass rate.
 *
 * @property passRate The rate of passing as a percentage, represented as a string.
 *                    Default is "70.0", which represents a 70% pass rate.
 * @property noOfSubjects The total number of subjects, represented as a string.
 *                        Default is "5", indicating 5 subjects.
 * @property probability The calculated probability of passing more subjects than failing.
 *                       This value is null by default and is set after the calculation.
 * @property errorRes The resource ID for an error message, if any error occurs during
 *                    calculation or validation. Null by default.
 */
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