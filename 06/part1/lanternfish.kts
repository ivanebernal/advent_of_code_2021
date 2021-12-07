import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val lanternfish = mutableListOf<Int>()

// File is one line long
File(input).forEachLine { line ->
    lanternfish.addAll(line.split(',').map { it.toInt() })
}

// Simulate 80 days
for(day in 0 until 80) {
    val newFish = mutableListOf<Int>()
    for (fish in 0 until lanternfish.size) {
        val timer = lanternfish[fish]
        if(timer == 0) {
            newFish.add(8)
            lanternfish[fish] = 6
        } else {
            lanternfish[fish]--
        }
    }
    lanternfish.addAll(newFish)
}

println("After 80 days there are ${lanternfish.size} lanternfish")