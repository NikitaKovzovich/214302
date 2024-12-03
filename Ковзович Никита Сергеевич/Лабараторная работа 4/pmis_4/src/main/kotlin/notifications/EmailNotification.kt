package notifications

class EmailNotification(message: String, timeReceived: String) : Notification(message, timeReceived) {
    override val type = NotificationType.EMAIL

    override fun send(recipientContact: String) {
        println("Отправка на Email  $recipientContact: $message")
    }
}