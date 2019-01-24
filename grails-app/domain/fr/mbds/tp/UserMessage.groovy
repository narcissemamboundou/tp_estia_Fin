package fr.mbds.tp

class UserMessage implements Serializable {

    User user
    Message message

    static constraints = {
    }

    static mapping = {
        id composite: ['user', 'message']
        version false
    }

    static UserMessage create(User user, Message message, boolean flush = false) {
        def instance = new UserMessage(user: user, message: message)
        instance.save(flush: flush)
        return instance
    }
}
