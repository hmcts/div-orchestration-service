package uk.gov.hmcts.reform.divorce.orchestration.workflows;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.Features;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.DefaultWorkflow;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.WorkflowException;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.task.Task;
import uk.gov.hmcts.reform.divorce.orchestration.service.FeatureToggleService;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.AddMiniPetitionDraftTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.AddNewDocumentsToCaseDataTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.SendSolicitorApplicationSubmittedEmailTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.SetPetitionerSolicitorOrganisationPolicyReferenceTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.AUTH_TOKEN_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.CASE_DETAILS_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.CASE_ID_JSON_KEY;

@Component
@Slf4j
@RequiredArgsConstructor
public class SolicitorUpdateWorkflow extends DefaultWorkflow<Map<String, Object>> {

    private final AddMiniPetitionDraftTask addMiniPetitionDraftTask;
    private final AddNewDocumentsToCaseDataTask addNewDocumentsToCaseDataTask;
    private final SendSolicitorApplicationSubmittedEmailTask sendSolicitorApplicationSubmittedEmailTask;
    private final SetPetitionerSolicitorOrganisationPolicyReferenceTask setPetitionerSolicitorOrganisationPolicyReferenceDetailTask;
    private final FeatureToggleService featureToggleService;

    public Map<String, Object> run(CaseDetails caseDetails, final String authToken) throws WorkflowException {
        String caseId = caseDetails.getCaseId();

        log.info("CaseID: {} SolicitorUpdateWorkflow workflow is going to be executed.", caseId);

        return this.execute(
            getTasks(caseId),
            caseDetails.getCaseData(),
            ImmutablePair.of(AUTH_TOKEN_JSON_KEY, authToken),
            ImmutablePair.of(CASE_DETAILS_JSON_KEY, caseDetails),
            ImmutablePair.of(CASE_ID_JSON_KEY, caseDetails.getCaseId())
        );
    }

    private Task<Map<String, Object>>[] getTasks(String caseId) {
        List<Task<Map<String, Object>>> tasks = new ArrayList<>();

        tasks.add(getAddMiniPetitionDraftTask(caseId));
        tasks.add(getAddNewDocumentsToCaseDataTask(caseId));
        tasks.add(getSolicitorApplicationSubmittedEmailTask(caseId));

        if (featureToggleService.isFeatureEnabled(Features.REPRESENTED_RESPONDENT_JOURNEY)) {
            tasks.add(getSolicitorOrganisationPolicyDetailsTask(caseId));
        }

        return tasks.toArray(new Task[] {});
    }

    private Task<Map<String, Object>> getSolicitorApplicationSubmittedEmailTask(String caseId) {
        log.info("CaseId: {} Adding task to send Application Submitted email to Petitioner solicitor.", caseId);
        return sendSolicitorApplicationSubmittedEmailTask;
    }

    private Task<Map<String, Object>> getAddNewDocumentsToCaseDataTask(String caseId) {
        log.info("CaseId: {} Adding task to Add new documents to case data.", caseId);
        return addNewDocumentsToCaseDataTask;
    }

    private Task<Map<String, Object>> getAddMiniPetitionDraftTask(String caseId) {
        log.info("CaseId: {} Adding task to Add Mini Petition Draft.", caseId);
        return addMiniPetitionDraftTask;
    }

    private Task<Map<String, Object>> getSolicitorOrganisationPolicyDetailsTask(String caseId) {
        log.info("CaseId: {} Feature Toggle {} is enabled. Adding petitioner solicitor organisation policy reference details task.",
            caseId, Features.REPRESENTED_RESPONDENT_JOURNEY);
        return setPetitionerSolicitorOrganisationPolicyReferenceDetailTask;
    }
}
