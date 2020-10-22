package uk.gov.hmcts.reform.divorce.orchestration.workflows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.ccd.CcdCallbackRequest;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.WorkflowException;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.task.DefaultTaskContext;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.task.TaskException;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.AddNewDocumentsToCaseDataTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.DocumentGenerationTask;
import uk.gov.hmcts.reform.divorce.orchestration.tasks.PersonalServiceValidationTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_TOKEN;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.CcdStates.ISSUED;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.AUTH_TOKEN_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.CASE_DETAILS_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.CASE_ID_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.DOCUMENT_FILENAME;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.DOCUMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.DOCUMENT_TYPE;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.PERSONAL_SERVICE_VALUE;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.SOLICITOR_PERSONAL_SERVICE_LETTER_DOCUMENT_TYPE;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.OrchestrationConstants.SOLICITOR_PERSONAL_SERVICE_LETTER_FILENAME;

@RunWith(MockitoJUnitRunner.class)
public class IssuePersonalServicePackWorkflowTest {

    @Mock
    PersonalServiceValidationTask personalServiceValidationTask;

    @Mock
    DocumentGenerationTask documentGenerationTask;

    @Mock
    AddNewDocumentsToCaseDataTask addNewDocumentsToCaseDataTask;

    @InjectMocks
    IssuePersonalServicePackWorkflow issuePersonalServicePackWorkflow;

    private static final String SOLICITOR_PERSONAL_SERVICE_LETTER_TEMPLATE_ID = "FL-DIV-GNO-ENG-00073.docx";

    DefaultTaskContext context;

    CaseDetails caseDetails;

    CcdCallbackRequest request;

    //given
    Map<String, Object> caseData;

    public void setupTest(String state, Map<String, Object> caseData) {
        caseDetails = CaseDetails.builder()
            .caseId(TEST_CASE_ID)
            .state(state)
            .caseData(caseData)
            .build();

        DefaultTaskContext context = new DefaultTaskContext();
        context.setTransientObjects(new HashMap<>() {
            {
                put(AUTH_TOKEN_JSON_KEY, TEST_TOKEN);
                put(CASE_ID_JSON_KEY, TEST_CASE_ID);
                put(CASE_DETAILS_JSON_KEY, caseDetails);
                put(DOCUMENT_TYPE, SOLICITOR_PERSONAL_SERVICE_LETTER_DOCUMENT_TYPE);
                put(DOCUMENT_TEMPLATE_ID, SOLICITOR_PERSONAL_SERVICE_LETTER_TEMPLATE_ID);
                put(DOCUMENT_FILENAME, SOLICITOR_PERSONAL_SERVICE_LETTER_FILENAME);
            }
        });

        request = CcdCallbackRequest.builder()
            .caseDetails(caseDetails)
            .build();

        //when
        when(personalServiceValidationTask.execute(context, caseData)).thenReturn(caseData);
        when(documentGenerationTask.execute(context, caseData)).thenReturn(caseData);
        when(addNewDocumentsToCaseDataTask.execute(context, caseData)).thenReturn(caseData);
    }

    @Test
    public void testRunExecutesExpectedTasksInOrderForCaseStateIssued() throws WorkflowException, TaskException {
        caseData = Collections.singletonMap("SolServiceMethod", PERSONAL_SERVICE_VALUE);
        setupTest(ISSUED, caseData);
        Map<String, Object> response = issuePersonalServicePackWorkflow.run(request, TEST_TOKEN);

        //then
        assertThat(response, is(caseData));
        InOrder inOrder = inOrder(
            personalServiceValidationTask,
            documentGenerationTask,
            addNewDocumentsToCaseDataTask
        );
        inOrder.verify(personalServiceValidationTask).execute(context, caseData);
        inOrder.verify(documentGenerationTask).execute(context, caseData);
        inOrder.verify(addNewDocumentsToCaseDataTask).execute(context, caseData);
    }
}
