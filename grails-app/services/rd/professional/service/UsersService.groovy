package rd.professional.service

import groovyx.net.http.ChainedHttpConfig
import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder
import groovyx.net.http.NativeHandlers
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import rd.professional.domain.User
import rd.professional.exception.HttpException
import rd.professional.web.command.AddAccountCommand

import static groovyx.net.http.ContentTypes.JSON
import static groovyx.net.http.HttpBuilder.configure
import static groovyx.net.http.NativeHandlers.Parsers.json
import static org.springframework.http.HttpStatus.NOT_FOUND

class UsersService {

    private final HttpBuilder http
    def configurationService

    UsersService() {
        http = configure {
            request.contentType = JSON[0]
            request.encoder JSON, NativeHandlers.Encoders.&json
            response.parser JSON, { ChainedHttpConfig config, FromServer fs ->
                json(config, fs) as User
            }
        }
    }

    def createUser(String firstName, String lastName, String email, String[] roles) {
        // TODO: Enforce presence of mandatory parameters and test
        return http.post(User) {
            request.uri = configurationService.getPrdUsersRestEndpointURL()
            request.uri.path = '/user'
            request.body = new User(firstName, lastName, email, roles)
            request.contentType = 'application/json'
            request.accept = 'application/json'
        }
    }

    def getForUuid(Serializable uuid) {
        ProfessionalUser.where {
            userId == uuid
        }.find()
    }

    def findUserAccountsByEmail(String email) {
        return ProfessionalUser.where {
            eq('emailId', email)
        }.find()
    }

    /**
     * Sets a user's PBA account number
     *
     * @param user
     * @param pbaNumber
     * @return true if successful, false if the account was not found or does not belong to the user's organisation
     */
    def setPbaAccount(ProfessionalUser user, String pbaNumber) {
        def orgAccounts = user.organisation.accounts

        def account = PaymentAccount.findByPbaNumber(pbaNumber)

        if (account && account.organisation == user.organisation) {
            user.addToAccounts(account)
        } else {
            throw new HttpException(NOT_FOUND, "PBA account ${pbaNumber} not found")
        }
    }
}
