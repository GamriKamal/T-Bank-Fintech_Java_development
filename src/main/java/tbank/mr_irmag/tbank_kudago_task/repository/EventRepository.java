package tbank.mr_irmag.tbank_kudago_task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    boolean existsByName(String name);

    Optional<Event> findByName(String name);

}
