package github.couryrr.aicoding.controller;

import github.couryrr.aicoding.api.DefaultApi;
import github.couryrr.aicoding.model.*;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class CustomerController implements DefaultApi {
    // private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final Map<UUID, Customer> customers = new ConcurrentHashMap<>();

    @Override
    public ResponseEntity<Customer> customersPost(CustomerCreate customerCreate) {
        Customer customer = new Customer()
                .id(UUID.randomUUID())
                .firstName(customerCreate.getFirstName())
                .lastName(customerCreate.getLastName())
                .birthdate(customerCreate.getBirthdate())
                .phone(customerCreate.getPhone())
                .createdAt((int) Instant.now().toEpochMilli())
                .updatedAt((int) Instant.now().toEpochMilli());

        customers.put(customer.getId(), customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);

    }

    @Override
    public ResponseEntity<Void> customersCustomerIdDelete(UUID customerId) {
        if (customers.remove(customerId) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Customer> customersCustomerIdGet(UUID customerId) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<CustomersGet200Response> customersGet(Integer page, Integer limit) {
        List<Customer> customerList = new ArrayList<>(customers.values());
        int totalCustomers = customerList.size();
        int totalPages = (int) Math.ceil((double) totalCustomers / limit);

        int start = (page - 1) * limit;
        int end = Math.min(start + limit, totalCustomers);

        if (start >= totalCustomers) {
            start = 0;
            end = 0;
        }

        List<Customer> paginatedCustomers = customerList.subList(start, end);

        PaginationInfo pagination = new PaginationInfo()
                .total(totalCustomers)
                .page(page)
                .limit(limit)
                .totalPages(totalPages);

        return ResponseEntity.ok(new CustomersGet200Response()
                .data(paginatedCustomers)
                .pagination(pagination));
    }

    @Override
    public ResponseEntity<Customer> customersCustomerIdPut(UUID customerId, CustomerUpdate customerUpdate) {
        Customer existingCustomer = customers.get(customerId);
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        if (customerUpdate.getFirstName() != null) {
            existingCustomer.setFirstName(customerUpdate.getFirstName());
        }
        if (customerUpdate.getLastName() != null) {
            existingCustomer.setLastName(customerUpdate.getLastName());
        }
        if (customerUpdate.getBirthdate() != null) {
            existingCustomer.setBirthdate(customerUpdate.getBirthdate());
        }
        if (customerUpdate.getPhone() != null) {
            existingCustomer.setPhone(customerUpdate.getPhone());
        }

        existingCustomer.setUpdatedAt((int) Instant.now().toEpochMilli());
        return ResponseEntity.ok(existingCustomer);
    }

    @Override
    public ResponseEntity<CustomersCustomerIdPurchasesGet200Response> customersCustomerIdPurchasesGet(UUID customerId,
            Integer page, Integer limit) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        List<Purchase> purchases = customer.getPurchases() != null ? customer.getPurchases() : Collections.emptyList();
        int totalPurchases = purchases.size();
        int totalPages = (int) Math.ceil((double) totalPurchases / limit);

        int start = (page - 1) * limit;
        int end = Math.min(start + limit, totalPurchases);

        if (start >= totalPurchases) {
            start = 0;
            end = 0;
        }

        List<Purchase> paginatedPurchases = purchases.subList(start, end);

        PaginationInfo pagination = new PaginationInfo()
                .total(totalPurchases)
                .page(page)
                .limit(limit)
                .totalPages(totalPages);

        return ResponseEntity.ok(new CustomersCustomerIdPurchasesGet200Response()
                .data(paginatedPurchases)
                .pagination(pagination));
    }

    @Override
    public ResponseEntity<Purchase> customersCustomerIdPurchasesPost(UUID customerId, PurchaseCreate purchaseCreate) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        Purchase purchase = new Purchase()
                .id(UUID.randomUUID())
                .item(new Item()
                        .id(UUID.randomUUID())
                        .name(purchaseCreate.getItem().getName())
                        .description(purchaseCreate.getItem().getDescription())
                        .price(purchaseCreate.getItem().getPrice())
                        .createdAt((int) Instant.now().toEpochMilli())
                        .updatedAt((int) Instant.now().toEpochMilli()))
                .quantity(purchaseCreate.getQuantity())
                .shipTo(new Address()
                        .id(UUID.randomUUID())
                        .streetLineOne(purchaseCreate.getShipTo().getStreetLineOne())
                        .streetLineTwo(purchaseCreate.getShipTo().getStreetLineTwo())
                        .city(purchaseCreate.getShipTo().getCity())
                        .state(purchaseCreate.getShipTo().getState())
                        .zip(purchaseCreate.getShipTo().getZip())
                        .createdAt((int) Instant.now().toEpochMilli())
                        .updatedAt((int) Instant.now().toEpochMilli()))
                .createdAt((int) Instant.now().toEpochMilli())
                .updatedAt((int) Instant.now().toEpochMilli());

        if (customer.getPurchases() == null) {
            customer.setPurchases(new ArrayList<>());
        }
        customer.getPurchases().add(purchase);

        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    @Override
    public ResponseEntity<Void> customersCustomerIdPurchasesPurchaseIdDelete(UUID customerId, UUID purchaseId) {
        Customer customer = customers.get(customerId);
        if (customer == null || customer.getPurchases() == null) {
            return ResponseEntity.notFound().build();
        }

        boolean removed = customer.getPurchases().removeIf(p -> p.getId().equals(purchaseId));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Purchase> customersCustomerIdPurchasesPurchaseIdGet(UUID customerId, UUID purchaseId) {
        Customer customer = customers.get(customerId);
        if (customer == null || customer.getPurchases() == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Purchase> purchase = customer.getPurchases().stream()
                .filter(p -> p.getId().equals(purchaseId))
                .findFirst();

        return purchase.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Purchase> customersCustomerIdPurchasesPurchaseIdPut(UUID customerId, UUID purchaseId,
            PurchaseUpdate purchaseUpdate) {
        Customer customer = customers.get(customerId);
        if (customer == null || customer.getPurchases() == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Purchase> purchaseOpt = customer.getPurchases().stream()
                .filter(p -> p.getId().equals(purchaseId))
                .findFirst();

        if (purchaseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Purchase purchase = purchaseOpt.get();
        if (purchaseUpdate.getItem() != null) {
            purchase
                    .getItem()
                    .name(purchaseUpdate.getItem().getName())
                    .description(purchaseUpdate.getItem().getDescription())
                    .price(purchaseUpdate.getItem().getPrice())
                    .updatedAt((int) Instant.now().toEpochMilli());
        }
        if (purchaseUpdate.getQuantity() != null) {
            purchase.setQuantity(purchaseUpdate.getQuantity());
        }
        if (purchaseUpdate.getShipTo() != null) {
            purchase
                    .getShipTo()
                    .streetLineOne(purchaseUpdate.getShipTo().getStreetLineOne())
                    .streetLineTwo(purchaseUpdate.getShipTo().getStreetLineTwo())
                    .city(purchaseUpdate.getShipTo().getCity())
                    .state(purchaseUpdate.getShipTo().getState())
                    .zip(purchaseUpdate.getShipTo().getZip())
                    .updatedAt((int) Instant.now().toEpochMilli());
        }

        purchase.setUpdatedAt((int) Instant.now().toEpochMilli());
        return ResponseEntity.ok(purchase);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
