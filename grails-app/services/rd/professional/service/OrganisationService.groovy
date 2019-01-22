package rd.professional.service

import grails.gorm.transactions.Transactional
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import rd.professional.domain.*
import rd.professional.exception.HttpException
import rd.professional.web.command.OrganisationRegistrationCommand

@Transactional
class OrganisationService {

    MessageSource messageSource

    Organisation registerOrganisation(OrganisationRegistrationCommand cmd) {
        def url = cmd.url
        if (url && !url.startsWith("http"))
            url = "https://$url"
        def organisation = new Organisation(name: cmd.name, url: url, sraId: cmd.sraId)

        if (cmd.pbaAccounts) {
            log.debug "Registering PBAs for organisation"
            cmd.pbaAccounts.each { account ->
                if (account.pbaNumber) organisation.addToAccounts(new PaymentAccount(pbaNumber: account.pbaNumber))
            }
        }

        log.debug "Creating superuser"
        if (!cmd.superUser)
            throw new HttpException(HttpStatus.BAD_REQUEST.value(), "Super user must be set")
        ProfessionalUser superuser = new ProfessionalUser(emailId: cmd.superUser.email, firstName: cmd.superUser.firstName, lastName: cmd.superUser.lastName)
        if (cmd.superUser.pbaAccount) {
            log.debug "Registering PBA for superuser"
            superuser.addToAccounts(organisation.accounts.find { it.pbaNumber == cmd.superUser.pbaAccount.pbaNumber })
        }
        if (cmd.superUser.address && cmd.superUser.address.address) {
            log.debug "Registering address for superuser"
            superuser.addToContacts(
                    new ContactInformation(
                            address: cmd.superUser.address.address
                    ))
        }
        organisation.addToUsers(superuser)

        //TODO: Ensure user emails match the organisations domains
        if (cmd.domains) {
            log.debug "Registering domains for organisation"
            cmd.domains.each { domain ->
                if (domain.domain) organisation.addToDomains(new Domain(host: domain.domain))
            }
        }
        if (!organisation.domains || organisation.domains.size() == 0) {
            log.debug "No domain in request. Using super user's email domain"
            String host = superuser.emailId.substring(superuser.emailId.indexOf('@') + 1)
            organisation.addToDomains(new Domain(host: host))
        }

        // address
        if (cmd.address && cmd.address.address) {
            log.debug "Registering address for organisation"
            organisation.addToContacts(
                    new ContactInformation(
                            address: cmd.address.address
                    ))
        }

        if (cmd.dxAddress) {
            log.debug "Adding DX address to organisation"
            organisation.setDxAddress(new DxAddress(dxNumber: cmd.dxAddress.dxNumber, dxExchange: cmd.dxAddress.dxExchange))
        }

        if (organisation.validate()) {
            log.debug "Saving organisation"
            organisation.save(failOnError: true, flush: true)
        } else {
            organisation.delete(flush: true)
            log.error "Unable to validate organisation: " + organisation.errors.allErrors.collect { FieldError e -> messageSource.getMessage(e, Locale.getDefault()) }
            throw new RuntimeException(organisation.errors.allErrors.collect { FieldError e -> messageSource.getMessage(e, Locale.getDefault()) }.join('<p>'))
        }
    }

    def getForUuid(Serializable uuid) {
        if (!uuid || uuid == "null")
            return null
        Organisation.where {
            organisationId == uuid
        }.find()
    }
}
