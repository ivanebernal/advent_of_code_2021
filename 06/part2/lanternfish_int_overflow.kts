import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val lanternfish = mutableListOf<Int>()
var result = 0L

// File is one line long
File(input).forEachLine { line ->
    lanternfish.addAll(line.split(',').map { it.toInt() })
    result += lanternfish.size
}

println("Initial fish: $result")

val timers = mutableListOf(0L,0L,0L,0L,0L,0L,0L,0L,0L)

// Organize lanternfish by timer
lanternfish.forEach { timer ->
    timers[timer]++
}

// Simulate 80 days
for(day in 0 until 256) {
    val newFish = timers[0]
    for (time in 0 until timers.size - 1) {
        // Decrease timers
        timers[time] = timers[time + 1]
    }
    // Start longer timer for new fish 
    timers[8] = newFish
    // Reset timer for fish that produced new fish
    timers[6]+= newFish
    result += newFish
}

println("After 256 days there are ${result.toString()} lanternfish")