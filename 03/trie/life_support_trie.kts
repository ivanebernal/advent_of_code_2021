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

class TrieNode(var count: Int) {
    var zero: TrieNode? = null
    var one: TrieNode? = null
}
// Mark initial trie node with -1
val readingsTrie = TrieNode(0)

// Read file line by line
File(input).forEachLine { bits ->
    var currentNode = readingsTrie
    for(b in bits) {
        if(b == '0') {
            if(currentNode.zero == null) currentNode.zero = TrieNode(0)
            currentNode.zero!!.count++
            currentNode = currentNode.zero!!
        } else if (b == '1') {
            if(currentNode.one == null) currentNode.one = TrieNode(0)
            currentNode.one!!.count++
            currentNode = currentNode.one!!
        }
    }
}

var co2Result = ""
var oxigenResult = ""

// Oxigen
var oxigen: TrieNode? = readingsTrie
while(oxigen != null) {
    if(oxigen?.one != null && oxigen?.zero != null) {
        if(oxigen?.one!!.count >= oxigen?.zero!!.count) {
            oxigenResult += "1"
            oxigen = oxigen?.one
        } else {
            oxigenResult += "0"
            oxigen = oxigen?.zero
        }
    } else if (oxigen?.one != null && oxigen?.zero == null) {
        oxigenResult += "1"
        oxigen = oxigen?.one
    } else if (oxigen?.one == null && oxigen?.zero != null) {
        oxigenResult += "0"
        oxigen = oxigen?.zero
    } else {
        oxigen = null
    }
}

// CO2
var co2: TrieNode? = readingsTrie
while(co2 != null) {
    if(co2?.one != null && co2?.zero != null) {
        if(co2?.one!!.count < co2?.zero!!.count) {
            co2Result += "1"
            co2 = co2?.one
        } else {
            co2Result += "0"
            co2 = co2?.zero
        }
    } else if (co2?.one != null && co2?.zero == null) {
        co2Result += "1"
        co2 = co2?.one
    } else if (co2?.one == null && co2?.zero != null) {
        co2Result += "0"
        co2 = co2?.zero
    } else {
        co2 = null
    }
}
println("Oxigen:\t${oxigenResult}\nCO2:\t${co2Result}")
println("Life support rating: ${oxigenResult.bitsToInt() * co2Result.bitsToInt()}")