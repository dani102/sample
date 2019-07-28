import java.util.ArrayList;
import java.util.List;

import static ElectricCar.TravelType.NORMAL;
import static ElectricCar.TravelType.SPRINT;

public class ElectricTrip {

    private static final String TRIP_COMPONENTS_SEPARATOR = "-";
    private CityFactory cityFactory;

    private List<MiniTrip> miniTrips;
    private List<Trip> trips;

    public ElectricTrip(String tripAsString) {
        cityFactory = new CityFactory();
        trips = new ArrayList<>();

        generateTripData(tripAsString);
    }

    private void generateTripData(String trip) {
        String tripComponents[] = trip.split(TRIP_COMPONENTS_SEPARATOR);
        miniTrips = generateMiniTrips(tripComponents);
    }

    private List<MiniTrip> generateMiniTrips(String[] tripComponents) {
        List<MiniTrip> trips = new ArrayList<>();

        int numberOfComponents = tripComponents.length;

        for (int i = 0; i + 2 < numberOfComponents; i += 2) {
            City startingCity = cityFactory.create(tripComponents[i]);
            City endingCity = cityFactory.create(tripComponents[i + 2]);
            int distance = Integer.parseInt(tripComponents[i + 1]);

            trips.add(new MiniTrip(startingCity, endingCity, distance));
        }
        return trips;
    }

    public int startTripIn(String startingCityName, int batterySize, int lowSpeedPerformance, int highSpeedPerformance) {
        ElectricCar car = new ElectricCar(batterySize, lowSpeedPerformance, highSpeedPerformance);

        City startingCity = getStartingCity(startingCityName);

        Trip trip = new Trip(startingCity, car);

        trips.add(trip);

        return trips.lastIndexOf(trip);
    }

    private City getStartingCity(String startingCityName) {
        return miniTrips.stream()
                .filter(m -> startingCityName.equals(m.getStartingCityName()))
                .findFirst()
                .map(MiniTrip::getStartingCity)
                .orElseThrow(RuntimeException::new);
    }

    public void go(int participantId) {
        Trip currentTrip = trips.get(participantId);

        currentTrip.start(miniTrips, NORMAL);
    }

    public String locationOf(int participantId) {
        Trip trip = trips.get(participantId);

        return trip.getCurrentLocationName();
    }

    public String chargeOf(int participantId) {
        return trips.get(participantId).getBatteryPercentage();
    }

    public void sprint(int participantId) {
        Trip currentTrip = trips.get(participantId);

        currentTrip.start(miniTrips, SPRINT);
    }

    public void charge(int participantId, int hoursOfCharge) {
        Trip trip = trips.get(participantId);

        trip.chargeCar(hoursOfCharge);
    }
}
