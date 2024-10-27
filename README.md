# Probability Calculator Using Binomial Distribution

## Introduction
This app is a simple probability calculator using binomial distribution to determine the likelihood of passing a certain number of subjects based on user-defined parameters.

## Problem Statement
19SW must pass three theory subjects and two practicals in the 8th semester. Find the probability that they will pass more subjects and practical than they fail out of five. The binomial distribution uses an average pass rate of 70%. Evaluate the problem by developing a Kotlin program. Apply the binomial distribution to determine the probability that 19SW will pass more subjects than they fail out of five by developing a program.

## Alogorithm
````Kotlin
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
````

## Output
https://github.com/user-attachments/assets/accc1bfe-7e2a-4195-b91b-c0221d404a6e
