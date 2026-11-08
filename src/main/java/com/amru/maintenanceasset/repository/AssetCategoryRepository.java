package com.amru.maintenanceasset.repository;

import com.amru.maintenanceasset.entity.AssetCategory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AssetCategoryRepository implements PanacheRepository<AssetCategory> {

    public AssetCategory findByName (String name) {

        return find("lower(name) = lower(?1)", name).firstResult();

    }

    public List<AssetCategory> findActiveCategory() {

        return list("isActive", true);

    }

}
