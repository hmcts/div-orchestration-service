package uk.gov.hmcts.reform.divorce.orchestration.workflows.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.task.TaskContext;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.DnSubmittedEmailNotificationTask;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_CASE_ID;

@RunWith(MockitoJUnitRunner.class)
public class DnSubmittedEmailNotificationWorkflowTest {

    @Mock
    private DnSubmittedEmailNotificationTask dnSubmittedEmailNotificationTask;

    @InjectMocks
    private DnSubmittedEmailNotificationWorkflow dnSubmittedEmailNotificationWorkflow;

    @Test
    public void sendDnSubmittedEmailTaskIsExecuted() throws Exception {
        Map<String, Object> casePayload = Collections.emptyMap();

        when(dnSubmittedEmailNotificationTask.execute(isNotNull(), eq(casePayload))).thenReturn(casePayload);

        dnSubmittedEmailNotificationWorkflow.run(TEST_CASE_ID, casePayload);

        verify(dnSubmittedEmailNotificationTask).execute(any(TaskContext.class), eq(casePayload));
    }
}