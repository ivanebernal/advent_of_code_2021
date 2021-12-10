import java.io.File
import java.lang.IllegalArgumentException
import java.util.ArrayDeque

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val scoreMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val openers = setOf('(', '[', '{', '<')
val closers = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
val corruptedCount = mutableMapOf<Char, Int>()

File(input).forEachLine { line ->
    val chunkStack = ArrayDeque<Char>()
    for(char in line.toCharArray()) {
        if(openers.contains(char)) {
            chunkStack.push(char)
        } else if(chunkStack.size > 0 && char == closers[chunkStack.peek()]) {
            chunkStack.poll()
        } else {
            corruptedCount.put(char, (corruptedCount[char] ?: 0) + 1)
            break
        }
    }
}

var result = 0
for(entry in corruptedCount) {
    result += entry.value * scoreMap[entry.key]!!
}

println("File syntax error score: $result")