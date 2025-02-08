package az.xalqbank.mstransactionevents.repository;

import az.xalqbank.mstransactionevents.model.TransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {
}
