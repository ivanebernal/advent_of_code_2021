import java.io.File
import java.lang.IllegalArgumentException

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

class Display(val encoded: List<String>, val encodedOutput: List<String>) {
    val output: Int
    private val decodeMap = mutableMapOf<Int, String>()
    val uniqueLengths = setOf(2, 3, 4, 7)
    val segmentToValueMap = mapOf(2 to listOf(1), 3 to listOf(7), 4 to listOf(4), 5 to listOf(2, 3, 5), 6 to listOf(0, 6, 9), 7 to listOf(8))

    init {
        decode()
        output = decodeOutput()
    }

    private fun decode() {
        // First find unique digits for reference
        encoded.forEach { signal ->
            when(signal.length) {
                2 -> decodeMap.put(1, signal)
                3 -> decodeMap.put(7, signal)
                4 -> decodeMap.put(4, signal)
                7 -> decodeMap.put(8, signal)
            }
        }

        for(signal in encoded) {
            if(uniqueLengths.contains(signal.length)) continue
            when(signal.length) {
                5 -> {
                    if(signal.containsAll(decodeMap[1]!!)) {
                        // 3 is the only 5 segment # that contains same segments as 1
                        decodeMap.put(3, signal)
                    } else if(signal.containsAllButOne(decodeMap[4]!!)) {
                        // 5 contains all but 1 segment of 4
                        decodeMap.put(5, signal)
                    } else {
                        decodeMap.put(2, signal)
                    }
                }
                6 -> {
                    if(!signal.containsAll(decodeMap[1]!!)) {
                        // 6 is the only 6 segment # that donesn't contain all segments of 1
                        decodeMap.put(6, signal)
                    } else if (signal.containsAll(decodeMap[4]!!)) {
                        decodeMap.put(9, signal)
                    } else {
                        decodeMap.put(0, signal)
                    }
                }
            }
        }
    }

    private fun decodeOutput(): Int {
        var mult = 1
        var result = 0
        for (i in encodedOutput.size - 1 downTo 0) {
            result += segmentsToInt(encodedOutput[i]) * mult
            mult *= 10
        }
        return result
    }

    private fun segmentsToInt(segments: String): Int {
        val candidates = segmentToValueMap[segments.length]!!
        candidates.forEach { digit -> 
            if(segments.containsAll(decodeMap[digit]!!)) return digit
        }
        return -1
    }

    fun String.containsAll(other: String): Boolean {
        if(other.length > this.length) return false
        other.forEach { if(!this.contains(it)) return false }
        return true
    }


    fun String.containsAllButOne(other: String): Boolean {
        var notIncluded = 0
        other.forEach { if(!this.contains(it)) notIncluded++ }
        return notIncluded == 1
    }
}

val displays = mutableListOf<Display>()

// File is one line long
File(input).forEachLine { line ->
    val encodedDisplay = line.split(" | ")
    displays.add(Display(encodedDisplay[0].split(" "), encodedDisplay[1].split(" ")))
}
var result = 0 
displays.forEach { result += it.output }
println("Sum of outputs: ${result}")