import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

typealias Coordinate = Pair<Int, Int>

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val lines = mutableListOf<Pair<Coordinate, Coordinate>>()

// Read file and parse input
File(input).forEachLine { line ->
    val strCoords = line.split(" -> ".toRegex())
    val startStr = strCoords[0].split(',')
    val endStr = strCoords[1].split(',')
    val start = Pair(startStr[0].toInt(), startStr[1].toInt())
    val end = Pair(endStr[0].toInt(), endStr[1].toInt())
    lines.add(Pair(start, end))
}

// Store all points that are dangerous
val dangerPoints = mutableMapOf<Coordinate, Int>()
var result = 0
lines.forEach { line ->
    val start = line.first
    val end = line.second

    // Ignore diagonals
    if(start.first != end.first && start.second != end.second) return@forEach

    // Check if horizontal or vertical
    val isHorizontal = start.first == end.first

    val range = if(isHorizontal) {
        Math.min(start.second, end.second) .. Math.max(start.second, end.second)
    } else {
        Math.min(start.first, end.first) .. Math.max(start.first, end.first)
    }

    // Mark danger points
    for (i in range) {
        val point = Pair(
            if(isHorizontal) start.first else i,
            if(!isHorizontal) start.second else i
        )
        if(!dangerPoints.contains(point)) {
            dangerPoints.put(point, 1)
        } else {
            val d = dangerPoints[point]!! + 1
            if (d == 2) result++
            dangerPoints.put(point, dangerPoints[point]!! + 1)
        }
    }
}
println("Danger! There are ${result} danger points")