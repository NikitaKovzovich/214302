import notifications.*
import system.NotificationSystem
import user.*
import java.io.*
import java.util.*

fun main() {
    val notificationSystem = NotificationSystem(File("users.bin"))
    var isRunning = true

    while (isRunning) {
        println("\nВыберите опцию:")
        println("1. Вход как администратор")
        println("2. Регистрация нового клиента")
        println("3. Вход в кабинет клиента")
        println("4. Выйти")

        when (readlnOrNull()) {
            "1" -> {
                println("Введите логин:")
                val login = readlnOrNull() ?: ""
                println("Введите пароль:")
                val pass = readlnOrNull() ?: ""
                val admin = Admin.authenticate(login, pass)

                if (admin != null) {
                    var inAdminCabinet = true
                    while (inAdminCabinet) {
                        println("1. Отправить уведомление всем пользователям")
                        println("2. Выйти")

                        when (readlnOrNull()) {
                            "1" -> {
                                println("Введите сообщение для отправки всем пользователям: ")
                                val message = readlnOrNull() ?: ""
                                println("Выберите тип уведомления (EMAIL, SMS, PUSH): ")
                                val typesInput = readlnOrNull()?.split(",")?.map { it.trim()
                                    .uppercase(Locale.getDefault()) }

                                val types = typesInput?.mapNotNull {
                                    try {
                                        NotificationType.valueOf(it)
                                    } catch (e: IllegalArgumentException) {
                                        null
                                    }
                                }

                                if (!types.isNullOrEmpty()) {
                                    types.forEach { type ->
                                        val notification = admin.createNotification(message, type)
                                        admin.sendNotificationToAllUsers(notificationSystem.getAllUsers(), notification, notificationSystem)
                                    }
                                } else {
                                    println("Некорректные типы уведомлений.")
                                }
                            }
                            "2" -> {
                                inAdminCabinet = false
                                println("Выход из кабинета администратора.")
                            }
                            else -> println("Неверный выбор.")
                        }
                    }
                } else {
                    println("Неверный логин или пароль.")
                }
            }

            "2" -> {
                println("Введите логин:")
                val login = readlnOrNull() ?: ""
                println("Введите пароль:")
                val password = readlnOrNull() ?: ""
                println("Введите имя:")
                val name = readlnOrNull() ?: ""
                println("Введите email:")
                val email = readlnOrNull() ?: ""
                println("Введите номер телефона:")
                val phone = readlnOrNull() ?: ""
                println("Выберите предпочтительные типы уведомлений через запятую (EMAIL, SMS, PUSH):")
                val types = readlnOrNull()?.split(",")?.map { it.trim().uppercase(Locale.getDefault()) }?.map { NotificationType.valueOf(it) }

                if (!types.isNullOrEmpty()) {
                    notificationSystem.registerUser(login, password, name, email, phone, types)
                } else {
                    println("Некорректные типы уведомлений.")
                }
            }

            "3" -> {
                println("Введите логин:")
                val login = readlnOrNull() ?: ""
                println("Введите пароль:")
                val password = readlnOrNull() ?: ""
                val user = notificationSystem.authenticateUser(login, password)

                if (user != null) {
                    var inUserCabinet = true
                    while (inUserCabinet) {
                        println("1. Просмотреть уведомления")
                        println("2. Изменить предпочтения уведомлений")
                        println("3. Выйти")

                        when (readlnOrNull()) {
                            "1" -> notificationSystem.displayUserNotifications(user)
                            "2" -> {
                                println("Выберите новые предпочтительные типы уведомлений через запятую (EMAIL, SMS, PUSH):")
                                val newTypesInput = readlnOrNull()?.split(",")?.map { it.trim()
                                    .uppercase(Locale.getDefault()) }
                                val newTypes = newTypesInput?.mapNotNull {
                                    try {
                                        NotificationType.valueOf(it)
                                    } catch (e: IllegalArgumentException) {
                                        null
                                    }
                                }

                                if (!newTypes.isNullOrEmpty()) {
                                    user.changeNotificationPreferences(newTypes)
                                    notificationSystem.saveUsers()
                                    println("Предпочтения уведомлений обновлены.")
                                } else {
                                    println("Некорректные типы уведомлений.")
                                }
                            }
                            "3" -> {
                                inUserCabinet = false
                                println("Выход из кабинета.")
                            }
                            else -> println("Неверный выбор.")
                        }
                    }
                } else {
                    println("Неверный логин или пароль.")
                }
            }

            "4" -> isRunning = false
            else -> println("Неверный выбор.")
        }
    }
}


