import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phoneNumber;
    private String emailAddress;

    public Contact(String name, String phoneNumber, String emailAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone Number: " + phoneNumber + ", Email Address: " + emailAddress;
    }
}

class AddressBook implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Contact> contacts;

    public AddressBook() {
        this.contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(String name) {
        contacts.removeIf(contact -> contact.getName().equalsIgnoreCase(name));
    }

    public Contact searchContact(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                return contact;
            }
        }
        return null;
    }

    public void displayAllContacts() {
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
    }

    public ArrayList<Contact> getAllContacts() {
        return contacts;
    }

    public void writeContactsToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void readContactsFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = inputStream.readObject();
            if (obj instanceof ArrayList) {
                contacts = (ArrayList<Contact>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle file not found or other exceptions
            e.printStackTrace();
        }
    }
}

public class AddressBookSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AddressBook addressBook = new AddressBook();

        // Read contacts from file (if available)
        addressBook.readContactsFromFile("contacts.dat");

        while (true) {
            System.out.println("\nAddress Book System");
            System.out.println("1. Add Contact");
            System.out.println("2. Edit Contact");
            System.out.println("3. Remove Contact");
            System.out.println("4. Search Contact");
            System.out.println("5. Display All Contacts");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline character

                switch (choice) {
                    case 1:
                        addContact(addressBook, scanner);
                        break;
                    case 2:
                        editContact(addressBook, scanner);
                        break;
                    case 3:
                        removeContact(addressBook, scanner);
                        break;
                    case 4:
                        searchContact(addressBook, scanner);
                        break;
                    case 5:
                        addressBook.displayAllContacts();
                        break;
                    case 6:
                        // Write contacts to file when exiting the application
                        addressBook.writeContactsToFile("contacts.dat");
                        System.out.println("Exiting the application. Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume the invalid input
            }
        }
    }

    private static void addContact(AddressBook addressBook, Scanner scanner) {
        System.out.print("Enter contact name: ");
        String name = scanner.nextLine();

        System.out.print("Enter contact phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter contact email address: ");
        String emailAddress = scanner.nextLine();

        Contact contact = new Contact(name, phoneNumber, emailAddress);
        addressBook.addContact(contact);
        System.out.println("Contact added successfully!");
    }

    private static void editContact(AddressBook addressBook, Scanner scanner) {
        System.out.print("Enter contact name to edit: ");
        String name = scanner.nextLine();
        Contact contact = addressBook.searchContact(name);

        if (contact != null) {
            System.out.println("Editing Contact:");
            System.out.println(contact);

            System.out.print("Enter new contact name: ");
            String newName = scanner.nextLine();

            System.out.print("Enter new contact phone number: ");
            String newPhoneNumber = scanner.nextLine();

            System.out.print("Enter new contact email address: ");
            String newEmailAddress = scanner.nextLine();

            Contact updatedContact = new Contact(newName, newPhoneNumber, newEmailAddress);
            addressBook.removeContact(name);
            addressBook.addContact(updatedContact);

            System.out.println("Contact information edited successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void removeContact(AddressBook addressBook, Scanner scanner) {
        System.out.print("Enter contact name to remove: ");
        String name = scanner.nextLine();
        addressBook.removeContact(name);
        System.out.println("Contact removed successfully!");
    }

    private static void searchContact(AddressBook addressBook, Scanner scanner) {
        System.out.print("Enter contact name to search: ");
        String name = scanner.nextLine();
        Contact contact = addressBook.searchContact(name);

        if (contact != null) {
            System.out.println("Contact found:\n" + contact);
        } else {
            System.out.println("Contact not found.");
        }
    }
}
