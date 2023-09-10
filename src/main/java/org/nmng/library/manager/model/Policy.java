package org.nmng.library.manager.model;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

public final class Policy {
    public static double FINE_PER_INTERVAL = 1000;
    public static Duration FINE_COUNTING_INTERVAL = Duration.of(21600, ChronoUnit.SECONDS);

}
