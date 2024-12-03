package system

import notifications.NotificationType
import user.User
import java.io.*

class NotificationSystem(private val file: File) {
    private val users = loadUsers().toMutableList()
    private var userIdCounter = (users.maxOfOrNull { it.id } ?: 0) + 1

    private fun loadUsers(): List<User> {
        return if (file.exists()) {
            ObjectInputStream(FileInputStream(file)).use { it.readObject() as List<User> }
        } else {
            emptyList()
        }
    }

    fun saveUsers() {
        ObjectOutputStream(FileOutputStream(file)).use { it.writeObject(users) }
    }

    fun registerUser(login: String, password: String, name: String, email: String, phone: String, preferredNotificationTypes: List<NotificationType>): User? {
        if (users.any { it.login == login }) {
            println("Пользователь с таким логином уже существует.")
            return null
        }

        val user = User(userIdCounter++, login, password, name, email, phone, preferredNotificationTypes)
        users.add(user)
        saveUsers()
        println("Пользователь ${user.name} зарегистрирован.")
        return user
    }

    fun authenticateUser(login: String, password: String): User? {
        return users.find { it.login == login && it.password == password }
    }

    fun displayUserNotifications(user: User) {
        val notifications = user.notifications.sortedByDescending { it.timeReceived }
        if (notifications.isEmpty()) {
            println("Нет уведомлений.")
        } else {
            notifications.forEach {
                println("[${it.timeReceived}] ${it.type} - ${it.message}")
            }
        }
    }

    fun getAllUsers(): List<User> = users
}