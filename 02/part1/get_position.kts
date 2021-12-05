import java.io.File
import java.lang.IllegalArgumentException

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

// Keep track of depth and horizontal distance
var hor = 0
var depth = 0
// Read file line by line
File(input).forEachLine { command ->
    // Split string with " " to get direction and magnitude
    val values = command.split(' ')
    if(values.size != 2) throw IllegalArgumentException("Command must be in the form of <direction> <quantity>. Actual: ${command}")
    val dir = values[0]
    val mag = values[1].toInt()
    // decide operation: down adds to depth, up substracts from depth, forward adds to horizontal distance
    when(dir) {
        "forward" -> hor += mag
        "down" -> depth += mag
        "up" -> depth -= mag
        else -> throw IllegalArgumentException("Accepted directions are \"up\", \"down\", and \"forward\". Actual: ${dir}")
    }
}

// Finally, multiply total depth and horizontal distance
println("Horizontal: ${hor}, Depth: $depth, Result: ${hor * depth}")