import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val crabs = mutableListOf<Int>()
var result = 0

// File is one line long
File(input).forEachLine { line ->
    crabs.addAll(line.split(',').map { it.toInt() })
}
var maxX = 0
var minX = Int.MAX_VALUE
val crabMap = mutableMapOf<Int, Int>()

crabs.forEach { crab ->
    maxX = Math.max(crab, maxX)
    minX = Math.min(crab, minX)
    crabMap.put(crab, crabMap[crab]?.let { it + 1} ?: 1)
}

var minCost = Int.MAX_VALUE

for (pos in minX .. maxX) {
    var cost = 0
    crabMap.forEach { entry ->
        cost += Math.abs(entry.key - pos) * entry.value
    }
    minCost = Math.min(cost, minCost)
}


println("Min fuel cost of crab align is $minCost")