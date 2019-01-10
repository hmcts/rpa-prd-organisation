package rd.professional.web


import grails.gorm.transactions.Transactional
import io.swagger.annotations.*
import rd.professional.domain.ContactInformation
import rd.professional.service.ContactInformationService
import rd.professional.service.UsersService
import rd.professional.web.command.ContactInformationCommand
import rd.professional.web.dto.UserAddressDto

import static org.springframework.http.HttpStatus.CREATED

@Api(
        value = "organisations/",
        description = "Contact Information APIs"
)
class UserContactInformationController extends AbstractDtoRenderingController<ContactInformation, UserAddressDto> {
    static responseFormats = ['json']

    UserContactInformationController() {
        super(ContactInformation, UserAddressDto)
    }

    ContactInformationService contactInformationService
    UsersService usersService

    @ApiOperation(
            value = "List all contact details for a professional user",
            nickname = "/{orgId}/users/{userId}/contacts",
            produces = "application/json",
            httpMethod = "GET",
            response = ContactInformation,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No contact information found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            )
    ])
    def index(Integer max) {
        super.index(max)
    }

    @ApiOperation(
            value = "Get specific contact information belonging to a professional user",
            nickname = "/{orgId}/users/{userId}/contacts/{id}",
            produces = "application/json",
            httpMethod = 'GET',
            response = ContactInformation
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Contact information not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "Contact information ID",
                    dataType = "string"
            )
    ])
    def show() {
        super.show()
    }

    @ApiOperation(
            value = "Delete a set of contact information belonging to a professional user",
            nickname = "/{orgId}/users/{userId}/contacts/{id}",
            httpMethod = 'DELETE'
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Contact information not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "Contact Information ID",
                    dataType = "string"
            )
    ])
    @Transactional
    def delete() {
        super.delete()
    }

    @ApiOperation(
            value = "Add contact information to a professional user",
            nickname = "/{orgId}/users/{userId}/contacts",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'POST'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 405, message = "Method not allowed"),
            @ApiResponse(code = 409, message = "Contact information already exists")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "Contact information details",
                    dataType = "rd.professional.web.command.ContactInformationCommand"
            )
    ])
    @Transactional
    def save() {
        def user = usersService.getForUuid(params.professionalUserId)
        if (!user) {
            notFound()
            return
        }

        def cmd = new ContactInformationCommand(request.getJSON())
        def contact = new ContactInformation(address: cmd.address)

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

    @ApiOperation(
            value = "Update contact information for a professional user",
            nickname = "/{orgId}/users/{userId}/contacts/{id}",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'PUT'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Contact information not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "Contact Information ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "Contact information details",
                    dataType = "rd.professional.web.command.ContactInformationCommand"
            )
    ])
    @Transactional
    def update() {
        super.update()
    }

    protected ContactInformation queryForResource(Serializable id) {
        contactInformationService.getContact(id)
    }

    protected List<ContactInformation> listAllResources(Map params) {
        contactInformationService.getContactsForUser(params.professionalUserId)
    }
}
