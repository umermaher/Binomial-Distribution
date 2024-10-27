package com.umermahar.smbinolmialdistribution

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow
import kotlin.math.round

class MainViewModel: ViewModel() {

    private val _state = MutableStateFlow(MainState(probability = calculate()))
    val state = _state.asStateFlow()

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

    /**
     * Calculates the probability of passing more subjects than failing, given a total number of subjects
     * and a pass rate. The function uses a binomial probability distribution to calculate the cumulative
     * probability of passing more than half the subjects.
     *
     * @param n The total number of subjects. Should be an integer greater than zero. Default is 5.
     * @param p The pass rate (success rate) as a percentage (e.g., 70 for 70%). This value is converted to a probability
     *          by dividing by 100 within the function. Default is 70f.
     * @return The cumulative probability (rounded to four decimal places) of passing more subjects than failing.
     *
     * This function assumes that each subject has the same independent probability of being passed.
     *
     * Example:
     * ```
     * val probability = calculate(5, 70f)
     * ```
     */
    private fun calculate(n: Int = 5, p: Float = 70f): Double {
        // Probability of passing each subject
        var cumulativeProbability = 0.0
        val moreThanHalf = (n / 2) + 1

        // Calculate probability for passing more than half of the subjects
        for (x in moreThanHalf..n) {
            cumulativeProbability += binomialProbability(n, x, p.toDouble() / 100)
        }
        // Rounding to four decimal places for clarity
        cumulativeProbability = round(cumulativeProbability * 10000) / 10000
        println("The probability that 19SW will pass more subjects than they fail out of five is: $cumulativeProbability")

        return cumulativeProbability
    }

    /**
     * Calculates the binomial probability of passing exactly `x` subjects out of `n` subjects,
     * given a specific pass rate. This function uses the binomial probability formula to compute
     * the likelihood of achieving exactly `x` successes in `n` trials.
     *
     * @param n The total number of subjects (or trials). Should be an integer greater than zero.
     * @param x The number of subjects (or trials) to pass (successes). Should be an integer in the range [0, n].
     * @param p The probability of passing each subject (success probability), represented as a decimal
     *          (e.g., 0.7 for a 70% success rate). Should be between 0 and 1.
     * @return The probability of passing exactly `x` subjects out of `n`, as a Double.
     *
     * This function uses the binomial probability formula:
     * `P(X = x) = C(n, x) * p^x * (1 - p)^(n - x)`
     * where `C(n, x)` is the binomial coefficient.
     *
     * Example:
     * ```
     * val probability = binomialProbability(5, 3, 0.7)
     * ```
     */
    private fun binomialProbability(n: Int, x: Int, p: Double): Double {
        val binCoEff = binomialCoefficient(n, x)
        return binCoEff * p.pow(x) * (1 - p).pow(n - x)
    }

    /**
     * Calculates the binomial coefficient, also known as "n choose x" (C(n, x)), which represents
     * the number of ways to choose `x` successes (or items) from a total of `n` trials (or items).
     * This coefficient is essential for calculating probabilities in the binomial distribution.
     *
     * @param n The total number of items or trials. Should be a non-negative integer greater than or equal to `x`.
     * @param x The number of items to choose. Should be a non-negative integer, less than or equal to `n`.
     * @return The binomial coefficient as a `Long` value, representing the number of combinations.
     *
     * This function uses an iterative approach to calculate the binomial coefficient without
     * overflow for relatively large values of `n` and `x` by handling intermediate division carefully.
     *
     * Formula:
     * `C(n, x) = n! / (x! * (n - x)!)`
     *
     * Example:
     * ```
     * val coefficient = binomialCoefficient(5, 3) // returns 10
     * ```
     */
    private fun binomialCoefficient(n: Int, x: Int): Long {
        var result = 1L
        for (i in 0 until x) {
            result = result * (n - i) / (i + 1)
        }
        return result
    }

    /**
     * Validates the provided number of subjects.
     * A valid number is a non-empty string representing an integer from 1 to 9 (inclusive).
     *
     * @param num The number of subjects as a string.
     * @return `true` if the input is a valid integer within the specified range, `false` otherwise.
     */
    private fun areNumOfSubjectsValid(num: String): Boolean {
        return num.toIntOrNull()?.let {
            it in 2..9
        } ?: false
    }

    /**
     * Validates the pass rate input.
     * A valid pass rate is a non-empty string representing a decimal or integer value from 0 to 100 (inclusive).
     *
     * @param num The pass rate as a string.
     * @return `true` if the input is a valid pass rate within the range 0 to 100, `false` otherwise.
     */
    private fun isPassRateValid(num: String): Boolean {
        return num.toFloatOrNull()?.let {
            it in 0f..100f
        } ?: false
    }

}