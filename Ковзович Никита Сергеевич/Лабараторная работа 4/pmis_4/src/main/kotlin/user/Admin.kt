package user

import notifications.*
import system.NotificationSystem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Admin private constructor() {
    companion object {
        private const val USERNAME = "admin"
        private const val PASSWORD = "1"

        fun authenticate(inputUsername: String, inputPassword: String): Admin? {
            return if (inputUsername == USERNAME && inputPassword == PASSWORD) Admin() else null
        }
    }

    fun createNotification(message: String, type: NotificationType): Notification {
        val timeReceived = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return when (type) {
            NotificationType.EMAIL -> EmailNotification(message, timeReceived)
            NotificationType.SMS -> SMSNotification(message, timeReceived)
            NotificationType.PUSH -> PushNotification(message, timeReceived)
        }
    }

    fun sendNotificationToAllUsers(users: List<User>, notification: Notification, notificationSystem: NotificationSystem) {
        users.forEach { user ->
            if (user.preferredNotificationTypes.contains(notification.type)) {
                when (notification.type) {
                    NotificationType.EMAIL -> notification.send(user.email)
                    NotificationType.SMS -> notification.send(user.phone)
                    NotificationType.PUSH -> notification.send("")
                }
                user.notifications.add(notification)
            }
        }
        notificationSystem.saveUsers()
    }
}