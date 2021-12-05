import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

val input = if(args.contains("-i")) args[1 + args.indexOf("-i")] else throw IllegalArgumentException("Specifile a file with -i")

class BingoCard(values: List<String>) {
    var unmarkedSum = 0
    val map = mutableMapOf<Int, List<Int>>()
    val marksInRow = mutableListOf(0,0,0,0,0)
    val marksInCol = mutableListOf(0,0,0,0,0)
    
    /*
    We are not really interested in the grid, we are interested in whether a given number is in a grid, 
    and how many numbers are marked in a given row or column
     */ 
    init {
        values.forEachIndexed { row, line -> 
            line.split(" ").filter { !it.isEmpty() }.forEachIndexed { col, v ->
                // Sum all numbers and substract when a given number is in the card
                unmarkedSum += v.toInt()
                map.put(v.toInt(), listOf(row, col))
            } 
        }
    }

    /**
     * Marks a value in this bingo card if it's present. Returns true if the card wins after being marked
     */
    fun mark(drawn: Int): Boolean {
        if(!map.contains(drawn)) return false
        unmarkedSum -= drawn
        val coord = map.get(drawn)!!
        marksInRow[coord[0]]++
        marksInCol[coord[1]]++
        return marksInRow[coord[0]] == 5 || marksInCol[coord[1]] == 5
    }
}

var drawnNumbers = mutableListOf<Int>()
val lineBuffer = mutableListOf<String>()
val cards = mutableListOf<BingoCard>()

// Read file
File(input).forEachLine { line ->
    if(line.isEmpty()) {
        if(lineBuffer.size == 1) {
            // First line of input consists of all drawn numbers separated by a comma
            drawnNumbers.addAll(lineBuffer.first().split(",").map { v -> v.toInt() })
        } else {
            // All bingo cards are separated by an empty line
            cards.add(BingoCard(lineBuffer))
        }
        lineBuffer.clear()
    } else {
        lineBuffer.add(line)
    }
}

drawnNumbers.forEach { n ->   
    var cardNumber = cards.size
    var idx = 0
    while(idx < cardNumber) {
        if(cards[idx].mark(n)) {
            if(cards.size == 1) {
                println("Last winner! Score: ${n * cards[0].unmarkedSum}")
                exitProcess(0)
            }
            // Remove the winning card if it's not the last one
            cards.removeAt(idx)
            idx--
            cardNumber--
        }
        idx++
    }
}