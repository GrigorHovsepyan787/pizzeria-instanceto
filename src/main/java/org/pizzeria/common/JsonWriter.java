package org.pizzeria.common;

import org.pizzeria.model.Customer;
import org.pizzeria.model.Order;
import org.pizzeria.model.Pizza;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonWriter {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path CUSTOMERS_FILE = DATA_DIR.resolve("customers.json");
    private static final Path ORDERS_FILE = DATA_DIR.resolve("orders.json");

    private static final JsonWriter INSTANCE = new JsonWriter();

    private static final Pattern PIZZA_ENTRY = Pattern.compile(
            "\\{\\s*\"name\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"type\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"count\"\\s*:\\s*(\\d+)\\s*\\}");

    private final Object writeLock = new Object();

    private JsonWriter() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create data directory: " + DATA_DIR, e);
        }
    }

    public static JsonWriter getInstance() {
        return INSTANCE;
    }

    public void writeCustomer(Customer customer) {
        synchronized (writeLock) {
            appendLine(CUSTOMERS_FILE, toJson(customer));
        }
    }

    public void writeOrder(Order order) {
        synchronized (writeLock) {
            appendLine(ORDERS_FILE, toJson(order));
        }
    }

    public List<Customer> readCustomers() {
        return readLines(CUSTOMERS_FILE, this::parseCustomer);
    }

    public List<Order> readOrders() {
        return readLines(ORDERS_FILE, this::parseOrder);
    }

    private void appendLine(Path file, String json) {
        try (BufferedWriter writer = Files.newBufferedWriter(file,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(json);
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write to " + file, e);
        }
    }

    private <T> List<T> readLines(Path file, Function<String, T> parser) {
        List<T> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                try {
                    result.add(parser.apply(trimmed));
                } catch (Exception e) {
                    System.err.println("Skipping malformed JSON line: " + trimmed);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read from " + file, e);
        }
        return result;
    }

    // ---------- serialization ----------

    private String toJson(Customer customer) {
        return "{" +
                "\"customerId\":" + customer.getCustomerId() +
                ",\"fullName\":\"" + escape(customer.getFullName()) + "\"" +
                ",\"username\":\"" + escape(customer.getUsername()) + "\"" +
                "}";
    }

    private String toJson(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"orderId\":");
        sb.append(order.getOrderId() == null ? "null" : order.getOrderId());
        sb.append(",\"customerId\":").append(order.getCustomerId());
        sb.append(",\"pizzas\":[");
        boolean first = true;
        for (Map.Entry<Pizza, Integer> entry : order.getPizzas().entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            Pizza pizza = entry.getKey();
            sb.append("{\"name\":\"").append(escape(pizza.getName())).append("\"");
            sb.append(",\"type\":\"").append(escape(pizza.getType())).append("\"");
            sb.append(",\"count\":").append(entry.getValue()).append("}");
        }
        sb.append("]}");
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char ch : value.toCharArray()) {
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (ch < 0x20) {
                        sb.append(String.format("\\u%04x", (int) ch));
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }

    // ---------- deserialization ----------

    private Customer parseCustomer(String line) {
        long customerId = extractLong(line, "customerId");
        String fullName = extractString(line, "fullName");
        String username = extractString(line, "username");
        Customer customer = new Customer(fullName, username);
        customer.setCustomerId(customerId);
        return customer;
    }

    private Order parseOrder(String line) {
        Long orderId = extractNullableLong(line, "orderId");
        long customerId = extractLong(line, "customerId");
        Order order = new Order(customerId, new HashMap<Pizza, Integer>());
        order.setOrderId(orderId);
        Matcher matcher = PIZZA_ENTRY.matcher(line);
        while (matcher.find()) {
            Pizza pizza = new Pizza(matcher.group(1), matcher.group(2), new String[0]);
            int count = Integer.parseInt(matcher.group(3));
            order.addPizza(pizza, count);
        }
        return order;
    }

    private long extractLong(String line, String key) {
        Matcher matcher = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+)").matcher(line);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException("Missing field: " + key);
    }

    private Long extractNullableLong(String line, String key) {
        Matcher nullMatcher = Pattern.compile("\"" + key + "\"\\s*:\\s*null").matcher(line);
        if (nullMatcher.find()) {
            return null;
        }
        Matcher matcher = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+)").matcher(line);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

    private String extractString(String line, String key) {
        Matcher matcher = Pattern.compile("\"" + key + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(line);
        if (matcher.find()) {
            return unescape(matcher.group(1));
        }
        throw new IllegalArgumentException("Missing field: " + key);
    }

    private String unescape(String value) {
        return value.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\b", "\b")
                .replace("\\f", "\f");
    }
}
