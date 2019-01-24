package fr.mbds.tp

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class CustomControllerSpec extends Specification implements ControllerUnitTest<CustomController> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
