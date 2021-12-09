import java.io.File
import java.lang.IllegalArgumentException
import java.util.PriorityQueue

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val heightMap = mutableListOf<List<Int>>()
val lowPoints = mutableListOf<Pair<Int,Int>>()
File(input).forEachLine { line ->
    heightMap.add(line.toCharArray().map { it - '0' })
}

val visitedMap = Array<Array<Boolean>>(heightMap.size, { Array<Boolean>(heightMap[0].size, { false }) })

fun isWithinBounds(row: Int, col: Int): Boolean {
    val rows = heightMap.size;
    val cols = heightMap[0].size
    return row >= 0 && row < rows && col >= 0 && col < cols 
}

fun getLargestBasin(row: Int, col: Int): Int {
    if(!isWithinBounds(row, col)|| heightMap[row][col] == 9 || visitedMap[row][col]) return 0
    visitedMap[row][col] = true
    val adjacents = listOf(Pair(row - 1, col), Pair(row + 1, col), Pair(row, col - 1), Pair(row, col + 1))
    var result = 1
    adjacents.forEach { adjacent ->
        result += getLargestBasin(adjacent.first, adjacent.second)
    }
    return result
}

var sizes = PriorityQueue<Int>()
for(row in 0 until heightMap.size) {
    for(col in 0 until heightMap[0].size) {
        if(heightMap[row][col] < 9 && !visitedMap[row][col]) {
            sizes.add(getLargestBasin(row, col))
            if(sizes.size > 3) sizes.poll()
        }
    }
}

var result = sizes.poll()!! * sizes.poll()!! * sizes.poll()!!

println("The multiplication of the largest 3 basins is $result")