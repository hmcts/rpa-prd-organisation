import grails.rest.render.xml.XmlRenderer
import rd.professional.domain.Organisation
import rd.professional.service.AccountsService

beans = {
    organisationRenderer(XmlRenderer, Organisation) {
        includes = ['primaryContactFirstName', 'primaryContactEmail']
    }
    accountsService(AccountsService) {
    }
    appInsights() {
    }
}