package rd.professional.web

import grails.gorm.transactions.Transactional
import grails.rest.RestfulController
import org.springframework.http.HttpStatus
import rd.professional.exception.HttpException

abstract class AbstractExceptionHandlerController<T> extends RestfulController<T> {
    static responseFormats = ['json']

    AbstractExceptionHandlerController(Class<T> resource) {
        super(resource)
    }

    def connectException(final ConnectException exception) {
        logException exception
        render view: 'error', model: [exception: exception]
    }

    @Transactional
    def httpException(final HttpException exception) {
        transactionStatus.setRollbackOnly()
        respond exception.getMessage(), status: exception.statusCode
    }

    @Transactional
    def runtimeExeption(final RuntimeException exception) {
        transactionStatus.setRollbackOnly()
        logException(exception)
        respond exception.getMessage(), status: HttpStatus.INTERNAL_SERVER_ERROR
    }


    /** Log exception */
    private void logException(final Exception exception) {
        log.error "Exception occurred. ${exception?.message}", exception
    }
}
