import java.io.File
import java.lang.IllegalArgumentException
import kotlin.collections.MutableList

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val dots = mutableSetOf<Pair<Int, Int>>()
val folds = mutableListOf<Pair<String, Int>>()

File(input).forEachLine { line ->
    if(line.contains("fold along")) {
        val instruction = line.split(" ")[2]
        folds.add(Pair(instruction.split("=")[0], instruction.split("=")[1].toInt()))
    } else if(line.isNotEmpty()){
        dots.add(Pair(line.split(",")[0].toInt(), line.split(",")[1].toInt()))
    }
}

// y up, x left
fun fold(axis: String, value: Int) {
    val newDots = mutableSetOf<Pair<Int, Int>>()
    val oldDots = mutableSetOf<Pair<Int, Int>>()
    dots.forEach { dot ->
        if(axis == "y") {
            if(dot.second > value) {
                val newY = value - (dot.second - value)
                if(newY >= 0) {
                    val newDot = Pair(dot.first, newY)
                    newDots.add(newDot)
                    oldDots.add(dot)
                }
            }
        } else {
            if(dot.first > value) {
                val newX = value - (dot.first - value)
                if(newX >= 0) {
                    val newDot = Pair(newX, dot.second)
                    newDots.add(newDot)
                    oldDots.add(dot)
                }
            }
        }
    }
    dots.removeAll(oldDots)
    dots.addAll(newDots)
}

folds.forEach { fold(it.first, it.second) }

var maxX = 0
var maxY = 0

dots.forEach { 
    maxX = Math.max(maxX, it.first)
    maxY = Math.max(maxY, it.second)
}

for(y in 0 .. maxY) {
    for(x in 0 .. maxX) {
        if(dots.contains(Pair(x, y))) print("#")
        else print(" ")
    }
    println()
}