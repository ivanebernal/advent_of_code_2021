import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

fun mean(l: List<Int>): Int = l.reduce{ acc, x -> acc + x }/l.size

val crabs = mutableListOf<Int>()

// File is one line long
File(input).forEachLine { line ->
    crabs.addAll(line.split(',').map { it.toInt() })
}
val crabMap = mutableMapOf<Int, Int>()

crabs.forEach { crab ->
    crabMap.put(crab, crabMap[crab]?.let { it + 1} ?: 1)
}

var cost = 0
val mean = mean(crabs)
crabMap.forEach { entry ->
    val dx = Math.abs(entry.key - mean)
    val fuel = dx * (dx + 1) / 2
    cost += fuel * entry.value
}

println("Min fuel cost of crab align is $cost")