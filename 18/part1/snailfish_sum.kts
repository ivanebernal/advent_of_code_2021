import java.io.File
import java.lang.IllegalArgumentException

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

class SFNumber(val nums: MutableList<Int>) {

    operator fun plus(other: SFNumber): SFNumber {
        val result = mutableListOf<Int>(-1)
        result.addAll(nums)
        result.addAll(other.nums)
        result.add(-2)
        return SFNumber(result).apply { reduce() }
    }

    fun reduce() {
        while(explode() || split());
    }

    fun split(): Boolean {
        for(i in 0 until nums.size) {
            if(nums[i] > 9) {
                val splitN = mutableListOf<Int>()
                splitN.add(nums[i]/2)
                splitN.add(if(nums[i]%2 == 0) nums[i]/2 else nums[i]/2 + 1)
                splitN.add(-2)
                nums[i] = -1
                nums.addAll(i + 1, splitN)
                return true
            }
        }
        return false
    }

    fun explode(): Boolean {
        var level = 0
        for(i in 0 until nums.size) {
            if(nums[i] == -1) level++
            else if(nums[i] == -2) level--
            if(level > 4) {
                val left = nums[i + 1]
                val right = nums[i + 2]
                nums[i] = 0
                nums.removeAt(i + 1)
                nums.removeAt(i + 1)
                nums.removeAt(i + 1)
                // Add left
                var idx = i - 1
                while(idx >= 0 && nums[idx] < 0) {
                    idx--
                }
                if(idx >= 0 && nums[idx] >= 0) nums[idx] = nums[idx] + left
                // Add right
                idx = i + 1
                while(idx < nums.size && nums[idx] < 0) {
                    idx++
                }
                if(idx < nums.size && nums[idx] >= 0) nums[idx] = nums[idx] + right
                return true
            }
        }
        return false
    }

    fun magnitude(start: Int = 0, end: Int = nums.size): Long {
        if(end - start == 1) {
            // println(nums[start])
            return nums[start].toLong()
        }
        // Get left magnitude
        val ls = start + 1
        var le = ls
        var level = 0
        do {
            if(nums[le] == -1) level++
            else if(nums[le] == -2) level--
            le++
        } while(level > 0)
        val leftMagnitude = 3L * magnitude(ls, le)
        // Get right magnitude
        val rs = le
        var re = rs
        level = 0
        do {
            if(nums[re] == -1) level++
            else if(nums[re] == -2) level--
            re++
        } while(level > 0)
        val rightMagnitude = 2L * magnitude(rs, re)
        val magnitude = leftMagnitude + rightMagnitude
        return magnitude
    }
}

val numbers = mutableListOf<SFNumber>()

fun parseNumber(number: String): SFNumber {
    val num = mutableListOf<Int>()
    for(c in number) {
        if(c == '[') num.add(-1)
        else if(c == ']') num.add(-2)
        else if(c != ',') num.add(c - '0')
    }
    return SFNumber(num)
}

File(input).forEachLine { line ->
    numbers.add(parseNumber(line))
}

var result = numbers[0]
for(i in 1 until numbers.size) {
    result = result + numbers[i]
}

val magnitude = result.magnitude()

println("Magnitude: $magnitude")