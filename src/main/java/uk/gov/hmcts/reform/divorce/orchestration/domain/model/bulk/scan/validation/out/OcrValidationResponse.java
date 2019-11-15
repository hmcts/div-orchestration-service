package uk.gov.hmcts.reform.divorce.orchestration.domain.model.bulk.scan.validation.out;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class OcrValidationResponse {

    @JsonProperty("warnings")
    private final List<String> warnings;

    @JsonProperty("errors")
    private final List<String> errors;

    @JsonProperty("status")
    private final ValidationStatus status;

    @JsonCreator
    public OcrValidationResponse(
        List<String> warnings,
        List<String> errors,
        ValidationStatus status
    ) {
        this.warnings = warnings;
        this.errors = errors;
        this.status = status;
    }

}