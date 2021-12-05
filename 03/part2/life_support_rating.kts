import java.io.File
import java.lang.IllegalArgumentException

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

fun String.bitsToInt(): Int {
    var result = 0
    for (i: Int in 0 until this.length) {
        if (this.get(i) == '1') {
            result = result or (1 shl (this.length - i - 1))
        }
    }
    return result
}

// Store bit values
val oxigenVals = mutableListOf<String>()
val co2Vals = mutableListOf<String>()

// Read file line by line
File(input).forEachLine { bits ->
    // Add values to lists
    oxigenVals.add(bits)
    co2Vals.add(bits)
}
var pos = 0

// While lists sizes are greater than 1, apply filter criteria
while((oxigenVals.size > 1 || co2Vals.size > 1) && pos < oxigenVals.first().length) {
    if(oxigenVals.size > 1) {
        // Get majority on bit position from remaining elements
        var acc = 0
        oxigenVals.forEach { b -> if(b.get(pos) == '0') acc-- else acc++ }
        oxigenVals.retainAll { b ->
            // oxigen: most common or 1
            b.get(pos) == (if(acc >= 0) '1' else '0')
        }
    }
    if(co2Vals.size > 1) {
        // Get majority on bit position from remaining elements
        var acc = 0
        co2Vals.forEach { b -> if(b.get(pos) == '0') acc-- else acc++ }
        co2Vals.removeAll { b ->
            // co2 least common or 0
            b.get(pos) == (if(acc >= 0) '1' else '0')
        }
    }
    pos++
}

if(oxigenVals.size > 1 || co2Vals.size > 1) throw IllegalArgumentException("Ambiguous list of values. A single value should remain after filtering by bit criteria.")

// multiply remaining values on both lists and return
val oxigen = oxigenVals[0].bitsToInt()
val co2 = co2Vals[0].bitsToInt()
println("Oxigen:\t${oxigenVals[0]}\nCO2:\t${co2Vals[0]}\nLife support rating: ${oxigen * co2}")