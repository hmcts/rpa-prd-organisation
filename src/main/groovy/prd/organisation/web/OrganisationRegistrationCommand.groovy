package prd.organisation.web

class OrganisationRegistrationCommand {
    String name
    String url
    String sarId
    
    String primaryUserFirstName
    String primaryUserLastName
    String primaryUserEmail

    static constraints = {
        sarId nullable: true
        url nullable: true
        primaryUserFirstName nullable: true
        primaryUserLastName nullable: true
        primaryUserEmail nullable: true
    }
}