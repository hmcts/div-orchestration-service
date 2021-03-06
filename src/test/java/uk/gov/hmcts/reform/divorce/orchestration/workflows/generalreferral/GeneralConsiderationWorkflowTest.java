package uk.gov.hmcts.reform.divorce.orchestration.workflows.generalreferral;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.generalreferral.GeneralReferralDataTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.generalreferral.GeneralReferralDecisionDateTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.generalreferral.GeneralReferralFieldsRemovalTask;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.reform.divorce.orchestration.testutil.Verificators.mockTasksExecution;
import static uk.gov.hmcts.reform.divorce.orchestration.testutil.Verificators.verifyTasksCalledInOrder;

@RunWith(MockitoJUnitRunner.class)
public class GeneralConsiderationWorkflowTest extends TestCase {

    @Mock
    private GeneralReferralDecisionDateTask generalReferralDecisionDateTask;

    @Mock
    private GeneralReferralDataTask generalReferralDataTask;

    @Mock
    private GeneralReferralFieldsRemovalTask generalReferralFieldsRemovalTask;

    @InjectMocks
    private GeneralConsiderationWorkflow generalConsiderationWorkflow;

    @Test
    public void runShouldBeExecuted() throws Exception {
        Map<String, Object> caseData = new HashMap<>();
        mockTasksExecution(
            caseData,
            generalReferralDecisionDateTask,
            generalReferralDataTask,
            generalReferralFieldsRemovalTask);

        generalConsiderationWorkflow.run(CaseDetails.builder().caseData(caseData).build());

        verifyTasksCalledInOrder(
            caseData,
            generalReferralDecisionDateTask,
            generalReferralDataTask,
            generalReferralFieldsRemovalTask);
    }
}
