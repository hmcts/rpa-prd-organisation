package rd.professional.service

import grails.gorm.transactions.Transactional
import org.springframework.context.MessageSource
import org.springframework.validation.FieldError
import rd.professional.domain.Address
import rd.professional.domain.Domain
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import rd.professional.web.OrganisationRegistrationCommand

@Transactional
class OrganisationService {

    MessageSource messageSource

    void registerOrganisation(OrganisationRegistrationCommand cmd) {
        def organisation = new Organisation(name: cmd.name, url: cmd.url, sraId: cmd.sraId)

        log.debug "Creating superuser"
        def superuser = new ProfessionalUser(emailId: cmd.email, firstName: cmd.firstName, lastName: cmd.lastName)
        organisation.addToUsers(superuser)

        if (cmd.pbaAccounts) {
            log.debug "Registering PBAs for organisation"
            cmd.pbaAccounts.split(',').each { account ->
                organisation.addToAccounts(new PaymentAccount(pbaNumber: account))
            }
        }

        if (cmd.domains) {
            log.debug "Registering domains for organisation"
            cmd.domains.split(',').each { domain ->
                organisation.addToDomains(new Domain(host: domain))
            }
        }

        // address
        if (cmd.houseNoBuildingName && cmd.addressLine1 && cmd.townCity) {
            log.debug "Registering address for organisation"
            organisation.addToAddresses(
                    new Address(
                            houseNoBuildingName: cmd.houseNoBuildingName,
                            addressLine1: cmd.addressLine1,
                            addressLine2: cmd.addressLine2,
                            townCity: cmd.townCity,
                            county: cmd.county,
                            country: cmd.country,
                            postcode: cmd.postcode
                    ))
        }

        if (organisation.validate()) {
            log.debug "Saving organisation"
            organisation.save(failOnError: true, flush: true)
        } else {
            log.debug "Unable to validate organisation: " + organisation.errors.allErrors.collect { FieldError e -> messageSource.getMessage(e, Locale.getDefault()) }
            throw new RuntimeException(organisation.errors.allErrors.collect { FieldError e -> messageSource.getMessage(e, Locale.getDefault()) }.join('<p>'))
        }
    }
}