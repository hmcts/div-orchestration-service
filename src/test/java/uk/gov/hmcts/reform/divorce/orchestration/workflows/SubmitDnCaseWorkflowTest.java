package uk.gov.hmcts.reform.divorce.orchestration.workflows;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.WorkflowException;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.task.Task;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.FormatDivorceSessionToDnCaseDataTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.GetCaseWithIdTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.SubmitDnCase;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.AUTH_TOKEN;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.AUTH_TOKEN_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.CASE_ID_JSON_KEY;

@RunWith(MockitoJUnitRunner.class)
public class SubmitDnCaseWorkflowTest {

    @Mock
    private GetCaseWithIdTask getCaseWithIdTask;

    @Mock
    private FormatDivorceSessionToDnCaseDataTask formatDivorceSessionToDnCaseDataTask;

    @Mock
    private SubmitDnCase submitDnCase;

    @InjectMocks
    private SubmitDnCaseWorkflow classUnderTest;

    @SuppressWarnings("unchecked")
    @Test
    public void whenSubmitDnCase_thenProcessAsExpected() throws WorkflowException {
        final Map<String, Object> inputData = mock(Map.class);
        final Map<String, Object> expectedOutput = mock(Map.class);

        final ImmutablePair<String, Object> authTokenPair = new ImmutablePair<>(AUTH_TOKEN_JSON_KEY, AUTH_TOKEN);
        final ImmutablePair<String, Object> caseIdPair = new ImmutablePair<>(CASE_ID_JSON_KEY, TEST_CASE_ID);

        final Task[] tasks = new Task[]{
            formatDivorceSessionToDnCaseDataTask,
            submitDnCase
        };

        when(classUnderTest.execute(tasks, inputData, authTokenPair, caseIdPair)).thenReturn(expectedOutput);

        assertEquals(expectedOutput, classUnderTest.run(inputData, AUTH_TOKEN, TEST_CASE_ID));
    }
}