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

val validVelocities = mutableSetOf<Pair<Int, Int>>()

for(vx in 0 .. xRange.endInclusive) {
    for(vy in yRange.start .. -yRange.start) {
        var step = 1
        var y = getY(vy, step)
        while(y >= yRange.start) {
            var x = getX(vx, step)
            if(xRange.contains(x) && yRange.contains(y)) {
                validVelocities.add(Pair(vx, vy))
            }
            step++
            y = getY(vy, step)
        }
    }
}

fun getX(vx: Int, step: Int): Int {
    return if(step > vx) {
        vx * vx - vx * (vx - 1) / 2
    } else {
        step * vx - step * (step - 1) / 2
    }
}

fun getY(vy: Int, step: Int) = step * vy - step * (step - 1) / 2

println("Valid initial velocities: ${validVelocities.size}")