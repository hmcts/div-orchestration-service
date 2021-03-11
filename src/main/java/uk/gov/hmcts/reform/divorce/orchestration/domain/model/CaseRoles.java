package uk.gov.hmcts.reform.divorce.orchestration.domain.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaseRoles {

    public static final String CREATOR = "[CREATOR]";
    public static final String PETITIONER_SOLICITOR = "[PETSOLICITOR]";
    public static final String RESPONDENT_SOLICITOR = "[RESPSOLICITOR]";

}
