package tbank.mr_irmag.tbank_kudago_task.services;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Place;

import java.time.LocalDate;

public class EventSpecification {
    public static Specification<Event> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<Event> hasPlace(Long placeId) {
        return (root, query, criteriaBuilder) -> {
            if (placeId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Event, Place> placeJoin = root.join("place");
            return criteriaBuilder.equal(placeJoin.get("id"), placeId);
        };
    }

    public static Specification<Event> betweenDates(LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction();
            } else if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("date"), fromDate, toDate);
            } else if (fromDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate);
            }
        };
    }

    public static Specification<Event> fetchPlace() {
        return (root, query, criteriaBuilder) -> {
            root.fetch("place", JoinType.LEFT);
            return criteriaBuilder.conjunction();
        };
    }
}
