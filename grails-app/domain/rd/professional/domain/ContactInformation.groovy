package rd.professional.domain

import io.swagger.annotations.ApiModelProperty

import java.time.LocalDateTime

class ContactInformation {

    @ApiModelProperty(hidden = true)
    Long id
    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode
    LocalDateTime dateCreated
    LocalDateTime lastUpdated
    UUID contactId = UUID.randomUUID()

    @ApiModelProperty(hidden = true)
    Organisation organisation
    @ApiModelProperty(hidden = true)
    ProfessionalUser user

    static belongsTo = [
            organisation: Organisation,
            user        : ProfessionalUser
    ]
    static constraints = {
        houseNoBuildingName nullable: false
        addressLine1 nullable: true
        addressLine2 nullable: true
        townCity nullable: false
        county nullable: true
        country nullable: true
        postcode nullable: false
        contactId nullable: false, unique: true
        organisation nullable: true
        user nullable: true
    }

    static mapping = {
        id generator: 'identity'
        contactId type: 'uuid-binary', length: 16
    }
}
