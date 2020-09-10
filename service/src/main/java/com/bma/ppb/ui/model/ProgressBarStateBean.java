package com.bma.ppb.ui.model;

import com.bma.ppb.ui.view.RegularProjectRenderer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * Represents state of the project's progress bar.
 *
 * @see RegularProjectRenderer
 */
@Data
@Builder
public class ProgressBarStateBean {

    private final float leftSpacePercent;
    private final float progressBarSpacePercent;
    private final double progress;
    private final ProgressBarColor color;

    public float getRightSpacePercent() {
        return 100 - leftSpacePercent - progressBarSpacePercent;
    }

    @AllArgsConstructor
    public enum ProgressBarColor {
        GREEN("project-progress-green"),
        RED("project-progress-red"),
        YELLOW("project-progress-yellow");

        @Getter
        private String cssClass;
    }
}
