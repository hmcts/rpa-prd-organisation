package rd.professional.config

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.stereotype.Component
import uk.gov.hmcts.reform.logging.appinsights.AbstractAppInsights

@Component
class AppInsights extends AbstractAppInsights {
    AppInsights(TelemetryClient telemetry) {
        super(telemetry)
    }
}
