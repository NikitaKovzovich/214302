package notifications

class PushNotification(message: String, timeReceived: String) : Notification(message, timeReceived) {
    override val type = NotificationType.PUSH

    override fun send(recipientContact: String) {
        println("Отправка Push-уведомления: $message")
    }
}