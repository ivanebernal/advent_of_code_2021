import java.io.File
import java.lang.IllegalArgumentException

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

// Create a list to keep track of which value has majority on each bit
val majority = mutableListOf<Int>()

// Read file line by line
File(input).forEachLine { bits ->
    val bitArr = bits.toCharArray()
    // For each line, read each bit. If 1, add 1 to corresponding list item, if 0, substract 1
    for(i: Int in 0 until bitArr.size) {
        if(majority.size < i+1) {
            majority.add(0)
        }
        if(bitArr[i] == '0') {
            majority[i]-- 
        } else if(bitArr[i] == '1') {
            majority[i]++
        } else {
            throw IllegalArgumentException("Bit value should be 1 or 0. Actual: ${bitArr[i]}")
        }
    }
}
var g = 0
var e = 0
// Create gama and epsilon values
for(j: Int in 0 until majority.size) {
    if(majority[j] > 0) {
        g = g or (1 shl (majority.size - j - 1))
    } else {
        e = e or (1 shl (majority.size - j - 1))
    }
}
// multiply and return
println("Battery level: ${g * e}")