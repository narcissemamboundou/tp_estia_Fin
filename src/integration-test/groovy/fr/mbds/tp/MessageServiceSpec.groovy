package fr.mbds.tp

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class MessageServiceSpec extends Specification {

    MessageService messageService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Message(...).save(flush: true, failOnError: true)
        //new Message(...).save(flush: true, failOnError: true)
        //Message message = new Message(...).save(flush: true, failOnError: true)
        //new Message(...).save(flush: true, failOnError: true)
        //new Message(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //message.id
    }

    void "test get"() {
        setupData()

        expect:
        messageService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Message> messageList = messageService.list(max: 2, offset: 2)

        then:
        messageList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        messageService.count() == 5
    }

    void "test delete"() {
        Long messageId = setupData()

        expect:
        messageService.count() == 5

        when:
        messageService.delete(messageId)
        sessionFactory.currentSession.flush()

        then:
        messageService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Message message = new Message()
        messageService.save(message)

        then:
        message.id != null
    }
}
