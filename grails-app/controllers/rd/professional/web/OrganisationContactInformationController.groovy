package rd.professional.web


import io.swagger.annotations.*
import rd.professional.domain.ContactInformation
import rd.professional.service.ContactInformationService
import rd.professional.service.OrganisationService
import rd.professional.web.command.ContactInformationCommand
import rd.professional.web.dto.OrganisationAddressDto

import static org.springframework.http.HttpStatus.CREATED

@Api(
        value = "organisations/",
        description = "Contact Information APIs"
)
class OrganisationContactInformationController extends AbstractDtoRenderingController<ContactInformation, OrganisationAddressDto> {
    static responseFormats = ['json']

    OrganisationContactInformationController() {
        super(ContactInformation, OrganisationAddressDto)
    }
    OrganisationService organisationService
    ContactInformationService contactInformationService

    @ApiOperation(
            value = "List all contact details for an organisation",
            nickname = "/{orgId}/contacts",
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
            )
    ])
    def index(Integer max) {
        super.index(max)
    }

    @ApiOperation(
            value = "Get specific contact information belonging to an organisation",
            nickname = "/{orgId}/contacts/{id}",
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
            value = "Delete a set of contact information belonging to an organisation",
            nickname = "/{orgId}/contacts/{id}",
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
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "Contact Information ID",
                    dataType = "string"
            )
    ])
    def delete() {
        super.delete()
    }

    @ApiOperation(
            value = "Add contact information to an organisation",
            nickname = "/{orgId}/contacts",
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
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "Contact information details",
                    dataType = "rd.professional.web.command.ContactInformationCommand"
            )
    ])
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

    @ApiOperation(
            value = "Update contact information for an organisation",
            nickname = "/{orgId}/contacts/{id}",
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
    def update() {
        super.update()
    }

    protected ContactInformation queryForResource(Serializable id) {
        contactInformationService.getContact(id)
    }

    protected List<ContactInformation> listAllResources(Map params) {
        contactInformationService.getContactsForOrg(params.organisationId)
    }
}
