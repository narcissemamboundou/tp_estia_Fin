package fr.mbds.tp

class Message {

    String messageContent

    User author

    Date dateCreated

    static constraints = {
        messageContent nullable: false, blank: false
        author nullable: false
        dateCreated nullable: false
    }
}
