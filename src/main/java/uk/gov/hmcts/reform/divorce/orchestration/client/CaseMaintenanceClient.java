package uk.gov.hmcts.reform.divorce.orchestration.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.ccd.CaseDetails;

import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "maintenance-service-client", url = "${case.maintenance.service.api.baseurl}")
public interface CaseMaintenanceClient {

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/casemaintenance/version/1/submit",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    Map<String, Object> submitCase(
        @RequestBody Map<String, Object> submitCase,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken
    );

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/casemaintenance/version/1/updateCase/{caseId}/{eventId}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    Map<String, Object> updateCase(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
        @PathVariable("caseId") String caseId,
        @PathVariable("eventId") String eventId,
        @RequestBody Map<String, Object> updateCase
    );

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/casemaintenance/version/1/retrieveAosCase",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    CaseDetails retrieveAosCase(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
        @RequestParam(value = "checkCcd") boolean checkCcd
    );
}