package org.pizzeria;

import org.pizzeria.common.ApplicationContextHolder;
import org.pizzeria.exceptions.DuplicateUsernameException;
import org.pizzeria.model.Customer;
import org.pizzeria.model.Order;
import org.pizzeria.model.Pizza;
import org.pizzeria.repository.CustomerRepository;
import org.pizzeria.service.OrderService;
import org.pizzeria.service.OrderServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleApplication {

    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final Scanner scanner;
    private final Map<String, Pizza> availablePizzas;

    public ConsoleApplication(ApplicationContextHolder contextHolder) {
        this.customerRepository = contextHolder.getObject(CustomerRepository.class);
        this.orderService = new OrderServiceImpl(contextHolder);
        this.scanner = new Scanner(System.in);
        this.availablePizzas = new HashMap<>();
        availablePizzas.put("regular", Pizza.REGULAR);
        availablePizzas.put("calzone", new Pizza("Calzone", "CALZONE", new String[0]));
    }

    public void run() {
        registerCustomerFromConsole();
        placeOrderFromConsole();
    }

    private void registerCustomerFromConsole() {
        System.out.println("=== Customer Registration ===");
        System.out.print("Enter full name: ");
        String fullName = readLine();
        System.out.print("Enter username: ");
        String username = readLine();

        Customer customer = new Customer(fullName, username);
        try {
            String registeredUsername = customerRepository.registerCustomer(customer);
            System.out.println("Customer successfully registered and saved into the repository.");
            System.out.println("Customer details: " + customer);
        } catch (DuplicateUsernameException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void placeOrderFromConsole() {
        System.out.println();
        System.out.print("Do you want to place an order? (y/n): ");
        String answer = readLine();
        if (answer == null || !answer.trim().equalsIgnoreCase("y")) {
            System.out.println("Order skipped.");
            return;
        }

        System.out.println("=== Place Order ===");
        System.out.print("Enter your username: ");
        String username = readLine();

        Optional<Customer> customerOptional = customerRepository.findCustomerByUsername(username);
        if (customerOptional.isEmpty()) {
            System.out.println("Order failed: user is not registered: " + username);
            return;
        }
        Customer customer = customerOptional.get();

        Map<Pizza, Integer> pizzas = readPizzasFromConsole();
        if (pizzas.isEmpty()) {
            System.out.println("Order cancelled: no pizzas selected.");
            return;
        }

        Order order = new Order(customer.getCustomerId(), pizzas);
        long orderId = orderService.orderPizza(username, pizzas);
        order.setOrderId(orderId);

        System.out.println("Order successfully saved into the database.");
        System.out.println("Order details: " + order);
    }

    private Map<Pizza, Integer> readPizzasFromConsole() {
        Map<Pizza, Integer> pizzas = new HashMap<>();
        System.out.println("Available pizzas: Regular, Calzone");
        System.out.println("Enter pizza and quantity (e.g. Regular 2), leave blank to finish:");
        while (true) {
            System.out.print("> ");
            String line = readLine();
            if (line == null || line.trim().isEmpty()) {
                break;
            }
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid format, use: <pizza-name> <quantity>");
                continue;
            }
            Pizza pizza = findPizza(parts[0]);
            if (pizza == null) {
                System.out.println("Unknown pizza: " + parts[0]);
                continue;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity: " + parts[1]);
                continue;
            }
            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                continue;
            }
            pizzas.merge(pizza, quantity, Integer::sum);
        }
        return pizzas;
    }

    private Pizza findPizza(String name) {
        return availablePizzas.get(name.toLowerCase());
    }

    private String readLine() {
        return scanner.nextLine();
    }
}
