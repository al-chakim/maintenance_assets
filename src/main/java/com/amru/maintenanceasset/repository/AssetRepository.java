package com.amru.maintenanceasset.repository;

import com.amru.maintenanceasset.entity.Asset;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.HashMap;
import java.util.Map;

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

    // search data asset by parameter
    public List<Asset> searchAssets(
            String status,
            Long categoryId,
            String keyword
    ) {

        StringBuilder query = new StringBuilder("1 = 1");

        Map<String, Object> parameters = new HashMap<>();

        if (status != null && !status.isBlank()) {

            query.append(" and lower(status) = lower(:status)");
            parameters.put("status", status.trim());

        }

        if (categoryId != null) {
            query.append(" and category.id = :categoryId");
            parameters.put("categoryId", categoryId);
        }

        if (keyword != null && !keyword.isBlank()) {

            query.append(
                    " and (lower(name) like lower(:keyword) " +
                            "or lower(assetCode) like lower(:keyword))"
            );

            parameters.put(
                    "keyword",
                    "%" + keyword.trim() + "%"
            );

        }

        return find(query.toString(), parameters).list();

    }

}
