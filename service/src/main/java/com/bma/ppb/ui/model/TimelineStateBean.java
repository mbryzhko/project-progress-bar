package com.bma.ppb.ui.model;

import com.bma.ppb.ui.view.TimelineRenderer;
import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;

/**
 * Represents State of Timeline.
 *
 * @see TimelineRenderer
 */
@Data
@Builder
public class TimelineStateBean {

    private YearMonth firstMonth;
    private YearMonth secondMonth;
    private YearMonth thirdMonth;
    private double quarterProgressPercent;

}
