package dev.teogor

import dev.teogor.sudoku.gen.Difficulty
import dev.teogor.sudoku.gen.Sudoku
import dev.teogor.sudoku.gen.SudokuGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val sudoku = SudokuGenerator.getSudoku(
        difficulty = Difficulty.EASY,
        seed = 0L,
        gridSize = 16,
    )
    val startTime = System.currentTimeMillis()
    val sudokus: MutableSet<Sudoku> = mutableSetOf()
    val singleSpace = true
    val sudokusSize = listOf(
        4,
        9
    )
    val sudokusResult = listOf(
        // 4x4
        "4312321442314123",
        // 9x9
        "924761835658923417371485296743598621892146573516237948437652189285319764169874352"
    )
    if (singleSpace) {
        // 4x4 = 4312321442314123
        // 9x9 = 924761835658923417371485296743598621892146573516237948437652189285319764169874352
        // 16x16 = todo
        val seed = 0L
        sudokusSize.forEachIndexed { index, gridSize ->
            repeat(2) {
                val sudoku = SudokuGenerator.getSudoku(
                    difficulty = Difficulty.EASY,
                    seed = seed,
                    gridSize = gridSize,
                )
                sudokus.add(sudoku)
                val solution = sudoku.solution
                if (solution != sudokusResult[index]) {
                    println("test constraint failed for $gridSize")
                } else {
                    println("test constraint passed for $gridSize")
                }
            }
        }
    } else {
        val parallelism = 1
        val setOf = 10L
        val startSeed = 0 * setOf
        val endSeed = startSeed + setOf
        val job = launch(Dispatchers.Default) {
            val chunkSize = (endSeed - startSeed) / parallelism
            val ranges = (startSeed until endSeed).chunked(chunkSize.toInt())
            for (range in ranges) {
                launch {
                    repeat(parallelism) {
                        val sudoku = SudokuGenerator.getSudoku(
                            difficulty = Difficulty.EASY,
                            seed = System.currentTimeMillis(),
                            gridSize = 9,
                        )
                        println(sudoku.solution)
                        sudokus.add(sudoku)
                    }
                }
            }
        }
        job.join()
    }
    val endTime = System.currentTimeMillis()
    val generationTime = endTime - startTime

    println()
    println("Sudoku List Generation Time: ${generationTime.toFormattedTime()}")
    println("Sudokus size ${sudokus.size}")
}

fun Long.toFormattedTime(): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / (1000 * 60)) % 60
    val hours = (this / (1000 * 60 * 60))

    val timeStringBuilder = StringBuilder()

    if (hours > 0) {
        timeStringBuilder.append("${hours}h ")
    }

    if (minutes > 0) {
        timeStringBuilder.append("${minutes}m ")
    }

    if (seconds > 0 && (hours == 0L && minutes == 0L)) {
        timeStringBuilder.append("${seconds}s")
    } else {
        timeStringBuilder.append("${this % 1000}ms")
    }

    return timeStringBuilder.toString().trimEnd(',', ' ')
}

