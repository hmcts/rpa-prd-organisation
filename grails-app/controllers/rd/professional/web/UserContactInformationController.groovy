package rd.professional.web

import grails.gorm.transactions.Transactional
import rd.professional.domain.ContactInformation
import rd.professional.service.ContactInformationService
import rd.professional.service.UsersService
import rd.professional.web.command.ContactInformationCommand
import rd.professional.web.dto.UserAddressDto

import static org.springframework.http.HttpStatus.CREATED

class UserContactInformationController extends AbstractDtoRenderingController<ContactInformation, UserAddressDto> {
    static responseFormats = ['json']

    UserContactInformationController() {
        super(ContactInformation, UserAddressDto)
    }

    ContactInformationService contactInformationService
    UsersService usersService

    @Transactional
    def save() {
        def user = usersService.getForUuid(params.professionalUserId)
        if (!user) {
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

        user.addToContacts(contact)

        contact.validate()
        if (contact.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond contact.errors, status: 400
            return
        }

        saveResource user

        respond new UserAddressDto(contact), [status: CREATED]
    }

    protected ContactInformation queryForResource(Serializable id) {
        contactInformationService.getContact(id)
    }

    protected List<ContactInformation> listAllResources(Map params) {
        contactInformationService.getContactsForUser(params.professionalUserId)
    }
}
