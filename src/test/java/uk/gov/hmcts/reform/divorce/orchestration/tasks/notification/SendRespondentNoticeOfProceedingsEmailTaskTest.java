package uk.gov.hmcts.reform.divorce.orchestration.tasks.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.LanguagePreference;
import uk.gov.hmcts.reform.divorce.orchestration.domain.model.email.EmailTemplateNames;
import uk.gov.hmcts.reform.divorce.orchestration.framework.workflow.task.TaskException;
import uk.gov.hmcts.reform.divorce.orchestration.service.EmailService;
import uk.gov.hmcts.reform.divorce.orchestration.service.bulk.print.dataextractor.AddresseeDataExtractorTest;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_PETITIONER_FIRST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_PETITIONER_LAST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_RESPONDENT_FIRST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_RESPONDENT_LAST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.TestConstants.TEST_SOLICITOR_EMAIL;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.CcdFields.RESPONDENT_SOLICITOR_ORGANISATION_POLICY;
import static uk.gov.hmcts.reform.divorce.orchestration.domain.model.email.EmailTemplateNames.SOL_RESPONDENT_NOTICE_OF_PROCEEDINGS;
import static uk.gov.hmcts.reform.divorce.orchestration.service.bulk.print.dataextractor.EmailDataExtractor.CaseDataKeys.RESPONDENT_SOLICITOR_EMAIL;
import static uk.gov.hmcts.reform.divorce.orchestration.service.bulk.print.dataextractor.FullNamesDataExtractor.CaseDataKeys.PETITIONER_FIRST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.service.bulk.print.dataextractor.FullNamesDataExtractor.CaseDataKeys.PETITIONER_LAST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.service.bulk.print.dataextractor.FullNamesDataExtractor.CaseDataKeys.RESPONDENT_FIRST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.service.bulk.print.dataextractor.FullNamesDataExtractor.CaseDataKeys.RESPONDENT_LAST_NAME;
import static uk.gov.hmcts.reform.divorce.orchestration.testutil.CaseDataTestHelper.buildOrganisationPolicy;
import static uk.gov.hmcts.reform.divorce.orchestration.testutil.ServiceJourneyEmailTaskHelper.getExpectedRespondentSolicitorNotificationTemplateVars;
import static uk.gov.hmcts.reform.divorce.orchestration.testutil.ServiceJourneyEmailTaskHelper.getTaskContext;
import static uk.gov.hmcts.reform.divorce.orchestration.testutil.TaskContextHelper.context;

@RunWith(MockitoJUnitRunner.class)
public class SendRespondentNoticeOfProceedingsEmailTaskTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SendRespondentNoticeOfProceedingsEmailTask task;

    @Test
    public void shouldSendEmail_ToSolicitor_whenExecuteEmailNotificationTask() throws TaskException {
        Map<String, Object> caseData = buildCaseData();

        executeTask(caseData);

        verifySolicitorEmailSent(caseData);
    }

    @Test
    public void shouldReturnTemplate() {
        EmailTemplateNames returnedTemplate = task.getTemplate();

        assertEquals(SOL_RESPONDENT_NOTICE_OF_PROCEEDINGS, returnedTemplate);
    }

    private Map<String, Object> buildCaseData() {
        Map<String, Object> caseData = AddresseeDataExtractorTest.buildCaseDataWithRespondentSolicitor();

        caseData.put(PETITIONER_FIRST_NAME, TEST_PETITIONER_FIRST_NAME);
        caseData.put(PETITIONER_LAST_NAME, TEST_PETITIONER_LAST_NAME);
        caseData.put(RESPONDENT_FIRST_NAME, TEST_RESPONDENT_FIRST_NAME);
        caseData.put(RESPONDENT_LAST_NAME, TEST_RESPONDENT_LAST_NAME);

        caseData.put(RESPONDENT_SOLICITOR_EMAIL, TEST_SOLICITOR_EMAIL);
        caseData.put(RESPONDENT_SOLICITOR_ORGANISATION_POLICY, buildOrganisationPolicy());

        return caseData;
    }

    private void executeTask(Map<String, Object> caseData) {
        Map<String, Object> returnPayload = task.execute(getTaskContext(), caseData);
        assertEquals(caseData, returnPayload);
    }

    private void verifySolicitorEmailSent(Map<String, Object> caseData) {
        verify(emailService).sendEmail(
            TEST_SOLICITOR_EMAIL,
            SOL_RESPONDENT_NOTICE_OF_PROCEEDINGS.name(),
            getExpectedRespondentSolicitorNotificationTemplateVars(context(), caseData),
            "CaseId: test.case.id, email template SOL_RESPONDENT_NOTICE_OF_PROCEEDINGS",
            LanguagePreference.ENGLISH
        );
    }
}
