package rd.professional.domain

import io.swagger.annotations.ApiModelProperty

class Domain {

    @ApiModelProperty(hidden = true)
    Long id
    UUID domainId = UUID.randomUUID()

    String host

    @ApiModelProperty(hidden = true)
    Organisation organisation

    static belongsTo = [organisation: Organisation]

    static constraints = {
        host nullable: false
        domainId nullable: false, unique: true
    }

    static mapping = {
        id generator: 'identity'
        domainId type: 'uuid-binary', length: 16
    }
}
