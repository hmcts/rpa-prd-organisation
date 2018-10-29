package prd.organisation.service

import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic


@Transactional
class UsersService {

    def configurationService

    def createUser(String firstName, String lastName, String email, String[] roles) {
        println "IN USERS SERVICE"
        RestBuilder rest = new RestBuilder()
        String url = "${configurationService.getPrdUsersRestEndpointURL()}/user"
        println "REST URL = $url"
        
        String jsonPayload = "{\"firstName\": \"$firstName\", \"lastName\": \"$lastName\", \"email\": \"$email\", \"roles\": \"$roles\"}"
        println "JSON PAYLOAD = $jsonPayload"

        RestResponse restResponse = rest.post(url) {
            accept("application/json")
            contentType("application/json")
            body(jsonPayload)
        }

        println "RESPONSE = ${restResponse}"

        if ( restResponse.statusCode.value() == 201 && restResponse.json ) {
            return restResponse.json
        }
    }

}
