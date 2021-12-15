import java.io.File
import java.lang.IllegalArgumentException
import java.util.PriorityQueue
import java.util.Comparator

typealias Vertex = Pair<Int, Int>

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val cave = mutableListOf<List<Int>>()

File(input).forEachLine { line ->
    cave.add(line.toCharArray().map { it - '0' })
}

val prevMap = mutableMapOf<Vertex, Vertex>()
val distanceMap = mutableMapOf<Vertex, Int>()

fun dijkstra() {
    val source = Pair(0, 0)
    distanceMap[source] = 0
    val nodeQueue = PriorityQueue<Vertex>({ a, b -> (distanceMap[a] ?: Int.MAX_VALUE) - (distanceMap[b] ?: Int.MAX_VALUE) })
    nodeQueue.add(source)
    while(nodeQueue.isNotEmpty()) {
        val u = nodeQueue.poll()
        for(neighbor in getNeighbors(u.first, u.second)) {
            val (row, col) = neighbor
            var risk = getRisk(row, col)
            val dist = distanceMap[u]?.plus(risk) ?: Int.MAX_VALUE 
            if(dist < distanceMap[neighbor] ?: Int.MAX_VALUE) {
                distanceMap[neighbor] = dist
                prevMap[neighbor] = u
                nodeQueue.add(neighbor)
            }
        }
    }
}

fun isValid(row: Int, col: Int): Boolean {
    return row >= 0 && col >= 0 && row < (cave.size * 5) && col < (cave[0].size * 5)
}

fun getNeighbors(row: Int, col: Int): List<Vertex> {
    val result = mutableListOf<Vertex>()
    if(isValid(row + 1, col)) result.add(Pair(row + 1, col))
    if(isValid(row, col + 1)) result.add(Pair(row, col + 1))
    if(isValid(row - 1, col)) result.add(Pair(row - 1, col))
    if(isValid(row, col - 1)) result.add(Pair(row, col - 1))
    return result
}

fun getRisk(row: Int, col: Int): Int {
    var risk = cave[row % cave.size][col % cave[0].size] + (row / cave.size) + (col / cave[0].size)
    return if(risk > 9) risk - 9 else risk
}

dijkstra()

var exit = Pair(cave.size * 5 - 1, cave[0].size * 5 - 1)

// Starting position risk is not counted
println("Lowes risk level ${distanceMap[exit]!!}")
