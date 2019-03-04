package rd.professional.web


import rd.professional.domain.ContactInformation
import rd.professional.service.ContactInformationService
import rd.professional.service.OrganisationService
import rd.professional.web.command.ContactInformationCommand
import rd.professional.web.dto.OrganisationAddressDto

import static org.springframework.http.HttpStatus.CREATED

class OrganisationContactInformationController extends AbstractDtoRenderingController<ContactInformation, OrganisationAddressDto> {
    static responseFormats = ['json']

    OrganisationContactInformationController() {
        super(ContactInformation, OrganisationAddressDto)
    }
    OrganisationService organisationService
    ContactInformationService contactInformationService

    def save() {
        def organisation = organisationService.getForUuid(params.organisationId)
        if (!organisation) {
            notFound()
            return
        }

        def address = new ContactInformationCommand(request.getJSON())
        def contact = new ContactInformation(
                houseNoBuildingName: address.houseNoBuildingName,
                addressLine1: address.addressLine1,
                addressLine2: address.addressLine2,
                townCity: address.townCity,
                county: address.county,
                country: address.country,
                postcode: address.postcode
        )

        organisation.addToContacts(contact)

        contact.validate()
        if (contact.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond contact.errors, status: 400
            return
        }

        saveResource organisation

        respond new OrganisationAddressDto(contact), [status: CREATED]
    }

    protected ContactInformation queryForResource(Serializable id) {
        contactInformationService.getContact(id)
    }

    protected List<ContactInformation> listAllResources(Map params) {
        contactInformationService.getContactsForOrg(params.organisationId)
    }
}
