package uk.gov.hmcts.reform.divorce.orchestration.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.idam.AuthenticateUserResponse;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.idam.GeneratePinRequest;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.idam.GeneratePinResponse;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.idam.UserDetails;

@FeignClient(name = "idam-api", url = "${idam.api.url}")
public interface IdamApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/details")
    UserDetails retrieveUserDetails(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation);

    @RequestMapping(method = RequestMethod.POST, value = "/pin")
    GeneratePinResponse generatePin(
        GeneratePinRequest requestBody,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation
    );

    @RequestMapping(method = RequestMethod.POST, value = "/oauth2/authorize")
    AuthenticateUserResponse authenticateUser(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation);
}