package rd.professional.web

import grails.gorm.transactions.Transactional
import rd.professional.domain.Domain
import rd.professional.service.OrganisationService
import rd.professional.web.command.AddDomainCommand

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NO_CONTENT

class DomainController extends AbstractExceptionHandlerController<Domain> {
    static responseFormats = ['json']

    DomainController() {
        super(Domain)
    }
    OrganisationService organisationService

    def index(Integer max) {
        super.index(max)
    }

    @Transactional
    def delete() {
        def organisationId = params.organisationId
        def domain = params.id
        def instance = Domain.where {
            organisation.organisationId == organisationId
            host == domain
        }.find()
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        deleteResource instance

        render status: NO_CONTENT
    }

    @Transactional
    def save() {
        def organisation = organisationService.getForUuid(params.organisationId)
        if (!organisation) {
            notFound()
            return
        }

        def cmd = new AddDomainCommand(request.getJSON())
        def domain = new Domain(host: cmd.domain)

        organisation.addToDomains(domain)

        domain.validate()
        if (domain.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond domain.errors, status: 400
            return
        }

        saveResource organisation

        respond domain, [status: CREATED]
    }

    protected Domain queryForResource(Serializable id) {
        Domain.where {
            domainId == id
        }.find()
    }

    protected List<Domain> listAllResources(Map params) {
        Domain.where {
            organisation.organisationId == params.organisationId
        }.findAll()
    }
}
