package com.umermahar.smbinolmialdistribution

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow
import kotlin.math.round

class MainViewModel: ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                probability = calculate(
                    n = it.noOfSubjects.toInt(),
                    p = it.passRate.toFloat()
                )
            )
        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnNoOfSubjectChange -> _state.update {
                it.copy(noOfSubjects = event.num)
            }

            is MainEvent.OnPassRateChange -> _state.update {
                it.copy(passRate = event.percentage)
            }

            MainEvent.OnCalculateButtonClick -> {
                if(!areNumOfSubjectsValid(state.value.noOfSubjects)) {
                    _state.update {
                        it.copy(errorRes = R.string.invalid_no_of_subjects)
                    }
                    return
                }
                if (!isPassRateValid(state.value.passRate)) {
                    _state.update {
                        it.copy(errorRes = R.string.invalid_no_of_pass_rate)
                    }
                    return
                }

                _state.update {
                    it.copy(
                        probability = calculate(
                            n = it.noOfSubjects.toInt(),
                            p = it.passRate.toFloat()
                        ),
                        errorRes = null
                    )
                }
            }
        }
    }

    private fun calculate(n: Int, p: Float): Double {
        // Probability of passing each subject
        var cumulativeProbability = 0.0

        val s = (n / 2) + 1
        // Calculate probability for passing 3, 4, and 5 subjects
        for (x in s..n) {
            cumulativeProbability += binomialProbability(n, x, p.toDouble() / 100)
        }

        // Rounding to four decimal places for clarity
        cumulativeProbability = round(cumulativeProbability * 10000) / 10000

        println("The probability that 19SW will pass more subjects than they fail out of five is: $cumulativeProbability")
        return cumulativeProbability
    }

    // Function to calculate the binomial coefficient "n choose k"
    private fun binomialCoefficient(n: Int, x: Int): Long {
        var result = 1L
        for (i in 0 until x) {
            result = result * (n - i) / (i + 1)
        }
        return result
    }

    // Function to calculate the probability of passing exactly 'k' subjects
    private fun binomialProbability(n: Int, k: Int, p: Double): Double {
        val binCoEff = binomialCoefficient(n, k)
        return binCoEff * p.pow(k) * (1 - p).pow(n - k)
    }

    private fun areNumOfSubjectsValid(num: String): Boolean{
        return if (num.isEmpty()) {
            false
        } else if(num.toInt() > 10 || num.toInt() < 1) {
            false
        } else true
    }

    private fun isPassRateValid(num: String): Boolean{
        return if (num.isEmpty()) {
            false
        } else if(num.toFloat() >= 100f || num.toFloat() <= 0f) {
            false
        } else true
    }

}