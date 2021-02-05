package uk.gov.hmcts.reform.divorce.orchestration.workflows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.AddMiniPetitionDraftTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.AddNewDocumentsToCaseDataTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.SetClaimCostsFromTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.SetSolicitorCourtDetailsTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.SetSolicitorOrganisationPolicyDetailsTask;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.AUTH_TOKEN;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.DIVORCE_COSTS_CLAIM_CCD_FIELD;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.YES_VALUE;
import static uk.gov.hmcts.reform.divorce.orchestration.testutil.Verificators.mockTasksExecution;
import static uk.gov.hmcts.reform.divorce.orchestration.testutil.Verificators.verifyTasksCalledInOrder;

@RunWith(MockitoJUnitRunner.class)
public class SolicitorCreateWorkflowTest {

    @Mock
    AddMiniPetitionDraftTask addMiniPetitionDraftTask;

    @Mock
    AddNewDocumentsToCaseDataTask addNewDocumentsToCaseDataTask;

    @Mock
    SetSolicitorCourtDetailsTask setSolicitorCourtDetailsTask;

    @Mock
    SetClaimCostsFromTask setClaimCostsFromTask;

    @Mock
    SetSolicitorOrganisationPolicyDetailsTask setSolicitorOrganisationPolicyDetailsTask;

    @InjectMocks
    SolicitorCreateWorkflow solicitorCreateWorkflow;

    @Test
    public void runShouldSetClaimCostsFromWhenClaimCostsIsYesAndClaimCostsFromIsEmpty() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put(DIVORCE_COSTS_CLAIM_CCD_FIELD, YES_VALUE);

        CaseDetails caseDetails = CaseDetails.builder().caseData(payload).build();

        mockTasksExecution(
            caseDetails.getCaseData(),
            setClaimCostsFromTask,
            setSolicitorCourtDetailsTask,
            addMiniPetitionDraftTask,
            addNewDocumentsToCaseDataTask,
            setSolicitorOrganisationPolicyDetailsTask
        );

        assertThat(solicitorCreateWorkflow.run(caseDetails, AUTH_TOKEN), is(caseDetails.getCaseData()));

        verifyTasksCalledInOrder(
            caseDetails.getCaseData(),
            setClaimCostsFromTask,
            setSolicitorCourtDetailsTask,
            addMiniPetitionDraftTask,
            addNewDocumentsToCaseDataTask,
            setSolicitorOrganisationPolicyDetailsTask
        );
    }
}
