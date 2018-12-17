package rd.professional.web

import grails.rest.RestfulController
import io.swagger.annotations.*
import rd.professional.domain.ContactInformation
import rd.professional.domain.ProfessionalUser

@Api(
        value = "organisations/",
        description = "Contact Information APIs"
)
class ContactInformationController extends RestfulController<ContactInformation> {
    static responseFormats = ['json']

    ContactInformationController() {
        super(ContactInformation)
    }
    //ContactInformationService contactService

    @ApiOperation(
            value="List all contact details for an organisation",
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
    def listOrgContacts(Integer max) {
        // TODO
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
    def showOrgContact() {
        // TODO
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
    def deleteOrgContact() {
        // TODO
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
                    dataType = "rd.professional.web.ContactInformationCommand"
            )
    ])
    def addOrgContact(ContactInformationCommand cmd) {
        // TODO
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
                    dataType = "rd.professional.web.ContactInformationCommand"
            )
    ])
    def updateOrgContact(ContactInformationCommand cmd) {
        // TODO
    }

    @ApiOperation(
            value="List all contact details for a professional user",
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
    def listUserContacts(Integer max) {
        // TODO
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
    def showUserContact() {
        // TODO
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
    def deleteUserContact() {
        // TODO
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
                    dataType = "rd.professional.web.ContactInformationCommand"
            )
    ])
    def addUserContact(ContactInformationCommand cmd) {
        // TODO
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
                    dataType = "rd.professional.web.ContactInformationCommand"
            )
    ])
    def updateUserContact(ContactInformationCommand cmd) {
        // TODO
    }
}
