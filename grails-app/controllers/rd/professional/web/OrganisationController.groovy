package rd.professional.web

import grails.gorm.transactions.Transactional
import rd.professional.domain.*
import rd.professional.service.ContactInformationService
import rd.professional.service.OrganisationService
import rd.professional.web.command.AddAccountCommand
import rd.professional.web.command.AddDomainCommand
import rd.professional.web.command.OrganisationRegistrationCommand
import rd.professional.web.command.OrganisationUpdateCommand
import rd.professional.web.dto.OrganisationDto

import static org.springframework.http.HttpStatus.*

class OrganisationController extends AbstractDtoRenderingController<Organisation, OrganisationDto> {
    static responseFormats = ['json']

    OrganisationController() {
        super(Organisation, OrganisationDto)
    }

    ContactInformationService contactInformationService
    OrganisationService organisationService

    @Transactional
    def save() {
        log.info "Creating organisation"
        def cmd = new OrganisationRegistrationCommand(request.getJSON())
        Organisation organisation = organisationService.registerOrganisation(cmd)
        respond new OrganisationDto(organisation), [status: CREATED]
    }

    @Transactional
    def update(OrganisationUpdateCommand cmd) {
        Organisation organisation = queryForResource(params.id)
        if (organisation == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (cmd.address) {
            if (!contactInformationService.doesAddressExist(cmd.address)) {
                organisation.addToContacts(new ContactInformation(cmd.address))
            }
        }
        if (cmd.dxAddress) {
            if (!DxAddress.find {
                dxNumber == cmd.dxAddress.dxNumber && dxExchange == cmd.dxAddress.dxExchange
            })
                organisation.dxAddress = new DxAddress(cmd.dxAddress)
        }
        if (cmd.domains) {
            for (AddDomainCommand domainCommand : cmd.domains) {
                if (!Domain.findByHost(domainCommand.domain))
                    organisation.addToDomains(new Domain(domainCommand))
            }
        }
        if (cmd.pbaAccounts) {
            for (AddAccountCommand accountCommand : cmd.pbaAccounts) {
                if (!PaymentAccount.findByPbaNumber(accountCommand.pbaNumber))
                    organisation.addToAccounts(new PaymentAccount(accountCommand))
            }
        }
        if (cmd.status)
            organisation.setStatus(Status.valueOf(cmd.status))
        if (cmd.sraId)
            organisation.setSraId(cmd.sraId)
        if (cmd.name)
            organisation.setName(cmd.name)

        organisation.validate()
        if (organisation.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond organisation.errors, [status: UNPROCESSABLE_ENTITY] // STATUS CODE 422
            return
        }

        organisation.save flush: true

        respond new OrganisationDto(organisation), [status: OK]
    }

    protected Organisation queryForResource(Serializable id) {
        organisationService.getForUuid(id)
    }
}
