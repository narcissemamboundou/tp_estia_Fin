package fr.mbds.tp


import grails.converters.JSON
import grails.converters.XML
import org.apache.catalina.servlet4preview.http.HttpServletRequest


import org.springframework.http.HttpRequest



class ApiController {

    def index() { render text: "ok"}

    def message (){
        switch (request.getMethod()){
            case "GET":
                if (params.id){// on doit retourner une instance de message
                    def messageInstance= Message.get(params.id)
                    if (messageInstance){
                        reponseFormat(messageInstance, request)
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
                    def userMesssage = UserMessage.findAllByMessage(messageInstance)
                    //on
                    userMesssage.each {
                        UserMessage userMessage->userMessage.delete(flush: true)
                    }
                    //on peut enfin effacer l'instance de message

                    messageInstance.delete(flush: true)
                    render(status: 200, text: "Mesage effacer")

                }
                else {
                    render(status: 404, text: "Mesage introuver")
                }
                break
            default:
                response.status=405

        }
    }

    def reponseFormat(Object instance, HttpServletRequest request ){
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
                reponseFormatList(Message.list(), request)
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

    def reponseFormatList(List list, HttpServletRequest request){
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


}
