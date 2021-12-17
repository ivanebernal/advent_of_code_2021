import java.io.File
import java.lang.IllegalArgumentException
import java.util.PriorityQueue
import java.util.Comparator

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val hexToBinMap = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
)

fun binToDec(bin: String): Int {
    var result = 0
    for (i in 0 until bin.length) {
        result = result or ((bin[i] - '0') shl (bin.length - 1 - i))
    }
    return result
}

var versionSum = 0
var pointer = 0
var inStr = ""
var tabs = 0

File(input).forEachLine { line ->
    val sb = StringBuilder()
    for(char in line) {
        sb.append(hexToBinMap[char]!!)
    }
    inStr = sb.toString()
}

val LENGTH_TYPE_ID = 1
val VERSION_OR_TYPE_ID = 3
val PACKET_NUMBER = 11
val PACKET_LENGTH = 15

fun read(offset: Int): Int {
    val result = binToDec(inStr.substring(pointer, pointer + offset))
    pointer += offset
    return result
}

fun readLiteral(): Int {
    val number = StringBuilder()
    var firstDigit = read(1)
    while(firstDigit == 1) {
        number.append(inStr.substring(pointer, pointer + 4))
        pointer += 4
        firstDigit = read(1)
    } 
    number.append(inStr.substring(pointer, pointer + 4))
    pointer += 4
    return binToDec(number.toString())
}

fun readPacket(lengthTypeID: Int, n: Int) {
    val start = pointer
    var packages = 0
    while((lengthTypeID == 0 && (n - (pointer - start)) > 0) || (lengthTypeID == 1 && packages < n)) {
        for(i in 0 until tabs) print("\t")
        val version = read(VERSION_OR_TYPE_ID)
        print("Version: $version\t")
        versionSum += version
        val typeID = read(VERSION_OR_TYPE_ID)
        print("Type: $typeID\t")
        // Literal
        if(typeID == 4) {
            val number = readLiteral()
            println("Literal: $number\t")
        } else { // Operator
            val lengthType = read(LENGTH_TYPE_ID)
            print("Length type: $lengthType\t")
            val length = if(lengthType == 0) read(PACKET_LENGTH) else read(PACKET_NUMBER)
            println("Length: $length\t")
            tabs++
            readPacket(lengthType, length)
            tabs--
        }
        packages++
    }
}

readPacket(1, 1)

println("Sum of versions: $versionSum")




