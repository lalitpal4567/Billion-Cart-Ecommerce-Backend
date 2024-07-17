package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.productCatalogue.Color;

public interface ColorRepository extends JpaRepository<Color, Long>{

}
