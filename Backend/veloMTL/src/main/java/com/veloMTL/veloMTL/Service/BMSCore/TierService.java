package com.veloMTL.veloMTL.Service.BMSCore;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import com.veloMTL.veloMTL.DTO.Helper.LoyaltyTierDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;

@Service
public class TierService {
    private final TripRepository tripRepository;
    private final OperatorRepository operatorRepository;
    private final RiderRepository riderRepository;

    public TierService(TripRepository tripRepository,
                       RiderRepository riderRepository, OperatorRepository operatorRepository) {
        this.tripRepository = tripRepository;
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

    public LoyaltyTier fetchUserTier(String userEmail) {
        User user = riderRepository.findByEmail(userEmail).orElse(null);
        if (user == null)
            user = operatorRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User does not exist with email: " + userEmail));
        return user.getTier();
    }

    /**
     * Returns the current tier without checking for changes or updating.
     * Use this for getting tier information without triggering notifications.
     * For tier change checks, use checkTierChange() which is only called on login.
     */
    public LoyaltyTierDTO getCurrentTier(String userEmail) {
        User user = riderRepository.findByEmail(userEmail).orElse(null);
        if (user == null)
            user = operatorRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User does not exist with email: " + userEmail));
        LoyaltyTier currentTier = user.getTier();
        // Return DTO with isTierChanged = false since we're not checking for changes
        return new LoyaltyTierDTO(currentTier, currentTier, false);
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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneYearAgo = LocalDateTime.now().plusYears(-1);
        LocalDateTime threeMonthsAgo = LocalDateTime.now().plusMonths(-3);

        // Rider completed at least 5 reservations in the last year
        if (allTrips.stream().filter((trip) -> trip.getReserveStart() != null && trip.getReserveEnd() != null && trip.getReserveStart().isAfter(oneYearAgo)).count() < 5) return false;

        // Rider surpassed 5 trips per month in the last 3 months
        LocalDateTime current = LocalDateTime.of(now.getYear(), now.getMonthValue(), 1, 0, 0);
        for (int i = 0; i < 3; i++) {
            LocalDateTime next = current.minusMonths(1);
            LocalDateTime currentFinal = current;
            if (allTrips.stream().filter((trip) -> trip.getStartTime() != null && trip.getEndTime() != null && trip.getStartTime().isAfter(next) && trip.getStartTime().isBefore(currentFinal)).count() < 5) return false;
            current = next;
        }

        return true;
    }

    private boolean verifyGoldTier(String userEmail, List<Trip> allTrips) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeMonthsAgo = now.plusMonths(-3);

        // Rider surpassed 5 trips per week in the last 3 months
        LocalDateTime current = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        while(current.minusWeeks(1).isAfter(threeMonthsAgo)) {
            LocalDateTime next = current.minusWeeks(1);
            LocalDateTime currentFinal = current;
            if (allTrips.stream().filter((trip) -> trip.getStartTime() != null && trip.getEndTime() != null && trip.getStartTime().isAfter(next) && trip.getStartTime().isBefore(currentFinal)).count() < 5) return false;
            current = next;
        }

        return true;
    }

    public double calculateTierDiscount(LoyaltyTier tier) {
        if (tier == null) return 0.0;
        return ((double) tier.getDiscount()) / 100;
    }
}

