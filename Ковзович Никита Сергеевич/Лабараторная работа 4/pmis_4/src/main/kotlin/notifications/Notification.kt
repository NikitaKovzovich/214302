package notifications

import java.io.Serializable


abstract class Notification(val message: String, val timeReceived: String) : Serializable {
    abstract val type: NotificationType
    abstract fun send(recipientContact: String)
}