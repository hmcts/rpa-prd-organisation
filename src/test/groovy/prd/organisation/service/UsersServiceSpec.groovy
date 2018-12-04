package prd.organisation.service

import com.stehno.ersatz.ContentType
import com.stehno.ersatz.Decoders
import com.stehno.ersatz.DecodingContext
import com.stehno.ersatz.Encoders
import com.stehno.ersatz.ErsatzServer
import grails.testing.services.ServiceUnitTest
import prd.organisation.domain.User
import spock.lang.Specification

import static com.stehno.ersatz.ContentType.APPLICATION_JSON

class UsersServiceSpec extends Specification implements ServiceUnitTest<UsersService> {

    void "test that the correct request is sent by the service when createUser is called"() {
        given:
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        String[] roles = ['cinnamon', 'gender', 'royce']
        def postedUser = new User(firstName, lastName, email, roles)
        ErsatzServer ersatz = new ErsatzServer()
        ersatz.expectations {
            post('/user') {
                header('accept', 'application/json')
                decoder(APPLICATION_JSON) { byte[] bytes, DecodingContext dc ->
                    Decoders.parseJson.apply(bytes, dc) as User
                }
                body(postedUser, 'application/json; charset=utf-8')
                called(1)
                responder {
                    code(201)
                    encoder APPLICATION_JSON, User, Encoders.json
                    // TODO: Is this actually the right response?
                    content(postedUser, ContentType.APPLICATION_JSON)
                }
            }
        }
        def mockConfigService = Mock(ConfigurationService)
        mockConfigService.getPrdUsersRestEndpointURL() >> ersatz.httpUrl
        service.configurationService = mockConfigService

        when:
        service.createUser(firstName, lastName, email, roles)

        then:
        ersatz.verify()

        cleanup:
        ersatz.stop()
    }

    // TODO: Test error case(s)
}