import java.io.File
import java.lang.IllegalArgumentException
import kotlin.collections.MutableList
import java.util.ArrayDeque

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

class Node(val name: String, val neighbors: MutableList<Node>)

val nodeMap = mutableMapOf<String, Node>()

fun getNode(s: String): Node {
    if(nodeMap.contains(s)) {
        return nodeMap[s]!!
    } else {
        val n = Node(s, mutableListOf())
        nodeMap.put(s, n)
        return n
    }
}

File(input).forEachLine { line ->
    val path = line.split("-")
    val s = path[0]
    val e = path[1]
    getNode(s).neighbors.add(getNode(e))
    getNode(e).neighbors.add(getNode(s))
}
var smallVisited = mutableSetOf<Node>()
var paths = 0

fun visit(cave: Node) {
    if(cave == getNode("end")) {
        // Found the exit
        paths++;
        return
    }
    if(smallVisited.contains(cave) || cave == getNode("start")) {
        // Already visited a small cave or not returnung to start
        return
    }
    if(cave.name[0].isLowerCase() && cave.name != "start") {
        smallVisited.add(cave)
    }
    cave.neighbors.forEach { neighbor ->
        visit(neighbor)
    }
    if(cave.name[0].isLowerCase() && cave.name != "start") {
        smallVisited.remove(cave)
    }
}

nodeMap["start"]!!.neighbors.forEach { visit(it) }

println("There are $paths paths")