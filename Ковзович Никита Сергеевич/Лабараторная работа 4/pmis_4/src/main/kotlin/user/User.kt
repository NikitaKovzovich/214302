    package user

    import notifications.*
    import java.io.Serializable

    data class User(
        val id: Int,
        val login: String,
        val password: String,
        val name: String,
        val email: String,
        val phone: String,
        var preferredNotificationTypes: List<NotificationType>,
        val notifications: MutableList<Notification> = mutableListOf()
    ) : Serializable {

        fun changeNotificationPreferences(newPreferences: List<NotificationType>) {
            preferredNotificationTypes = newPreferences
        }
    }

