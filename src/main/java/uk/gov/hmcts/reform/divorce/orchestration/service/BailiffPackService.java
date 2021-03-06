package uk.gov.hmcts.reform.divorce.orchestration.service;

import uk.gov.hmcts.reform.divorce.orchestration.domain.model.ccd.CaseDetails;

import java.util.Map;

public interface BailiffPackService {

    Map<String, Object> issueCertificateOfServiceDocument(String authorizationToken, CaseDetails caseDetails)
        throws CaseOrchestrationServiceException;
}
