package notifications

class SMSNotification(message: String, timeReceived: String) : Notification(message, timeReceived) {
    override val type = NotificationType.SMS

    override fun send(recipientContact: String) {
        println("Отправка SMS на номер $recipientContact: $message")
    }
}