package com.bma.ppb.timeline.service;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Properties based implementation of {@link TimelineService}.
 */
@Service
public class PropertiesTimelineService implements TimelineService {

    @Setter @Getter
    @Value("${ppb.reportingPeriod.startQuarter}")
    private YearMonth startQuarter;

    @Override
    public ReportingPeriodBean getCurrentReportingPeriod() {
        return ReportingPeriodBean.builder()
                .firstMonth(startQuarter)
                .today(LocalDate.now()).build();
    }

}
