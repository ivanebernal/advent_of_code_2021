import java.io.File
import java.lang.IllegalArgumentException
import kotlin.collections.MutableList

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val STEPS = 100
val octopi = mutableListOf<MutableList<Int>>()
var step = 0
var flashes = 0

File(input).forEachLine { line ->
    val octoRow = mutableListOf<Int>()
    octoRow.addAll(line.toCharArray().map { it - '0' })
    octopi.add(octoRow)
}

fun performStep() {
    // First increase all octopi by 1
    for(row in 0 until octopi.size) {
        for(col in 0 until octopi[0].size) {
            octopi[row][col]++
        }
    }

    // Then, check for all octopi and flash all that are greater than 9. 
    for(row in 0 until octopi.size) {
        for(col in 0 until octopi[0].size) {
            maybeFlash(row, col)
        }
    }
}

fun maybeFlash(row: Int, col: Int) {
    // Ignore 0s as they have already flashed
    if(octopi[row][col] <= 9) return
    octopi[row][col] = 0
    flashes++
    val adjacent = mutableListOf<Pair<Int, Int>>().apply {
        // Top left
        if(isValid(row - 1, col - 1)) add(Pair(row - 1, col - 1))
        // Top
        if(isValid(row - 1, col)) add(Pair(row - 1, col))
        // Top right
        if(isValid(row - 1, col + 1)) add(Pair(row - 1, col + 1))
        // Right
        if(isValid(row, col + 1)) add(Pair(row, col + 1))
        // Bottom right
        if(isValid(row + 1, col + 1)) add(Pair(row + 1, col + 1))
        // Bottom
        if(isValid(row + 1, col)) add(Pair(row + 1, col))
        // Bottom left
        if(isValid(row + 1, col - 1)) add(Pair(row + 1, col - 1))
        // Left
        if(isValid(row, col - 1)) add(Pair(row, col - 1))
    }
    adjacent.forEach { (r, c) ->
        // Ignore 0s as the have already flashed
        if(octopi[r][c] > 0) {
            octopi[r][c]++
            maybeFlash(r, c)
        }
    }
}

fun isValid(row: Int, col: Int) = 
    row >= 0 && col >= 0 && 
    row < octopi.size && col < octopi[0].size 

while(flashes != octopi.size * octopi[0].size) {
    step++
    flashes = 0
    performStep()
}

println("Octopi synchronize on step $step")