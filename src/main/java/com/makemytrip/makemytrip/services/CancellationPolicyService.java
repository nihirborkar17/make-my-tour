package com.makemytrip.makemytrip.services;

import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class CancellationPolicyService {

    public double calculateRefundPercentage(LocalDateTime journeyDate, LocalDateTime cancellationTime) {
        Duration duration = Duration.between(cancellationTime, journeyDate);
        long hoursBeforeJourney = duration.toHours();

        if (hoursBeforeJourney >= 48) return 0.9;    // 90% refund
        else if (hoursBeforeJourney >= 24) return 0.75; // 75% refund
        else return 0.5;   // 50% refund if <24 hours before journey
    }
}
