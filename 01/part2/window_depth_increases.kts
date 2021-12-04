import java.io.File
import java.util.Queue
import java.util.ArrayDeque

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

// 2. Keep track of queue of last 3 depths, current sum, depth increase count, and previous sum
var depthQueue: Queue<Int> = ArrayDeque<Int>()
var previousSum: Int? = null
var result = 0
var sum = 0
// 1. Read file line per line
File(file).forEachLine { line: String ->
    val curr = line.toInt()
    // 3. Add value to queue and sum, if queue.size > 3 substract tail value from sum and from queue
    depthQueue.add(curr)
    sum += curr
    if(depthQueue.size > 3) {
        sum -= depthQueue.poll()
    }
    // 4. If queue.size == 3, compare depth sums and increase increase count if current is greater than previous
    if(depthQueue.size == 3) {
        previousSum?.let { prevSum ->
            if(sum > prevSum) {
                result++
            }
        }
        // 5. Update previous sum
        previousSum = sum
    }
    
}
println("Depth window increases: $result")