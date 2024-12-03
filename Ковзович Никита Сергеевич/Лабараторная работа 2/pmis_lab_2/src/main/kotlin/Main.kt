// Реализуйте программу, которая генерирует все возможные перестановки заданного массива чисел и выводит их.



fun main() {
    println("Введите элементы массива через пробел:")
    val input = readln()
    val array = input.split(" ").map { it.toInt() }

    val result = permute(array)

    println("Все возможные перестановки:")
    result.forEach { println(it) }
}

fun permute(array: List<Int>): List<List<Int>> {

    if (array.size == 1) return listOf(array)

    val permutations = mutableListOf<List<Int>>()
    val sub = array.drop(1)

    for (perm in permute(sub)) {
        for (i in array.indices) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, array[0])
            permutations.add(newPerm)
        }
    }
    return permutations
}















/*       методы массивов:
* arr.size
*     contentToString() - вывод значений массива
*     joinToString - форматирует элементы массива в строку с разделителями,
*     forEachIndexed - получать и значение и индекс
*      reverse() - перевернуть
*      remove()
*     count () (val count = numbers.count { it > 3 })
*      sort()
*     sortDescending()
*     sortBy(условие)
*     contains() - в скобках что содержит, если содержит true нет - false
*     average() - среднее
*     sum
*     min max
*      intersect() - найти общие элементы двух массивов (firstArray.intersect(secondArray.toList()).toIntArray()
*      shuffle() - перемешать элементы массива в случайном порядке
*      distinct() - вернеет массив без дупликатов
* */