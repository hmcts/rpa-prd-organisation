package rd.professional.service

import com.stehno.ersatz.*
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import rd.professional.domain.User
import rd.professional.exception.HttpException
import spock.lang.Specification

import static com.stehno.ersatz.ContentType.APPLICATION_JSON

class UsersServiceSpec extends Specification implements ServiceUnitTest<UsersService>, DataTest {

    def setup() {
        mockDomains(Organisation, ProfessionalUser, PaymentAccount)
    }

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

    void "test setPbaAccount Org has account, user does not (happy path)"() {
        given:
        PaymentAccount account = new PaymentAccount(pbaNumber: "123456")
        ProfessionalUser user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        Organisation org = new Organisation(name: "QuxNet")
        org.addToAccounts(account)
        org.addToUsers(user)
        org.save(flush: true)

        when:
        service.setPbaAccount(user, "123456")

        then:
        notThrown(HttpException)
    }

    void "test setPbaAccount Account does not exist"() {
        given:
        ProfessionalUser user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        Organisation org = new Organisation(name: "QuxNet")
        org.addToUsers(user)
        org.save(flush: true)

        when:
        service.setPbaAccount(user, "123456")

        then:
        thrown(HttpException)
    }

    void "test setPbaAccount Neither org nor user has account"() {
        given:
        PaymentAccount account = new PaymentAccount(pbaNumber: "123456")
        ProfessionalUser user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        Organisation org = new Organisation(name: "QuxNet")
        org.addToUsers(user)
        org.save(flush: true)

        when:
        service.setPbaAccount(user, "123456")

        then:
        thrown(HttpException)
    }

    void "test setPbaAccount Org has different account, user has none"() {
        given:
        PaymentAccount account = new PaymentAccount(pbaNumber: "123456")
        account.save(flush: true)
        PaymentAccount account2 = new PaymentAccount(pbaNumber: "654321")
        ProfessionalUser user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        Organisation org = new Organisation(name: "QuxNet")
        org.addToAccounts(account2)
        org.addToUsers(user)
        org.save(flush: true)

        when:
        service.setPbaAccount(user, "123456")

        then:
        thrown(HttpException)
    }

    void "test getForUUID UUID not found"() {
        given:
        UUID uuid = UUID.randomUUID()

        when:
        def user = service.getForUuid(uuid)

        then:
        user == null
    }

    void "test getForUUID, User found"() {
        given:
        ProfessionalUser user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        Organisation org = new Organisation(name: "QuxNet")
        org.addToUsers(user)
        org.save(flush: true)

        when:
        def foundUser = service.getForUuid(user.userId)

        then:
        user.firstName == foundUser.firstName
        user.emailId == foundUser.emailId
    }
}