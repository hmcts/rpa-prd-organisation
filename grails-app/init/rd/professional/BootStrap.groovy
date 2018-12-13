package rd.professional

import grails.converters.JSON
import rd.professional.domain.ContactInformation
import rd.professional.domain.Domain
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(Organisation, {
            return [
                    name:it.name,
                    url:it.url,
                    sraId:it.sraId,
                    organisationId:it.organisationId,
                    lastUpdated:it.lastUpdated,
                    companyNumber:it.companyNumber,
                    status:it.status,
                    contacts:it.contacts,
                    accounts:it.accounts,
                    domains:it.domains,
                    contacts:it.contacts
            ]
        })
        JSON.registerObjectMarshaller(ProfessionalUser, {
            return [
                    userId:it.userId,
                    emailId:it.emailId,
                    firstName:it.firstName,
                    lastName:it.lastName,
                    status:it.status
            ]
        })
        JSON.registerObjectMarshaller(ContactInformation, {
            return [
                    address:it.address,
                    dateCreated:it.dateCreated,
                    lastUpdated:it.lastUpdated,
                    contactId:it.contactId
            ]
        })
        JSON.registerObjectMarshaller(PaymentAccount, {
            return [
                    paymentAccountId:it.paymentAccountId,
                    pbaNumber:it.pbaNumber
            ]
        })
        JSON.registerObjectMarshaller(Domain, {
            return [
                    domainId:it.domainId,
                    host:it.host,
            ]
        })
    }
    def destroy = {
    }
}
