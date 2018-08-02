package uk.gov.hmcts.reform.divorce.orchestration.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrchestrationServiceTest {

    @Autowired
    private OrchestrationService orchestrationService;

    @Before
    public void setup() {
        assertNotNull(orchestrationService);
    }

    @Test
    public void givenCoreCaseData_whenOrchestrate_then() {
        assertTrue(true);
    }

}