import java.io.File
import java.lang.IllegalArgumentException
import java.util.PriorityQueue
import java.util.Comparator

typealias Vertex = Pair<Int, Int>

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val caveTile = mutableListOf<List<Int>>()

File(input).forEachLine { line ->
    caveTile.add(line.toCharArray().map { it - '0' })
}

val cave = Array(caveTile.size * 5) { Array(caveTile[0].size * 5) { 0 } }

for(row in 0 until caveTile.size * 5) {
    for(col in 0 until caveTile[0].size * 5) {
        val risk = caveTile[row % caveTile.size][col % caveTile[0].size] + (row / caveTile.size) + (col / caveTile[0].size)
        cave[row][col] = if(risk > 9) risk - 9 else risk
    }
}

val prevMap = mutableMapOf<Vertex, Vertex>()

fun dijkstra() {
    val source = Pair(0, 0)
    val distanceMap = mutableMapOf<Vertex, Int>()
    distanceMap[source] = 0
    val nodeQueue = PriorityQueue<Vertex>({ a, b -> (distanceMap[a] ?: Int.MAX_VALUE) - (distanceMap[b] ?: Int.MAX_VALUE) })
    nodeQueue.add(source)
    while(nodeQueue.isNotEmpty()) {
        val u = nodeQueue.poll()
        for(neighbor in getNeighbors(u.first, u.second)) {
            val dist = distanceMap[u]?.plus(cave[neighbor.first][neighbor.second]) ?: Int.MAX_VALUE 
            if(dist < distanceMap[neighbor] ?: Int.MAX_VALUE) {
                distanceMap[neighbor] = dist
                prevMap[neighbor] = u
                nodeQueue.add(neighbor)
            }
        }
    }
}

fun isValid(row: Int, col: Int): Boolean {
    return row >= 0 && col >= 0 && row < cave.size && col < cave[0].size
}

fun getNeighbors(row: Int, col: Int): List<Vertex> {
    val result = mutableListOf<Vertex>()
    if(isValid(row + 1, col)) result.add(Pair(row + 1, col))
    if(isValid(row, col + 1)) result.add(Pair(row, col + 1))
    if(isValid(row - 1, col)) result.add(Pair(row - 1, col))
    if(isValid(row, col - 1)) result.add(Pair(row, col - 1))
    return result
}

dijkstra()

var current = Pair(cave.size - 1, cave[0].size - 1)
var result = 0

while(current != Pair(0,0)) {
    result += cave[current.first][current.second]
    current = prevMap[current]!!
}

// Starting position risk is not counted
println("Lowes risk level $result")
