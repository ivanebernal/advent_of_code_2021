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

fun binToDec(bin: String): Long {
    var result = 0L
    for (i in 0 until bin.length) {
        result = result or ((bin[i] - '0').toLong() shl (bin.length - 1 - i))
    }
    return result
}

var versionSum = 0L
var pointer = 0
var inStr = ""

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

val SUM = 0L
val PROD = 1L
val MIN = 2L
val MAX = 3L
val GT = 5L
val LT = 6L
val EQ = 7L

fun read(offset: Int): Long {
    val result = binToDec(inStr.substring(pointer, pointer + offset))
    pointer += offset
    return result
}

fun readLiteral(): Long {
    val number = StringBuilder()
    var firstDigit = read(1)
    while(firstDigit == 1L) {
        number.append(inStr.substring(pointer, pointer + 4))
        pointer += 4
        firstDigit = read(1)
    } 
    number.append(inStr.substring(pointer, pointer + 4))
    pointer += 4
    return binToDec(number.toString())
}

fun readPacket(lengthTypeID: Long, n: Long, operation: Long): Long {
    val start = pointer
    var packages = 0
    val values = mutableListOf<Long>()
    while((lengthTypeID == 0L && (n - (pointer - start)) > 0L) || (lengthTypeID == 1L && packages < n)) {
        val version = read(VERSION_OR_TYPE_ID)
        versionSum += version
        val typeID = read(VERSION_OR_TYPE_ID)
        // Literal
        if(typeID == 4L) {
            values.add(readLiteral())
        } else { // Operator
            val lengthType = read(LENGTH_TYPE_ID)
            val length = if(lengthType == 0L) read(PACKET_LENGTH) else read(PACKET_NUMBER)
            values.add(readPacket(lengthType, length, typeID))
        }
        packages++
    }

    return when(operation) {
        SUM -> {
            var result = 0L
            values.forEach { result += it }
            result
        }
        PROD -> {
            var result = 1L
            values.forEach { result *= it }
            result
        }
        MIN -> {
            var result = Long.MAX_VALUE
            values.forEach { result = Math.min(result, it) }
            result
        }
        MAX -> {
            var result = 0L
            values.forEach { result = Math.max(result, it) }
            result
        }
        GT -> {
            if(values[0] > values[1]) 1L else 0L
        }
        LT -> {
            if(values[0] < values[1]) 1L else 0L
        }
        EQ -> {
            if(values[0] == values[1]) 1L else 0L
        } 
        else -> {
            throw IllegalArgumentException()
        }
    }
}

println("Expression eval: ${readPacket(1, 1, 0)}")




