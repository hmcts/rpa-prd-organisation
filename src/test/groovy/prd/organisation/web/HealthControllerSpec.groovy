package prd.organisation.web

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class HealthControllerSpec extends Specification implements ControllerUnitTest<HealthController> {

    void "test health check"() {
        when: "we call the health check endpoint"
        controller.index()

        then: "we expect to see it tell us it is up"
        response.status == 200
        response.json.status == 'UP'
    }
}