package rd.professional.web

import grails.converters.JSON
import grails.gorm.transactions.Transactional

import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

abstract class AbstractDtoRenderingController<T, U> extends AbstractExceptionHandlerController<T> {
    static responseFormats = ['json', 'xml']

    Class<U> dtoType

    AbstractDtoRenderingController(Class<T> resource, Class<U> dto) {
        super(resource)
        this.dtoType = dto
    }

    def index(Integer max) {
        respond listAllResources(params).collect { dtoType.newInstance(it) }
    }

    def show() {
        T instance = queryForResource(params.id)
        if (instance == null) {
            notFound()
            return
        }
        respond dtoType.newInstance(instance), [status: OK]
    }

    @Transactional
    def update() {
        T instance = queryForResource(params.id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        instance.properties = getObjectToBind()

        instance.validate()
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view:'edit' // STATUS CODE 422
            return
        }

        instance.save flush: true
        respond dtoType.newInstance(instance), [status: OK]
    }
}
