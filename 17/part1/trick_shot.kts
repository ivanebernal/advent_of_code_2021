import java.io.File
import java.lang.IllegalArgumentException
import kotlin.text.Regex

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")
val rangesRegex = Regex(".*x=(-?\\d*\\.\\.-?\\d*), y=(-?\\d*\\.\\.-?\\d*)")
var xRange = 0 .. 1
var yRange = 0 .. 1

File(input).forEachLine { line ->
    rangesRegex.find(line)?.let { result ->
        val rX = result.groupValues[1]
        val rY = result.groupValues[2]
        rX.split("..").let { xLimits ->
            xRange = if(xLimits[0].toInt() > xLimits[1].toInt()) {
                xLimits[1].toInt() .. xLimits[0].toInt()
            } else {
                xLimits[0].toInt() .. xLimits[1].toInt()
            }
        }
        rY.split("..").let { yLimits ->
            yRange = if(yLimits[0].toInt() > yLimits[1].toInt()) {
                yLimits[1].toInt() .. yLimits[0].toInt()
            } else {
                yLimits[0].toInt() .. yLimits[1].toInt()
            }
        }
    }
}

var maxY = 0
val validSteps = mutableSetOf<Int>()
var vix = 0

// Get possible valid steps based on x range
while(vix <= xRange.endInclusive) {
    for(step in 1 .. vix) {
        if(xRange.contains(getX(vix, step))) {
            validSteps.add(step)
        }
    }
    vix++
}

// Evaluate each y in range for each valid step
for(step in validSteps) {
    for(y in yRange) {
        maxY = Math.max(maxY, getMaxY(y, step))
    }
}

fun getX(vx: Int, step: Int) = step * vx - step * (step - 1) / 2

fun getMaxY(y: Int, step: Int): Int {
    val maxVyNum = y + (step * (step - 1))/2
    val maxVyDen = (step)
    if(maxVyNum % maxVyDen != 0) return 0
    val maxVy = maxVyNum/maxVyDen
    return maxVy * (maxVy + 1) / 2 
} 

println("Max Y is: $maxY")