package com.veloMTL.veloMTL.Service.BMSCore;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.veloMTL.veloMTL.DTO.Helper.LoyaltyTierDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.Repository.BMSCore.ReservationRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;

@Service
public class TierService {
    private final TripRepository tripRepository;
    private final OperatorRepository operatorRepository;
    private final ReservationRepository reservationRepository;
    private final RiderRepository riderRepository;

    public TierService(TripRepository tripRepository, ReservationRepository reservationRepository,
                       RiderRepository riderRepository, OperatorRepository operatorRepository) {
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
    }

    public LoyaltyTier resolveTier(String userEmail) {
        // can do chain of responsibility pattern
        List<Trip> allTrips = tripRepository.fetchTripsByUserId(userEmail);
        if (!verifyBronzeTier(userEmail, allTrips)) return LoyaltyTier.ENTRY;
        if (!verifySilverTier(userEmail, allTrips)) return LoyaltyTier.BRONZE;
        if (!verifyGoldTier(userEmail, allTrips)) return LoyaltyTier.SILVER;
        return LoyaltyTier.GOLD;
    }

    public LoyaltyTierDTO checkTierChange(String userEmail) {
        LoyaltyTier tier = resolveTier(userEmail);
        User user = riderRepository.findByEmail(userEmail).orElse(null);
        if (user == null)
            user = operatorRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User does not exist with email: " + userEmail));
        LoyaltyTier userTier = user.getTier();
        saveUserTier(user, tier);
        return new LoyaltyTierDTO(tier, userTier, tier != userTier);
    }


    public void saveUserTier(User user, LoyaltyTier tier) {
        user.setTier(tier);
        if (user instanceof Rider) riderRepository.save((Rider) user);
        if (user instanceof Operator) operatorRepository.save((Operator) user);
    }

    private boolean verifyBronzeTier(String userEmail, List<Trip> allTrips) {
        LocalDateTime oneYearAgo = LocalDateTime.now().plusYears(-1);

        // Rider has a missed reservation in last year
        if (allTrips.stream().filter((trip) -> trip.getReserveStart() != null && trip.getReserveStart().isAfter(oneYearAgo)).anyMatch(Trip::isReservationExpired)) return false;

        // Rider successfully returned all bikes they ever took. (no ongoing trips lasting > 3 days)
        if (allTrips.stream().anyMatch((trip) -> trip.getStartTime() != null && trip.getEndTime() == null && trip.getStartTime().isAfter(trip.getStartTime().plusDays(3)))) return false;

        // Rider completed >= 10 trips in the last year
        if (allTrips.stream().filter((trip) -> trip.getStartTime() != null && trip.getEndTime() != null && trip.getStartTime().isAfter(oneYearAgo)).count() < 10) return false;

        return true;
    }

    private boolean verifySilverTier(String userEmail, List<Trip> allTrips) {
        LocalDateTime oneYearAgo = LocalDateTime.now().plusYears(-1);
        LocalDateTime threeMonthsAgo = LocalDateTime.now().plusMonths(-3);

        // Rider completed at least 5 reservations in the last year
        if (allTrips.stream().filter((trip) -> trip.getReserveStart() != null && trip.getReserveEnd() != null && trip.getReserveStart().isAfter(oneYearAgo)).count() < 5) return false;

        // Rider surpassed 5 trips per month in the last 3 months
        List<Trip> threeMonthsTrips = allTrips.stream().filter((trip) -> trip.getStartTime() != null && trip.getEndTime() != null && trip.getStartTime().isAfter(threeMonthsAgo)).toList();
        return hasMinTripsEachMonthPastCompleteMonths(threeMonthsTrips, 3, 5);
    }

    private boolean verifyGoldTier(String userEmail, List<Trip> allTrips) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().plusMonths(-3);

        // Rider surpassed 5 trips per week in the last 3 months
        List<Trip> threeMonthsTrips = allTrips.stream().filter((trip) -> trip.getStartTime() != null && trip.getEndTime() != null && trip.getStartTime().isAfter(threeMonthsAgo)).toList();
        return hasMinTripsEachFullWeekPastMonths(threeMonthsTrips, 3, 5);
    }

    /**
     * Checks if for the past `monthsBack` COMPLETE past months (excluding current month),
     * each month has at least `minTrips` trips starting in that month.
     */
    private boolean hasMinTripsEachMonthPastCompleteMonths(
            List<Trip> trips, int monthsBack, int minTrips) {

        YearMonth currentMonth = YearMonth.now();          // e.g., 2025-02
        YearMonth lastCompleteMonth = currentMonth.minusMonths(1);
        // e.g., if today is Feb, last complete month = Jan

        // The target months are the last N *completed* months
        List<YearMonth> targetMonths = new ArrayList<>();
        for (int i = 0; i < monthsBack; i++) {
            targetMonths.add(lastCompleteMonth.minusMonths(i));
        }

        // Group trips by YearMonth(startTime)
        Map<YearMonth, Long> tripsPerMonth = trips.stream()
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getStartTime()),
                        Collectors.counting()
                ));

        // Verify each full month has at least the required number of trips
        return targetMonths.stream()
                .allMatch(m -> tripsPerMonth.getOrDefault(m, 0L) >= minTrips);
    }

    /**
     * Checks if for the past `monthsBack` months, every full ISO week
     * has at least `minTrips` trips starting in that week.
     */
    private boolean hasMinTripsEachFullWeekPastMonths(
            List<Trip> trips, int monthsBack, int minTrips) {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(monthsBack);

        // Compute first full week start (Monday)
        LocalDate firstFullWeekStart = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        if (startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            firstFullWeekStart = startDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        // Compute last full week end (Sunday)
        LocalDate lastFullWeekEnd = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        if (today.getDayOfWeek() != DayOfWeek.SUNDAY) {
            lastFullWeekEnd = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        }

        // If the range contains no full weeks, return false by rule definition
        if (firstFullWeekStart.isAfter(lastFullWeekEnd)) {
            return false;
        }

        // Build list of full week identifiers (year-week)
        List<String> fullWeeks = new ArrayList<>();
        LocalDate d = firstFullWeekStart;

        while (!d.isAfter(lastFullWeekEnd)) {
            int year = d.get(IsoFields.WEEK_BASED_YEAR);
            int week = d.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            fullWeeks.add(year + "-W" + week);
            d = d.plusWeeks(1);
        }

        // Count trips per ISO week only within the full-week window
        LocalDate firstFullWeekStartFinal = firstFullWeekStart;
        LocalDate lastFullWeekEndFinal = lastFullWeekEnd;
        Map<String, Long> tripsPerWeek = trips.stream()
                .map(Trip::getStartTime)
                .map(LocalDateTime::toLocalDate)
                .filter(date -> !date.isBefore(firstFullWeekStartFinal) && !date.isAfter(lastFullWeekEndFinal))
                .collect(Collectors.groupingBy(date -> {
                    int year = date.get(IsoFields.WEEK_BASED_YEAR);
                    int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    return year + "-W" + week;
                }, Collectors.counting()));

        // Verify each full week meets the required minimum
        return fullWeeks.stream()
                .allMatch(week -> tripsPerWeek.getOrDefault(week, 0L) >= minTrips);
    }

    public double calculateTierDiscount(LoyaltyTier tier) {
        if (tier == null) return 0.0;
        return ((double) tier.getDiscount()) / 100;
    }
}

