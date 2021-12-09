import java.io.File
import java.lang.IllegalArgumentException

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val heightMap = mutableListOf<List<Int>>()
val lowPoints = mutableListOf<Pair<Int,Int>>()
File(input).forEachLine { line ->
    heightMap.add(line.toCharArray().map { it - '0' })
}

fun isWithinBounds(point: Pair<Int, Int>): Boolean {
    val rows = heightMap.size;
    val cols = heightMap[0].size
    return point.first >= 0 && point.first < rows && point.second >= 0 && point.second < cols 
}

fun isLowPoint(point: Pair<Int, Int>, adjacents: List<Pair<Int, Int>>): Boolean {
    adjacents.forEach { adjacent ->
        if(isWithinBounds(adjacent) && heightMap[adjacent.first][adjacent.second] <= heightMap[point.first][point.second]) {
            return false
        }
    }
    return true
}

var result = 0

for(row in 0 until heightMap.size) {
    for(col in 0 until heightMap[0].size) {
        val point = Pair(row, col)
        val adjacents = listOf(Pair(row - 1, col), Pair(row + 1, col), Pair(row, col - 1), Pair(row, col + 1))
        if(isLowPoint(point, adjacents)) result += 1 + heightMap[row][col]
    }
}

println("The sum of all risk levels is $result")