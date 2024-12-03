//Напишите программу, которая вычисляет как среднее арифметическое,
// так и среднее геометрическое и гармоническое для ряда чисел.
// Реализуйте проверку на корректность ввода данных.

import kotlin.math.pow

fun main() {
    val length: Int

    while(true) {
        print("Введите длину массива: ")
        val input = readLine()?.toIntOrNull()

        if(input != null  && input > 0) {
            length = input.toInt()
            break;
        }
        else{
            println("Неккоретный ввод. Попробуйте снова")
        }
    }

    val numbers = arrayOfNulls<Float>(length)

    println("Введите $length элементов массива(положительные числа): ")

    var counter = 0

    while(counter < length) {

        val input = readLine()?.toFloatOrNull()

        if(input != null && input > 0) {
            numbers[counter] = input.toFloat()
            counter++
        }
        else{
            println("Неккоретный ввод. Попробуйте снова")
        }
    }

    countArithmetic(length, numbers)
    countGeometric(length, numbers)
    countHarmonic(length, numbers)

}

fun countHarmonic(length: Int, numbers: Array<Float?>) {

    val H:Float
    var sum = 0f

    for(number in numbers){
        if (number != null) {
            sum += 1/number
        }
    }
    H = length.toFloat() / sum
    println("Среднее гармоническое: $H")

}

fun countArithmetic (length: Int, numbers: Array<Float?>){

    val A:Float
    var sum = 0f

    for (number in numbers){
        if (number != null) {
            sum += number
        }
    }

    A = sum / length.toFloat()

    println("Среднее арифметическое: $A")

}

fun countGeometric(length: Int, numbers: Array<Float?>){

    val G:Float
    var  multiplication = 1f

    for (number in numbers){
        if (number != null) {
            multiplication *= number
        }
    }

    G = multiplication.pow(1/length.toFloat())

    println("Среднее геометрическое: $G")
}
