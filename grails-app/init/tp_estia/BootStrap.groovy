package tp_estia

import fr.mbds.tp.Message
import fr.mbds.tp.Role
import fr.mbds.tp.User
import fr.mbds.tp.UserMessage
import fr.mbds.tp.UserRole

class BootStrap {

    def init = { servletContext ->
        def userAdmin = new User(username: "admin",password: "secret", firstName: "admin",lastName: "adminLast", mail: "adminMail", isDelete: false).save(flush: true, failOneError: true)
        def roleAdmin = new Role(authority : "ROLE_ADMIN",isDelete: false).save(flush: true, failOneError: true)


        def userUser=new User(username: "user", password: "secret", firstName: "Michaud", lastName: "Antoine", mail: "usermail").save(flush: true)
        def roleUser=new Role(authority: "ROLE_USER").save(flush: true, failOneError: true)



        UserRole.create(userAdmin,roleAdmin,true)
        (1..50).each {
            def userInstance = new User(username: "username-$it", password: "password", firstName: "first", lastName: "last", mail: "mail-$it", isDelete: false)
                    .save (flush: true, failOnError: true)

            new Message(messageContent: "lalaPA", author: userInstance, isDelete: false).save(flush:true, failOnError:true)
        }
        Message.list().each {
            Message messageInstance ->

            User.list().each {
                User userInstance ->
                    UserMessage.create(userInstance, messageInstance, true)
            }
        }
    }
    def destroy = {
    }
}
