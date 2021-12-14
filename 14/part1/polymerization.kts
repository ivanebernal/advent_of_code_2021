import java.io.File
import java.lang.IllegalArgumentException
import kotlin.collections.MutableList

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val STEPS = 10

class Node(val element: Char, var next: Node?)

val ruleMap = mutableMapOf<String, String>()
val countMap = mutableMapOf<Char, Int>()
var polymerStr: String = ""

File(input).forEachLine { line ->
    if(line.contains("->")) {
        val rule = line.split(" -> ")
        ruleMap.put(rule[0], rule[1])
    } else if(line.isNotEmpty()){
        polymerStr = line
    }
}
val polymer = Node(polymerStr[0], null)
countMap.put(polymerStr[0], (countMap.get(polymerStr[0]) ?: 0) + 1)
var head = polymer
for(i in 1 until polymerStr.length) {
    head.next = Node(polymerStr[i], null)
    countMap.put(polymerStr[i], (countMap.get(polymerStr[i]) ?: 0) + 1)
    head = head.next!!
}

for (step in 0 until STEPS) {
    head = polymer
    while(head.next != null) {
        if(ruleMap.contains("${head.element}${head.next!!.element}")) {
            val newElement = ruleMap["${head.element}${head.next!!.element}"]!![0]
            val n = Node(newElement, head.next)
            countMap.put(newElement, (countMap.get(newElement) ?: 0) + 1)
            head.next = n
            head = head.next!!.next!!
        }
    }
}
var maxCount = 0
var minCount = Int.MAX_VALUE
countMap.forEach { (_, count) ->
    maxCount = Math.max(count, maxCount)
    minCount = Math.min(count, minCount)
}

println("Score ${maxCount - minCount}")