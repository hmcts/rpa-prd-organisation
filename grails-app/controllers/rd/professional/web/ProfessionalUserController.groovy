package rd.professional.web

import grails.gorm.transactions.Transactional
import rd.professional.domain.ContactInformation
import rd.professional.domain.ProfessionalUser
import rd.professional.service.OrganisationService
import rd.professional.service.UsersService
import rd.professional.web.command.UserRegistrationCommand
import rd.professional.web.dto.ProfessionalUserDto

import static org.springframework.http.HttpStatus.CREATED

class ProfessionalUserController extends AbstractDtoRenderingController<ProfessionalUser, ProfessionalUserDto> {
    static responseFormats = ['json']

    ProfessionalUserController() {
        super(ProfessionalUser, ProfessionalUserDto)
    }

    OrganisationService organisationService
    UsersService usersService

    @Transactional
    def save() {
        def organisation = organisationService.getForUuid(params.organisationId)
        if (!organisation) {
            notFound()
            return
        }

        def cmd = new UserRegistrationCommand(request.getJSON())
        def user = new ProfessionalUser(
                firstName: cmd.firstName,
                lastName: cmd.lastName,
                emailId: cmd.email
        )
        if (cmd.address) {
            user.addToContacts(new ContactInformation(
                    address: cmd.address.address
            ))
        }

        organisation.addToUsers(user)

        if (cmd.pbaAccount) {
            usersService.setPbaAccount(user, cmd.pbaAccount.pbaNumber)
        }

        user.validate()
        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, status: 400
            return
        }

        saveResource organisation

        respond new ProfessionalUserDto(user), [status: CREATED]
    }

    protected ProfessionalUser queryForResource(Serializable id) {
        ProfessionalUser.where {
            userId == id
        }.find()
    }

    protected List<ProfessionalUser> listAllResources(Map params) {
        ProfessionalUser.where {
            organisation.organisationId == params.organisationId
        }.findAll()
    }
}
