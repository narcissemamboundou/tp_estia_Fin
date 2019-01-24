package fr.mbds.tp

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class MessageController {

    MessageService messageService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond messageService.list(params), model:[messageCount: messageService.count()]
    }

    def show(Long id) {
        respond messageService.get(id)
        def messageInstance= Message.get(id)
        def userMessgeList= UserMessage.findAllByMessage(messageInstance)
        def userList= userMessgeList.collect {}
    }

    def create() {
        respond new Message(params)
    }

    def save(Message message) {
        if (message == null) {
            notFound()
            return
        }

        try {
            messageService.save(message)
        } catch (ValidationException e) {
            respond message.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = "Le message a été correctement créé (id : ${message.id})"
                redirect message
            }
            '*' { respond message, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond messageService.get(id)
    }

    def update(Message message) {
        if (message == null) {
            notFound()
            return
        }

        try {
            messageService.save(message)
        } catch (ValidationException e) {
            respond message.errors, view:'edit'
            return
        }

        // Récupérer l'id du destinataire
        // Instancier ce dernier
        // Créer une instance de UserMessage correspondant à l'envoi de ce message
        // Persister l'instance UserMessage nouvellement créée

        // Si groupe spécifié :
        // Récupérer l'instance de Role désigné
        // Créer un nouveau UserMessage pour tous les utilisateurs dudit Groupe

        request.withFormat {
            form multipartForm {
                flash.message = "Le message ayant l'id ${message.id} a été correctement mis à jour"
                redirect message
            }
            '*'{ respond message, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        messageService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'message.label', default: 'Message'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
