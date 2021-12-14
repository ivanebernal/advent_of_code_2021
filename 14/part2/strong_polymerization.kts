import java.io.File
import java.lang.IllegalArgumentException
import kotlin.collections.MutableList
import kotlin.collections.MutableMap

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val STEPS = 40

val ruleMap = mutableMapOf<String, String>()
val countMap = mutableMapOf<Char, Long>()
var polymerMap = mutableMapOf<String, Long>()
var polymerStr: String = ""

File(input).forEachLine { line ->
    if(line.contains("->")) {
        val rule = line.split(" -> ")
        ruleMap.put(rule[0], rule[1])
    } else if(line.isNotEmpty()){
        polymerStr = line
    }
}

for (i in 0 until polymerStr.length - 1) {
    val pair = "${polymerStr[i]}${polymerStr[i+1]}"
    polymerMap.put(pair, (polymerMap[pair] ?: 0L) + 1)
}

for(step in 0 until STEPS) {
    val pairs = polymerMap.keys.toMutableList()
    val newPolymerMap = mutableMapOf<String, Long>()
    pairs.forEach { pair ->
        val newElement = ruleMap[pair]
        if(newElement != null) {
            val pairLeft = "${pair[0]}$newElement"
            val pairRight = "$newElement${pair[1]}"
            newPolymerMap[pairLeft] = (newPolymerMap[pairLeft] ?: 0) + polymerMap[pair]!!
            newPolymerMap[pairRight] = (newPolymerMap[pairRight] ?: 0) + polymerMap[pair]!!
        }
    }
    polymerMap = newPolymerMap
}

polymerMap.forEach { (pair, count) ->
    countMap[pair[0]] = (countMap[pair[0]] ?: 0) + count
    countMap[pair[1]] = (countMap[pair[1]] ?: 0) + count
}

val chars = countMap.keys
var max = 0L
var min = Long.MAX_VALUE

chars.forEach { element ->
    if(element == polymerStr.first() || element == polymerStr.last()) {
        countMap[element] = (countMap[element]!! / 2) + 1
    } else {
        countMap[element] = countMap[element]!! / 2
    }
    max = Math.max(countMap[element]!!, max)
    min = Math.min(countMap[element]!!, min)
}

println("Score ${max - min}")
