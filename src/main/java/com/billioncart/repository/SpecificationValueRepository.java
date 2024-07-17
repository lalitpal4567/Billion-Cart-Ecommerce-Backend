package com.billioncart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.productCatalogue.Product;
import com.billioncart.model.productCatalogue.SpecificationName;
import com.billioncart.model.productCatalogue.SpecificationValue;

public interface SpecificationValueRepository extends JpaRepository<SpecificationValue, Long>{
	Optional<SpecificationValue> findByProductAndSpecificationName(Product product, SpecificationName specificationName);
//	Optional<SpecificationValue> findByProductAndSpecificationNameId(Product product, Long specNameId);

}
