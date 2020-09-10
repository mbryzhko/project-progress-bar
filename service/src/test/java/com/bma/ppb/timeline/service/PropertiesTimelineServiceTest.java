package com.bma.ppb.timeline.service;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = { "ppb.reportingPeriod.startQuarter=2020-05" })
class PropertiesTimelineServiceIntegrationTest {

    @Autowired
    private PropertiesTimelineService service;

    @Test
    public void startOfQuarterIsReadFromProperties() {
        ReportingPeriodBean period = service.getCurrentReportingPeriod();

        assertEquals(YearMonth.of(2020, 5), period.getFirstMonth());
        assertEquals(YearMonth.of(2020, 7), period.getLastMonth());
    }

    @Test
    public void currentDateIsTodayByDefault() {
        LocalDate expectedCurrentDate = LocalDate.now();

        ReportingPeriodBean period = service.getCurrentReportingPeriod();

        assertEquals(expectedCurrentDate, period.getToday());
    }

}