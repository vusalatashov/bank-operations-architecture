package az.xalqbank.mscustomers.repository;

import az.xalqbank.mscustomers.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}