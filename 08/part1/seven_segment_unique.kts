import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val output = mutableListOf<String>()
val unique = setOf(2, 3, 4, 7) // Unique number of segments
var result = 0
// File is one line long
File(input).forEachLine { line ->
    line.split(" | ")[1].split(' ').forEach { signal ->
        if (unique.contains(signal.length)) result++
    }
}

println("Unique digits: ${result}")