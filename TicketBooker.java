import java.util.*;

public class TicketBooker {
    static int availableLowerBerths = 1; // normally 21
    static int availableMiddleBerths = 1; // normally 21
    static int availableUpperBerths = 1; // normally 21
    static int availableRacTickets = 1; // normally 18
    static int availableWaitingList = 1; // normally 10

    static Queue<Integer> waitingList = new LinkedList<>(); // queue of WL passengers
    static Queue<Integer> racList = new LinkedList<>(); // queue of RAC passengers
    static List<Integer> bookedTicketList = new ArrayList<>(); // list of booked ticket passengers

    static List<Integer> lowerBerthsPositions = new ArrayList<>(Arrays.asList(1)); // normally 1,2,...21
    static List<Integer> middleBerthsPositions = new ArrayList<>(Arrays.asList(1)); // normally 1,2,...21
    static List<Integer> upperBerthsPositions = new ArrayList<>(Arrays.asList(1)); // normally 1,2,...21
    static List<Integer> racPositions = new ArrayList<>(Arrays.asList(1)); // normally 1,2,...18
    static List<Integer> waitingListPositions = new ArrayList<>(Arrays.asList(1)); // normally 1,2,...10

    static Map<Integer, Passenger> passengers = new HashMap<>(); // map of passenger ids to passengers

    // Book ticket
    public void bookTicket(Passenger p, int berthInfo, String allottedBerth) {
        p.number = berthInfo;
        p.alloted = allottedBerth;
        passengers.put(p.passengerId, p);
        bookedTicketList.add(p.passengerId);
        System.out.println("--------------------------Booked Successfully");
    }

    // Adding to RAC
    public void addToRAC(Passenger p, int racInfo, String allottedRAC) {
        p.number = racInfo;
        p.alloted = allottedRAC;
        passengers.put(p.passengerId, p);
        racList.add(p.passengerId);
        availableRacTickets--;
        racPositions.remove(0);
        bookedTicketList.add(p.passengerId);
        System.out.println("--------------------------added to RAC Successfully");
    }

    // Adding to WL
    public void addToWaitingList(Passenger p, int waitingListInfo, String allottedWL) {
        p.number = waitingListInfo;
        p.alloted = allottedWL;
        passengers.put(p.passengerId, p);
        waitingList.add(p.passengerId);
        availableWaitingList--;
        waitingListPositions.remove(0);
        bookedTicketList.add(p.passengerId);
        System.out.println("-------------------------- added to Waiting List Successfully");
    }

    // Cancel ticket
    public void cancelTicket(int passengerId) {
        Passenger p = passengers.get(passengerId);
        if (p == null) {
            System.out.println("Passenger ID not found");
            return;
        }

        passengers.remove(passengerId);
        bookedTicketList.remove(Integer.valueOf(passengerId));
        int positionBooked = p.number;

        if (p.alloted.equals("L")) {
            availableLowerBerths++;
            lowerBerthsPositions.add(positionBooked);
        } else if (p.alloted.equals("M")) {
            availableMiddleBerths++;
            middleBerthsPositions.add(positionBooked);
        } else if (p.alloted.equals("U")) {
            availableUpperBerths++;
            upperBerthsPositions.add(positionBooked);
        } else if (p.alloted.equals("RAC")) {
            availableRacTickets++;
            racPositions.add(positionBooked);
            racList.remove(Integer.valueOf(passengerId));

            if (waitingList.size() > 0) {
                Passenger passengerFromWaitingList = passengers.get(waitingList.poll());
                int positionWL = passengerFromWaitingList.number;
                waitingListPositions.add(positionWL);
                waitingList.remove(Integer.valueOf(passengerFromWaitingList.passengerId));

                passengerFromWaitingList.number = racPositions.get(0);
                passengerFromWaitingList.alloted = "RAC";
                racPositions.remove(0);
                racList.add(passengerFromWaitingList.passengerId);

                availableWaitingList++;
                availableRacTickets--;
            }
        } else if (p.alloted.equals("WL")) {
            availableWaitingList++;
            waitingListPositions.add(positionBooked);
            waitingList.remove(Integer.valueOf(passengerId));
        }

        if (racList.size() > 0 && (p.alloted.equals("L") || p.alloted.equals("M") || p.alloted.equals("U"))) {
            Passenger passengerFromRAC = passengers.get(racList.poll());
            int positionRac = passengerFromRAC.number;
            racPositions.add(positionRac);
            racList.remove(Integer.valueOf(passengerFromRAC.passengerId));
            availableRacTickets++;
            Main.bookTicket(passengerFromRAC);
        }

        System.out.println("---------------cancelled Successfully");
    }

    // Print all available seats
    public void printAvailable() {
        System.out.println("Available Lower Berths " + availableLowerBerths);
        System.out.println("Available Middle Berths " + availableMiddleBerths);
        System.out.println("Available Upper Berths " + availableUpperBerths);
        System.out.println("Available RACs " + availableRacTickets);
        System.out.println("Available Waiting List " + availableWaitingList);
        System.out.println("--------------------------");
    }

    // Print all occupied passengers from all types including WL
    public void printPassengers() {
        if (passengers.size() == 0) {
            System.out.println("No details of passengers");
            return;
        }
        for (Passenger p : passengers.values()) {
            System.out.println("PASSENGER ID " + p.passengerId);
            System.out.println("Name " + p.name);
            System.out.println("Age " + p.age);
            System.out.println("Status " + p.number + p.alloted);
            System.out.println("--------------------------");
        }
    }
}
