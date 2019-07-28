import static ElectricCar.TravelType.NORMAL;

public class ElectricCar {
    enum TravelType {SPRINT, NORMAL}

    private int batterySize;
    private double currentBatteryCharge;
    private int lowSpeedPerformance;
    private int highSpeedPerformance;

    public ElectricCar(int batterySize, int lowSpeedPerformance, int highSpeedPerformance) {
        this.batterySize = batterySize;
        this.lowSpeedPerformance = lowSpeedPerformance;
        this.highSpeedPerformance = highSpeedPerformance;

        currentBatteryCharge = batterySize;
    }

    public String computeBatteryPercentage() {
        double batteryPercentage = currentBatteryCharge * 100.0 / batterySize;

        return Math.round(batteryPercentage) + "%";
    }

    public void drive(int numberOfKm, TravelType travelType) {
        int speedPerformance = getSpeedPerformance(travelType);

        currentBatteryCharge = currentBatteryCharge - ((double) numberOfKm / speedPerformance);
    }

    public boolean hasEnoughChargeFor(int distanceToNextStop, TravelType travelType) {
        int speedPerformance = getSpeedPerformance(travelType);

        return (currentBatteryCharge - (distanceToNextStop / speedPerformance)) > 0;
    }

    private int getSpeedPerformance(TravelType travelType) {
        return travelType == NORMAL ? lowSpeedPerformance : highSpeedPerformance;
    }

    public boolean shouldChargeNow(TravelType travelType, City currentLocation, int distanceToNextChargingSpot) {
        return currentLocation.hasChargingStation() &&
                !hasEnoughChargeFor(distanceToNextChargingSpot, travelType) &&
                currentBatteryCharge < batterySize;
    }

    public void charge(int hoursOfCharge, int currentLocationChargingPower) {
        int amountCharged = hoursOfCharge * currentLocationChargingPower;

        if (currentBatteryCharge + amountCharged > batterySize) {
            currentBatteryCharge = batterySize;
        } else {
            currentBatteryCharge += amountCharged;
        }
    }

    public boolean shouldKeepDriving(TravelType travelType, MiniTrip miniTrip, int distanceToNextChargingSpot) {
        boolean hasEnoughChargeToKeepDriving =
                hasEnoughChargeFor(distanceToNextChargingSpot, travelType) ||
                        hasEnoughChargeFor(miniTrip.getDistance(), travelType);

        return !shouldChargeNow(travelType, miniTrip.getStartingCity(), distanceToNextChargingSpot) && hasEnoughChargeToKeepDriving;
    }
}
