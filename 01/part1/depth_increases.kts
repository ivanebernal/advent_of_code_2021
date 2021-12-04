import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

// 1. Read file line per line
// 2. If there's a stored value, compare to previous value and increment if current is greater
// 3. Store new value
var previousValue: Int? = null
var result = 0
File(file).forEachLine { line: String ->
    val curr = line.toInt()
    previousValue?.let { v -> 
        if(v < curr) {
            result++ 
        }
    }
    previousValue = curr
}
println("Depth increases: $result")