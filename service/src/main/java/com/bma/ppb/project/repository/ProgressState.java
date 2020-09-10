package com.bma.ppb.project.repository;

import com.bma.ppb.ui.model.ProgressBarCalc;

/**
 * Defines current state of the project.
 *
 * @see ProgressBarCalc
 */
public enum ProgressState {
    NOT_STARTED,
    ON_TRACK,
    BEHIND_SCHEDULE,
    AT_RISK,
    COMPLETED;
}
