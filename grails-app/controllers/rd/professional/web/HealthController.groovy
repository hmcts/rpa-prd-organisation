package rd.professional.web

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

@Api(
        value = "health",
        description = "Health and status related endpoints"
)
class HealthController {
    static responseFormats = ['json']

    @ApiOperation(
            value = "Check whether the service is responding to requests",
            httpMethod = "GET",
            produces = "application/json",
            nickname = "/"
    )
    def index() {
        render '{"status": "UP"}'
    }
}
