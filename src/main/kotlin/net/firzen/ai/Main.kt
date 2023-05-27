package net.firzen.ai

import java.util.Random

const val POPULATION_SIZE = 100

// Valid Genes
val GENES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890, .-;:_!\"#*%&/()=?@\${[]}"

// Target string to be generated
const val TARGET = "Abrakadabra"

// Function to generate random numbers in given range
fun randomNum(start: Int, end: Int): Int {
    val range = (end - start) + 1
    return start + Random().nextInt(range)
}

// Create random genes for mutation
fun mutatedGenes(): Char {
    val len = GENES.length
    val r = randomNum(0, len - 1)
    return GENES[r]
}

// create chromosome or string of genes
fun createGnome(): String {
    val len = TARGET.length
    var gnome = ""
    repeat(len) {
        gnome += mutatedGenes()
    }
    return gnome
}

// Class representing individual in population
class Individual(val chromosome: String) {
    var fitness = calculateFitness()

    private fun calculateFitness(): Int {
        val len = TARGET.length
        var fitness = 0

        repeat(len) {
            if (chromosome[it] != TARGET[it]) {
                fitness++
            }
        }

        return fitness
    }

    fun mate(parent2: Individual): Individual {
        val childChromosome = StringBuilder()
        val chromosomeLength = chromosome.length

        repeat(chromosomeLength) {
            val p = randomNum(0, 100) / 100.0

            if (p < 0.45) {
                childChromosome.append(chromosome[it])
            } else if (p < 0.90) {
                childChromosome.append(parent2.chromosome[it])
            } else {
                childChromosome.append(mutatedGenes())
            }
        }

        return Individual(childChromosome.toString())
    }
}

fun main() {
    // current generation
    var generation = 0

    val population = mutableListOf<Individual>()
    var found = false

    // create initial population
    repeat(POPULATION_SIZE) {
        val gnome = createGnome()
        population.add(Individual(gnome))
    }

    while (!found) {
        // sort the population in increasing order of fitness score
        population.sortBy { it.fitness }

        // if the individual having lowest fitness score ie.
        // 0 then we know that we have reached the target
        // and break the loop
        if (population[0].fitness <= 0) {
            found = true
            break
        }

        // Otherwise generate new offsprings for new generation
        val newGeneration = mutableListOf<Individual>()

        // Perform Elitism, that means 10% of fittest population
        // goes to the next generation
        val s = POPULATION_SIZE / 10
        repeat(s) {
            newGeneration.add(population[it])
        }

        // From 50% of the fittest population, Individuals
        // will mate to produce offspring
        val remaining = (90 * POPULATION_SIZE) / 100
        repeat(remaining) {
            val r = randomNum(0, POPULATION_SIZE / 2)
            val parent1 = population[r]
            val r2 = randomNum(0, POPULATION_SIZE / 2)
            val parent2 = population[r2]
            val offspring = parent1.mate(parent2)
            newGeneration.add(offspring)
        }

        population.clear()
        population.addAll(newGeneration)

        println("Generation: $generation\tString: ${population[0].chromosome}\tFitness: ${population[0].fitness}")

        generation++
    }

    println("Generation: $generation\tString: ${population[0].chromosome}\tFitness: ${population[0].fitness}")
}
