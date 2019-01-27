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
                        render (text : "UPDATE WAS SUCCES   ${messageInstance.id}")
                    }
                    else {
                        render(status: 400, text: "FAIL UPDATE ${messageInstance.id}")
                    }

                }
                else {
                    render (status: 404, text:" MESSAGE NOT FOUND")
                }
                response.status=400
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
                    render(status: 200, text: " GOOD ! MESSAGE WAS DELETED")

                }
                else {
                    render(status: 404, text: "MESSAGE NOT FOUND !")
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
                def authorInstance = params.authorId? User.get(params.authorId) : null
                def messageInstance
                if (authorInstance){
                    messageInstance = new Message(author: authorInstance, messageContent: params.messageContent)
                    if (messageInstance.save(flush: true)){
                        //response.status=201
//                        if(params.receiver.id){
//                            def receiverInstance= User.get(params.receiver.id)
//                            if(receiverInstance){new UserMessage(user: receiverInstance, Message: messageInstance).save(flush: true)}
//                        }
                        render(status: 201, message : "MESSAGE WAS CREATED")
                    }else {
                        render(status:  400, text: "MESSAGE DOESN'T EXIST")
                    }
                }
                else {
                    render(status: 400, text: "AUTHOR DOESN'T EXIST" )
                }



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
                                    render(status: 201, text: "MESSAGE ${messageInstance.id} WAS CORRECTLY SENDED")
                                else
                                    render(status: 400, text: "FAIL TO SEND MESSAGE ! ")
                            }
                            else
                                render(status: 404, text: "MESSAGE ID ${params.messageId} DOESN4T EXIST")
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
                                        render(status: 201, text: "MESSAGE ${messageInstance.id} WAS CORRECTLY SEND")
                                    else
                                        render(status: 400, text: "FAIL TO SEND MESSAGE")
                                }
                                else
                                    render(status: 400, text: "FAIL TO SEND MESSAGE")
                            }
                            else
                                render(status: 404, text: "AUTHOR OR MESSAGE NOT FOUND")
                        }
                    }
                    else
                        render(status: 404, text: "USER ID NOT FOUND")

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
                def userInstance = params.userId ? User.get(params.userId) : null
                if (userInstance) {
                    if (userInstance) {
                        if (params.username) {
                            userInstance.username = params.username
                        }
                        if (params.password) {
                            userInstance.password = params.password
                        }
                        if (params.mail) {
                            userInstance.mail = params.mail
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
                        render(status: 200, text: "UPDATE SUCCES USER ${userInstance.id} for ${userInstance.username}")
                    } else
                        render(status: 400, text: "ERROR UPDATE ${userInstance.id}")
                } else
                    render(status: 404, text: "USER NOT FOUND")
                break

            case "DELETE":
                def userInstance = params.userId ? User.get(params.userId) : null
                if (userInstance) {


                    def allMessageUser = UserMessage.findAllByUser(userInstance)
                    allMessageUser.each {UserMessage userMessage -> userMessage.delete(flush: true)}
                    userInstance.isDelete = true

                   //userInstance.delete(flush: true)
                    render(status: 201, text: "USER DELETED ")

                }
                else
                    render(status: 404, text: "USER NOT FOUND ")
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
                def userInstance = params.userId ? User.get(params.userId) : null
                if (userInstance){
                    //def newUser= new UserController(user:userInstance)
                    def newUser= new User(password: params.password, username: params.username, mail: params.mail, dob: params.dob, tel: params.tel, firstName: params.firstName, lastName: params.lastName, isDelete: params.isDelete)

                    if (newUser.save(flush: true)) {
                        render(status: 201, text: "New USER WAS CREATED")
                    }
                    if (response.status != 201)
                        response.status = 401
                }
                else {
                    render(status: 400, text: "USER DOESN'T EXIST")
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
                if(params.groupId){
                    def roleInstance = Role.get(params.groupId)
                    if(roleInstance){
                        if(params.messageId){
                            def messageInstance = Message.get(params.messageId)
                            if(messageInstance){
                                def userRoleInstance=UserRole.findAllByRole(roleInstance)
                                userRoleInstance.each{
                                    def userInstance=User.get(it.user.id)
                                    if(userInstance){
                                        def userMessageInstance = new UserMessage(user: userInstance, message: messageInstance )
                                        if(userMessageInstance.save(flush:true)) {
                                            render(status: 201, text: "MESSAGE ${messageInstance.id} WAS SENDD")
                                        }
                                    }
                                }
                            }else
                                render(status: 404, text: "MESSAGE ${params.messageId} DOESN'T EXIST")
                        }
                        else{
                            def authorInstance = params.authorId ? User.get(params.authorId) : null
                            def messageContentInstance = params.messageContent
                            if(authorInstance && messageContentInstance) {
                                def messageInstance2 = new Message(author: authorInstance, messageContent: messageContentInstance)
                                if (messageInstance2.save(flush: true)){
                                    def userRoleInstance=UserRole.findAllByRole(roleInstance)
                                    userRoleInstance.each{
                                        def userInstance=User.get(it.user.id)
                                        if(userInstance){
                                            def userMessageInstance = new UserMessage(user: userInstance, message:messageInstance2 )
                                            if(userMessageInstance.save(flush:true)) {
                                                render(status: 201, text: "MESSAGE ${messageInstance2.id} WAS SEND BY ${messageInstance2.author} ")
                                            }else{
                                                render(status: 400, text: "ERROR TO SEND MESSAGE")
                                            }
                                        }
                                    }
                                }else
                                    render(status: 400, text: "Fail TO SEND MESSAGE FOR USER ")
                            }else
                                render(status: 404, text: "MESSAGE OR AUTHOR IS NOT FOUND ")
                        }
                    }else
                        render(status: 404, text: " GROUP NOT FOUND ")
                }else
                    render(status: 400, text: "GROUP ID DOESN'T EXIST")
                break
            default:
                response.status=405
        }
    }


}
