package fr.mbds.tp


import grails.converters.JSON
import grails.converters.XML

import javax.servlet.http.HttpServletRequest

//import org.apache.catalina.servlet4preview.http.HttpServletRequest

class ApiController {

    def index() { render text: "test ok"}

    def message (){
        switch (request.getMethod()){
            case "GET":
                if (params.id){// on doit retourner une instance de message
                    def messageInstance= Message.get(params.id)
                    if (messageInstance){
                        responseFormat(messageInstance, request)
                    }
                    else{
                        response.status=404
                    }
                }else{//on doit retourner la liste de tous les messages
                    forward action: "messages"
                }
            case "POST":
                forward action: "messages"
                break
            case "PUT":
                def messageInstance = params.id? Message.get(params.id) : null
                if (messageInstance){
                    if (params.authorId){
                        def authorInstance = User.get(params.authorId)
                        if (authorInstance){
                            messageInstance.author= authorInstance
                        }
                    }
                    if (params.messageContent){
                        messageInstance.messageContent=params.messageContent
                    }
                    if (messageInstance.save(flush : true)){
                        render (text : "mise a jour effectuée du message ${messageInstance.id}")
                    }
                    else {
                        render(status: 400, text: "Echec de mise a jour ${messageInstance.id}")
                    }

                }
                else {
                    render (status: 404, text: "le message est introuvable")
                }
                break

            case "DELETE" :
                def messageInstance = params.id? Message.get(params.id):null
                if (messageInstance){
                    //on repere la liste des userMessage qui refferent les message que tous les efface
                    def userMessages = UserMessage.findAllByMessage(messageInstance)
                    //on
                    userMessages.each {
                        UserMessage userMessage->userMessage.delete(flush: true)
                    }
                    //on peut enfin effacer l'instance de message

                    messageInstance.delete(flush: true)
                    render(status: 200, text: " Good ! Message delete")

                }
                else {
                    render(status: 404, text: "Message not found")
                }
                break
            default:
                response.status=405

        }
    }

    def responseFormat(Object instance, HttpServletRequest request ){
        switch (request.getHeader("Accept")){
            case "text/xml":
                render instance as XML
                break
            case "text/json":
                render instance as JSON
                break
        }
    }

    def messages(){
        switch (request.getMethod()){
            case "GET":
                responseFormatList(Message.list(), request)
                break
            case "POST":
                //verifier auteur
                def authorInstance = params.author.id? User.get(params.author.id) : null
                def messageInstance
                if (authorInstance){
                    messageInstance = new Message(author: authorInstance, messageContent: params.messageContent)
                    if (messageInstance.save(flush: true)){
                        //response.status=201
//                        if(params.receiver.id){
//                            def receiverInstance= User.get(params.receiver.id)
//                            if(receiverInstance){new UserMessage(user: receiverInstance, Message: messageInstance).save(flush: true)}
//                        }
                        render(status: 201, message : "message was created")
                    }
                }

                response.status=400

                //creer le message
                //ajouter destinataire
                break
        }
    }


    def responseFormatList(List list, HttpServletRequest request){
        switch (request.getHeader("Accept")){
            case "text/xml":
                render list as XML
                break
            case "text/json":
                render list as JSON
                break
            default:
                response.status=415
        }
    }

    def messageToUser (){
        switch (request.getMethod()){

            case "POST":
                    def receiverInstance = params.userId? User.get(params.userId): null
                    if(receiverInstance){
                        if(params.messageId){
                            def messageInstance = Message.get(params.messageId)
                            if(messageInstance){
                                def userMessageInstance = new UserMessage(user: receiverInstance, message: messageInstance)
                                if(userMessageInstance.save(flush: true))
                                    render(status: 201, text: "Message ${messageInstance.id} was correctly send")
                                else
                                    render(status: 400, text: "Fail to send message ! ")
                            }
                            else
                                render(status: 404, text: "Message ID ${params.messageId} doesn't exist")
                        }
                        else
                        {
                            def authorInstance = params.authorId ? User.get(params.authorId) : null
                            def messageContentInstance = params.messageContent
                            if(authorInstance && messageContentInstance){
                                def messageInstance = new Message(author: authorInstance, messageContent: messageContentInstance)
                                if(messageInstance.save(flush:true)){
                                    def userMessageInstance = new UserMessage(user: receiverInstance, message: messageInstance)
                                    if(userMessageInstance.save(flush: true))
                                        render(status: 201, text: "Message ${messageInstance.id} was correctly send")
                                    else
                                        render(status: 400, text: "Failure to create message")
                                }
                                else
                                    render(status: 400, text: "Failure to send userMessage")
                            }
                            else
                                render(status: 404, text: "Not found")
                        }
                    }
                    else
                        render(status: 404, text: "userID not found")

                break
            default:
                response.status=405
        }
    }


    def user() {
        switch (request.getMethod()) {
            case "GET":
                if (params.id) //On doit retourner une instance de message
                {
                    def userInstance = User.get(params.id)
                    if (userInstance) {
                        responseFormat(userInstance, request)
                    } else
                        response.status = 404
                } else
                    forward action: "users"
                break

            case "POST":
                forward action: "users"
                break

            case "PUT":
                def userInstance = params.id ? User.get(params.id) : null
                if (userInstance) {
                    if (userInstance) {
                        if (params.username) {
                            userInstance.username = params.username
                            userInstance.save(flush: true)
                        }
                        if (params.password) {
                            userInstance.password = params.password
                        }
                        if (params.email) {
                            userInstance.email = params.email
                        }
                        if (params.dob) {
                            userInstance.dob = params.dob
                        }
                        if (params.tel) {
                            userInstance.tel = params.tel
                        }
                        if (params.firstName) {
                            userInstance.firstName = params.firstName
                        }
                        if (params.lastName) {
                            userInstance.lastName = params.lastName
                        }
                        userInstance.save(flush: true)
                    }
                    if (userInstance.save(flush: true)) {
                        render(status: 200, text: "update successful for user ${userInstance.id} for ${userInstance.username}")
                    } else
                        render(status: 400, text: "echec de la MAJ ${userInstance.id}")
                } else
                    render(status: 404, text: "user not found")
                break

            case "DELETE":
                def userInstance = params.id ? User.get(params.id) : null
                if (userInstance) {
                    userInstance.isDelete = false
                    userInstance.delete(flush: true)
                    render(status: 201, text: "user deleted ")
                }
                else
                    render(status: 404, text: "user not found ")
                break
            default:
                response.status = 405
                break
        }
    }

    def users() {
        switch (request.getMethod()) {
            case "GET":
                responseFormatList(User.list(), request)
                break
            case "POST":

                def userInstance = new User(password: params.password, username: params.username, mail: params.mail, dob: params.dob, tel: params.tel, firstName: params.firstName, lastName: params.lastName, isDelete: params.isDelete)
                if (userInstance){
                    //def newUser= new UserController(user:userInstance)
                    def newUser= new UserController(userInstance).create()

                    if (newUser.save(flush: true)) {
                        render(status: 201, text: "Nouvel utilisateur ${User.id} created")
                    }
                    if (response.status != 201)
                        response.status = 410
                }

                break
            default:
                response.status = 405
                break
        }
    }




    def messageToGroup (){
        switch (request.getMethod()){

            case "POST":
                if(params.RoleId){
                    def roleReceiverInstance = Role.get(params.RoleId)
                    if(roleReceiverInstance){
                        if(params.MessageId){
                            def messageInstance = Message.get(params.MessageId)
                            if(messageInstance){
                                def userRoleInstance=UserRole.findAllByRole(roleReceiverInstance)
                                userRoleInstance.each{
                                    def userInstance=User.get(it.user.id)
                                    if(userInstance){
                                        def userMessageInstance = new UserMessage(user: userInstance, message: messageInstance )
                                        if(userMessageInstance.save(flush:true)) {
                                            render(status: 201, text: "Message ${messageInstance.id} was created")
                                        }else{
                                            render(status: 400, text: "Fail to create message")
                                        }
                                    }
                                }
                            }else
                                render(status: 404, text: "Message ${params.MessageId} doesn't exist")
                        }else{
                            def authorInstance = params.AuthorId ? User.get(params.AuthorId) : null
                            def messageContentInstance = params.MessageContent
                            if(authorInstance && messageContentInstance) {
                                def newMessageInstance = new Message(author: authorInstance, messageContent: messageContentInstance)
                                if (newMessageInstance.save(flush: true)){
                                    def userRoleInstance=UserRole.findAllByRole(roleReceiverInstance)
                                    userRoleInstance.each{
                                        def userInstance=User.get(it.user.id)
                                        if(userInstance){
                                            def userMessageInstance = new UserMessage(user: userInstance, message: messageInstance )
                                            if(userMessageInstance.save(flush:true)) {
                                                render(status: 201, text: "Message ${messageInstance.id} was created")
                                            }else{
                                                render(status: 400, text: "fail to create message")
                                            }
                                        }
                                    }
                                }else
                                    render(status: 400, text: "fail to create message of user")
                            }else
                                render(status: 404, text: "not found (message  or author)")
                        }
                    }else
                        render(status: 404, text: " role not found ")
                }else
                    render(status: 400, text: "RoleId doesn't exist")
                break
            default:
                response.status=405
        }
    }




}
