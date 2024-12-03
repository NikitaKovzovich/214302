// (sin(30) + cos(π)) * 5 + (log_2(8) / (sqrt(16) - 2))

// (((log_2(64) * 5) * (tan(120))) + 1) / ((log_3(81))^(cot((e)^(3*π))))

import java.util.*
import kotlin.math.*

fun main() {

    println("\nЛогарифмы записываются: log_a(b), где a - основание логарифма, b - аргумент логарифма.")
    println("Натуральный логарифм: ln(b), где b - аргумент логарифма.")
    println("Извлечь корень: sqrt(a), где a - неотрицательное число.")
    println("PI: π - использовать для тригонометрических функций (π = 180 градусов), в остальных ситуациях: pi = 3,14... .")
    println("Экспонента: e.")
    println("Синус: sin(a), где a мб в радианах sin(2*π), либо градусах sin(360)")
    println("Косинус: cos(a), где a мб в радианах cos(2*π), либо градусах cos(360)")
    println("Тангенс: tan(a), где a мб в радианах tan(2*π), либо градусах tan(360)")
    println("Котангенс: cot(a), где a мб в радианах cot(π/2), либо градусах cot(90)")

    while (true) {
        println("\nВведите математическое выражение (или введите 0 для выхода): ")
        val expression = readLine() ?: ""

        if (expression == "0") break

        try {
            val result = evaluateExpression(expression)
            println("Результат: $result")
        } catch (e: Exception) {
            println("Ошибка: ${e.message}")
        }
    }
}

val add: (Double, Double) -> Double = { a, b -> a + b }
val subtract: (Double, Double) -> Double = { a, b -> a - b }
val multiply: (Double, Double) -> Double = { a, b -> a * b }
val divide: (Double, Double) -> Double = { a, b ->
    if (b == 0.0) throw IllegalArgumentException("Ошибка: Деление на ноль")
    a / b
}
val power: (Double, Double) -> Double = { a, b -> a.pow(b) }
val sqrtFunc: (Double) -> Double = { a ->
    if (a < 0) throw IllegalArgumentException("Ошибка: Невозможно извлечь корень из отрицательного числа")
    sqrt(a)
}

val epsilon = 1e-10  // Порог точности

fun toRadians(degrees: Double): Double {
    return Math.toRadians(degrees)
}

val sinFunc: (Double) -> Double = { value ->
    sin(toRadians(value))
}

val cosFunc: (Double) -> Double = { value ->
    cos(toRadians(value))
}

val tanFunc: (Double) -> Double = { value ->
    if (abs(cos(toRadians(value))) < epsilon) throw IllegalArgumentException("Ошибка: Тангенс не определён для угла $value")
    tan(toRadians(value))
} // 90

val cotFunc: (Double) -> Double = { value ->
    if (abs(sin(toRadians(value))) < epsilon) throw IllegalArgumentException("Ошибка: Котангенс не определён для угла $value")
    1 / tan(toRadians(value))
} // 0

val lnFunc: (Double) -> Double = { value -> ln(value) }
val logFunc: (Double, Double) -> Double = { base, value ->
    if (base <= 0 || base == 1.0) throw IllegalArgumentException("Некорректное основание логарифма: $base")
    ln(value) / ln(base)
}

fun evaluateExpression(expr: String): Double {
    val tokens = tokenize(expr.replace(" ", ""))
    val result = parseExpression(tokens)
    if (tokens.isNotEmpty()) throw IllegalArgumentException("Некорректное выражение")
    return result
}

fun tokenize(expr: String): Queue<String> {
    val tokens = LinkedList<String>()
    var i = 0
    while (i < expr.length) {
        val c = expr[i]
        when {
            c.isDigit() -> {
                val number = StringBuilder()
                while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                    number.append(expr[i])
                    i++
                }
                tokens.add(number.toString())
            }
            c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '^' -> {
                tokens.add(c.toString())
                i++
            }
            c.isLetter() -> {
                val function = StringBuilder()
                while (i < expr.length && (expr[i].isLetter() || expr[i] == '_' || expr[i].isDigit())) {
                    function.append(expr[i])
                    i++
                }
                tokens.add(function.toString())
            }
            else -> throw IllegalArgumentException("Некорректный символ: $c")
        }
    }
    return tokens
}

fun parseExpression(tokens: Queue<String>): Double {
    var result = parseTerm(tokens)
    while (tokens.isNotEmpty() && (tokens.peek() == "+" || tokens.peek() == "-")) {
        val operator = tokens.poll()
        val nextTerm = parseTerm(tokens)
        result = when (operator) {
            "+" -> add(result, nextTerm)
            "-" -> subtract(result, nextTerm)
            else -> result
        }
    }
    return result
}

fun parseTerm(tokens: Queue<String>): Double {
    var result = parseFactor(tokens)
    while (tokens.isNotEmpty() && (tokens.peek() == "*" || tokens.peek() == "/")) {
        val operator = tokens.poll()
        val nextFactor = parseFactor(tokens)
        result = when (operator) {
            "*" -> multiply(result, nextFactor)
            "/" -> divide(result, nextFactor)
            else -> result
        }
    }
    return result
}

fun parseFactor(tokens: Queue<String>): Double {
    var result = parseExponent(tokens)
    while (tokens.isNotEmpty() && tokens.peek() == "^") {
        tokens.poll()
        val exponent = parseExponent(tokens)
        result = power(result, exponent)
    }
    return result
}

fun parseExponent(tokens: Queue<String>): Double {
    val token = tokens.poll() ?: throw IllegalArgumentException("Недостаточно аргументов")
    return when {
        token == "(" -> {
            val result = parseExpression(tokens)
            if (tokens.poll() != ")") throw IllegalArgumentException("Пропущена закрывающая скобка")
            result
        }
        token.matches(Regex("-?\\d+(\\.\\d+)?")) -> token.toDouble()
        token == "-" -> -parseExponent(tokens)
        token == "π" -> 180.0
        token == "pi" -> Math.PI
        token == "e" -> Math.E
        token == "sqrt" -> sqrtFunc(parseExponent(tokens))
        token == "sin" -> sinFunc(parseExponent(tokens))
        token == "cos" -> cosFunc(parseExponent(tokens))
        token == "tan" -> tanFunc(parseExponent(tokens))
        token == "cot" -> cotFunc(parseExponent(tokens))
        token == "ln" -> lnFunc(parseExponent(tokens))
        token.startsWith("log_") -> {
            val baseString = token.substring(4)
            val base = baseString.toDoubleOrNull()
                ?: throw IllegalArgumentException("Некорректное основание логарифма: $baseString")
            logFunc(base, parseExponent(tokens))
        }
        else -> throw IllegalArgumentException("Некорректный токен: $token")
    }
}




