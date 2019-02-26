package rd.professional.web.command

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
class ContactInformationCommand {

    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode
}
