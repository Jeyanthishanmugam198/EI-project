/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
import java.util.*;

// Singleton Pattern for Office Configuration
class Office {
    private static Office instance;
    private List<Room> rooms;

    private Office() {
        rooms = new ArrayList<>();
    }

    public static Office getInstance() {
        if (instance == null) {
            instance = new Office();
        }
        return instance;
    }

    public void configureRooms(int count) {
        rooms.clear();
        for (int i = 1; i <= count; i++) {
            rooms.add(new Room("Room " + i));
        }
        System.out.println("Office configured with " + count + " meeting rooms: " + rooms);
    }

    public Room getRoom(int roomNumber) {
        if (roomNumber > 0 && roomNumber <= rooms.size()) {
            return rooms.get(roomNumber - 1);
        }
        System.out.println("Invalid room number. Please enter a valid room number.");
        return null;
    }
}

// Room class
class Room {
    private String name;
    private int maxCapacity;
    public boolean occupied;
    private boolean booked;
    private List<Observer> observers;

    public Room(String name) {
        this.name = name;
        this.maxCapacity = 0;
        this.occupied = false;
        this.booked = false;
        this.observers = new ArrayList<>();
    }

    public void setMaxCapacity(int capacity) {
        if (capacity > 0) {
            this.maxCapacity = capacity;
            System.out.println(name + " maximum capacity set to " + capacity + ".");
        } else {
            System.out.println("Invalid capacity. Please enter a valid positive number.");
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public void setOccupied(int occupants) {
        if (occupants >= 2 && occupants <= maxCapacity) {
            this.occupied = true;
            this.booked = true;
            System.out.println(name + " is now occupied by " + occupants + " persons. AC and lights turned on.");
        } else if (occupants == 0) {
            this.occupied = false;
            this.booked = false;
            System.out.println(name + " is now unoccupied. AC and lights turned off.");
        } else {
            System.out.println(name + " occupancy insufficient to mark as occupied.");
        }
        notifyObservers();
    }

    public void book(String time, int duration) {
        if (!booked) {
            booked = true;
            System.out.println(name + " booked from " + time + " for " + duration + " minutes.");
        } else {
            System.out.println(name + " is already booked during this time. Cannot book.");
        }
    }

    public void cancelBooking() {
        if (booked) {
            booked = false;
            System.out.println("Booking for " + name + " cancelled successfully.");
        } else {
            System.out.println(name + " is not booked. Cannot cancel booking.");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}

// Observer interface
interface Observer {
    void update(Room room);
}

// AC and Lights control as Observers
class AC implements Observer {
    @Override
    public void update(Room room) {
        if (!room.occupied) {
            System.out.println("AC turned off in " + room);
        }
    }
}

class Lights implements Observer {
    @Override
    public void update(Room room) {
        if (!room.occupied) {
            System.out.println("Lights turned off in " + room);
        }
    }
}

// Command interface and its implementations
interface Command {
    void execute();
}

class ConfigureRoomsCommand implements Command {
    private Office office;
    private int count;

    public ConfigureRoomsCommand(Office office, int count) {
        this.office = office;
        this.count = count;
    }

    @Override
    public void execute() {
        office.configureRooms(count);
    }
}

class SetMaxCapacityCommand implements Command {
    private Room room;
    private int capacity;

    public SetMaxCapacityCommand(Room room, int capacity) {
        this.room = room;
        this.capacity = capacity;
    }

    @Override
    public void execute() {
        room.setMaxCapacity(capacity);
    }
}

class AddOccupantCommand implements Command {
    private Room room;
    private int occupants;

    public AddOccupantCommand(Room room, int occupants) {
        this.room = room;
        this.occupants = occupants;
    }

    @Override
    public void execute() {
        room.setOccupied(occupants);
    }
}

class BookRoomCommand implements Command {
    private Room room;
    private String time;
    private int duration;

    public BookRoomCommand(Room room, String time, int duration) {
        this.room = room;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public void execute() {
        room.book(time, duration);
    }
}

class CancelBookingCommand implements Command {
    private Room room;

    public CancelBookingCommand(Room room) {
        this.room = room;
    }

    @Override
    public void execute() {
        room.cancelBooking();
    }
}

// Main class to demonstrate usage
public class SmartOffice {
    public static void main(String[] args) {
        Office office = Office.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Configure Rooms");
            System.out.println("2. Set Room Max Capacity");
            System.out.println("3. Add Occupant");
            System.out.println("4. Book Room");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter number of rooms to configure: ");
                    int roomCount = scanner.nextInt();
                    Command configRooms = new ConfigureRoomsCommand(office, roomCount);
                    configRooms.execute();
                    break;
                case 2:
                    System.out.print("Enter room number to set max capacity: ");
                    int roomNumberForCapacity = scanner.nextInt();
                    Room roomForCapacity = office.getRoom(roomNumberForCapacity);
                    if (roomForCapacity != null) {
                        System.out.print("Enter max capacity for Room " + roomNumberForCapacity + ": ");
                        int capacity = scanner.nextInt();
                        Command setMaxCapacity = new SetMaxCapacityCommand(roomForCapacity, capacity);
                        setMaxCapacity.execute();
                    }
                    break;
                case 3:
                    System.out.print("Enter room number to add occupants: ");
                    int roomNumberForOccupants = scanner.nextInt();
                    Room roomForOccupants = office.getRoom(roomNumberForOccupants);
                    if (roomForOccupants != null) {
                        System.out.print("Enter number of occupants for Room " + roomNumberForOccupants + ": ");
                        int occupants = scanner.nextInt();
                        Command addOccupant = new AddOccupantCommand(roomForOccupants, occupants);
                        addOccupant.execute();
                    }
                    break;
                case 4:
                    System.out.print("Enter room number to book: ");
                    int roomNumberForBooking = scanner.nextInt();
                    Room roomForBooking = office.getRoom(roomNumberForBooking);
                    if (roomForBooking != null) {
                        System.out.print("Enter start time (e.g., 09:00) for Room " + roomNumberForBooking + ": ");
                        String time = scanner.next();
                        System.out.print("Enter duration in minutes for Room " + roomNumberForBooking + ": ");
                        int duration = scanner.nextInt();
                        Command bookRoom = new BookRoomCommand(roomForBooking, time, duration);
                        bookRoom.execute();
                    }
                    break;
                case 5:
                    System.out.print("Enter room number to cancel booking: ");
                    int roomNumberForCancel = scanner.nextInt();
                    Room roomForCancel = office.getRoom(roomNumberForCancel);
                    if (roomForCancel != null) {
                        Command cancelBooking = new CancelBookingCommand(roomForCancel);
                        cancelBooking.execute();
                    }
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
