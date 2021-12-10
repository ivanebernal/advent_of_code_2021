import java.io.File
import java.lang.IllegalArgumentException
import java.util.ArrayDeque
import java.lang.StringBuilder

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

val scoreMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
val openers = setOf('(', '[', '{', '<')
val closers = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
val lineScores = mutableListOf<Long>()

File(input).forEachLine { line ->
    val chunkStack = ArrayDeque<Char>()
    var isLineCorrupted = false
    for(char in line.toCharArray()) {
        if(openers.contains(char)) {
            chunkStack.push(char)
        } else if(chunkStack.size > 0 && char == closers[chunkStack.peek()]) {
            chunkStack.poll()
        } else {
            isLineCorrupted = true
            break
        }
    }
    if(!isLineCorrupted) {
        var score = 0L
        while(!chunkStack.isEmpty()) {
            val closer = closers[chunkStack.poll()]!!
            score *= 5
            score += scoreMap[closer]!!
        }
        lineScores.add(score)
    }
}

val sortedScores = lineScores.sorted()

println("Autocomplete score: ${sortedScores[sortedScores.size/2]}")