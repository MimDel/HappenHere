package com.example.happenhere.repository;

import com.example.happenhere.model.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    boolean existsById(Long id);

    Optional<EventEntity> findById(Long aLong);

    @Query("SELECT e FROM EventEntity e JOIN e.categories c WHERE " +
            "(:town IS NULL OR e.venue.address.town = :town) AND " +
            "(:country IS NULL OR e.venue.address.country = :country) AND " +
            "(:category IS NULL OR c IN :categories) AND " +
            "(:dateFrom IS NULL OR e.startingDate >= :dateFrom) AND " +
            "(:dateTo IS NULL OR e.startingDate <= :dateTo) AND " +
            "(:priceFrom IS NULL OR e.price >= :priceFrom) AND " +
            "(:priceTo IS NULL OR e.price <= :priceTo) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'DATE_ASC' THEN e.startingDate END ASC, " +
            "CASE WHEN :sort = 'DATE_DESC' THEN e.startingDate END DESC, " +
            "CASE WHEN :sort = 'PRICE_ASC' THEN e.price END ASC, " +
            "CASE WHEN :sort = 'PRICE_DESC' THEN e.price END DESC")
    Page<EventEntity> findWithFiltersAndSort(
            @Param("town") String town,
            @Param("country") String country,
            @Param("categories") String[] categories,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("priceFrom") BigDecimal priceFrom,
            @Param("priceTo") BigDecimal priceTo,
            @Param("sort") String sort,
            Pageable pageable
    );
}
