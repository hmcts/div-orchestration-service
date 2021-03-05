package uk.gov.hmcts.reform.divorce.orchestration.workflows;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.DefaultWorkflow;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.WorkflowException;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.task.Task;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.AddCourtsToPayloadTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.CreateAmendPetitionDraftTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.UpdateCaseInCCD;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.CcdEvents.AMEND_PETITION;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.AUTH_TOKEN_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.CASE_EVENT_ID_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.CASE_ID_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.NEW_AMENDED_PETITION_DRAFT_KEY;

@Component
@RequiredArgsConstructor
public class AmendPetitionWorkflow extends DefaultWorkflow<Map<String, Object>> {

    private final CreateAmendPetitionDraftTask amendPetitionDraft;
    private final UpdateCaseInCCD updateCaseInCCD;
    private final AddCourtsToPayloadTask addCourtsToPayloadTask;

    public Map<String, Object> run(String caseId, String authToken) throws WorkflowException {
        execute(
            new Task[] {
                amendPetitionDraft,
                updateCaseInCCD
            },
            new HashMap<>(),
            ImmutablePair.of(CASE_ID_JSON_KEY, caseId),
            ImmutablePair.of(AUTH_TOKEN_JSON_KEY, authToken),
            ImmutablePair.of(CASE_EVENT_ID_JSON_KEY, AMEND_PETITION)
        );

        Map<String, Object> newDraft = getContext().getTransientObject(NEW_AMENDED_PETITION_DRAFT_KEY);

        return execute(new Task[] {addCourtsToPayloadTask}, newDraft);
    }
}