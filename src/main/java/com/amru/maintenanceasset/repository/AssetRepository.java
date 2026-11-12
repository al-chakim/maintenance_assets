package com.amru.maintenanceasset.repository;

import com.amru.maintenanceasset.entity.Asset;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

@ApplicationScoped
public class AssetRepository implements PanacheRepository<Asset> {

    // find asset by id
    public Asset findByAssetCode(String assetCode) {

        return find("assetCode", assetCode).firstResult();

    }

    // find by category id
    public List<Asset> findByCategoryId(Long categoryId) {

        return find("category.id", categoryId).list();

    }

    // count category id
    public long countCategoryId(Long categoryId) {

        return count("category.id", categoryId);

    }

}
