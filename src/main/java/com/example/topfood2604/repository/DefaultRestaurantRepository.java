package com.example.topfood2604.repository;

import com.example.topfood2604.entity.DefaultRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefaultRestaurantRepository
        extends JpaRepository<DefaultRestaurant, Long> {

    List<DefaultRestaurant> findTop6ByCardAreaAndEnabledOrderBySortOrderAsc(
            String cardArea,
            Boolean enabled
    );
}