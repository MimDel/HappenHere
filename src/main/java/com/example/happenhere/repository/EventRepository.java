package com.example.happenhere.repository;

import com.example.happenhere.dto.enums.SortEnum;
import com.example.happenhere.model.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    boolean existsById(Long id);

    Optional<EventEntity> findById(Long aLong);

    @Query("SELECT DISTINCT e FROM EventEntity e LEFT JOIN e.categories c WHERE " +
            "(:town IS NULL OR e.venue.address.town = :town) AND " +
            "(:country IS NULL OR e.venue.address.country = :country) AND " +
            "(:categories IS NULL OR c.name IN :categories) AND " +
            "(:dateFrom IS NULL OR e.startingDate >= :dateFrom) AND " +
            "(:dateTo IS NULL OR e.startingDate <= :dateTo) AND " +
            "(:priceFrom IS NULL OR e.price >= :priceFrom) AND " +
            "(:priceTo IS NULL OR e.price <= :priceTo) " +
            "ORDER BY " +
            "CASE :sortBy " +
            "  WHEN 'DATE_ASC' THEN e.startingDate END ASC, " +
            "CASE :sortBy " +
            "  WHEN 'DATE_DESC' THEN e.startingDate END DESC, " +
            "CASE :sortBy " +
            "  WHEN 'PRICE_ASC' THEN e.price END ASC, " +
            "CASE :sortBy " +
            "  WHEN 'PRICE_DESC' THEN e.price END DESC, " +
            "e.id ASC")
    Page<EventEntity> findWithFiltersAndSort(
            @Param("town") String town,
            @Param("country") String country,
            @Param("categories") String[] categories,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("priceFrom") BigDecimal priceFrom,
            @Param("priceTo") BigDecimal priceTo,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );
}
