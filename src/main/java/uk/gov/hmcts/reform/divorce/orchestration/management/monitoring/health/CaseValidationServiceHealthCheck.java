package uk.gov.hmcts.reform.divorce.orchestration.management.monitoring.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CaseValidationServiceHealthCheck extends WebServiceHealthCheck {
    @Autowired
    public CaseValidationServiceHealthCheck(HttpEntityFactory httpEntityFactory, RestTemplate restTemplate,
                                            @Value("${case.validation.service.api.baseurl}/health") String uri) {
        super(httpEntityFactory, restTemplate, uri);
    }
}