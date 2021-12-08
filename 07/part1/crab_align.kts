import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

fun median(l: List<Int>): Int {
    val list = l.sorted()
    return if(list.size % 2 == 1) list[list.size/2]
        else (list[list.size/2 - 1] + list[list.size/2])/2
}

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
val median = median(crabs)
crabMap.forEach { entry ->
    cost += Math.abs(entry.key - median) * entry.value
}


println("Min fuel cost of crab align is $cost")