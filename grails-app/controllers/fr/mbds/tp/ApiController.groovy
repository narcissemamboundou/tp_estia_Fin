package fr.mbds.tp


import grails.converters.JSON
import grails.converters.XML
import org.apache.catalina.servlet4preview.http.HttpServletRequest

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
                    if (params.author.id){
                        def authorInstance = User.get(params.author.id)
                        if (authorInstance){
                            messageInstance.author= authorInstance
                        }
                    }
                    if (params.messageContent){
                        messageInstance.messageContent=params.messageContent
                    }
                    if (messageInstance.save(flush : true)){
                        render (text : "Message updated ! ${messageInstance.id}")
                    }
                    else {
                        render(status: 400, text: "Faillure to update ${messageInstance.id}")
                    }

                }
                else {
                    render (status: 404, text: "Damn!  message not found !")
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
                        render(status: 201)
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
                if(params.UserId){
                    def receiverInstance = User.get(params.UserId)
                    if(receiverInstance){
                        if(params.MessageId){
                            def messageInstance = Message.get(params.MessageId)
                            if(messageInstance){
                                def userMessageInstance = new UserMessage(user: receiverInstance, message: messageInstance)
                                if(userMessageInstance.save(flush: true))
                                    render(status: 201, text: "Message ${messageInstance.id} was correctly created")
                                else
                                    render(status: 400, text: "Fail to create message ! ")
                            }
                            else
                                render(status: 404, text: "Message ${params.MessageId} doesn't exist")
                        }
                        else
                        {
                            def authorInstance = params.AuthorId ? User.get(params.AuthorId) : null
                            def messageContentInstance = params.MessageContent
                            if(authorInstance && messageContentInstance){
                                def messageInstance = new Message(author: authorInstance, messageContent: messageContentInstance)
                                if(messageInstance.save(flush:true)){
                                    def userMessageInstance = new UserMessage(user: receiverInstance, message: messageInstance)
                                    if(userMessageInstance.save(flush: true))
                                        render(status: 201, text: "Message ${messageInstance.id} w")
                                    else
                                        render(status: 400, text: "Failure to create message")
                                }
                                else
                                    render(status: 400, text: "Failure to create userMessage")
                            }
                            else
                                render(status: 404, text: "author or messageContent not found")
                        }
                    }
                    else
                        render(status: 404, text: "user not found")
                }
                else
                    render(status: 400, text: "miss  userId")
                break
            default:
                response.status=405
        }
    }


}
