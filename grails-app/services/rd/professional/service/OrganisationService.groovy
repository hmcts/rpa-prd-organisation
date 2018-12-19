package rd.professional.service

import grails.gorm.transactions.Transactional
import org.springframework.context.MessageSource
import org.springframework.validation.FieldError
import rd.professional.domain.*
import rd.professional.web.OrganisationRegistrationCommand

@Transactional
class OrganisationService {

    MessageSource messageSource

    def registerOrganisation(OrganisationRegistrationCommand cmd) {
        def organisation = new Organisation(name: cmd.name, url: cmd.url, sraId: cmd.sraId)

        log.debug "Creating superuser"
        def superuser = new ProfessionalUser(emailId: cmd.superUser.email, firstName: cmd.superUser.firstName, lastName: cmd.superUser.lastName)
        organisation.addToUsers(superuser)

        if (cmd.pbaAccounts) {
            log.debug "Registering PBAs for organisation"
            cmd.pbaAccounts.each { account ->
                organisation.addToAccounts(new PaymentAccount(pbaNumber: account.pbaNumber))
            }
        }

        if (cmd.domains) {
            log.debug "Registering domains for organisation"
            cmd.domains.each { domain ->
                organisation.addToDomains(new Domain(host: domain.domain))
            }
        }

        // address
        if (cmd.address) {
            log.debug "Registering address for organisation"
            organisation.addToContacts(
                    new ContactInformation(
                            address: cmd.address.address
                    ))
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
        Organisation.where {
            organisationId == uuid
        }.find()
    }
}
