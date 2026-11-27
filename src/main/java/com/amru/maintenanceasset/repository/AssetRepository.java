package com.amru.maintenanceasset.repository;

import com.amru.maintenanceasset.entity.Asset;
import io.quarkus.panache.common.Sort;
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

    // search data asset by parameter and sort, pagination data item
    public List<Asset> searchAssets(
            String status,
            Long categoryId,
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        StringBuilder query = new StringBuilder("1 = 1");

        Map<String, Object> parameters = new HashMap<>();

        // check asset status not null
        if (status != null && !status.isBlank()) {

            query.append(" and lower(status) = lower(:status)");
            parameters.put("status", status.trim());

        }

        // check category id not null
        if (categoryId != null) {
            query.append(" and category.id = :categoryId");
            parameters.put("categoryId", categoryId);
        }

        // check keyword not null
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

        Sort sort = Sort.by(sortBy);

        // short data
        if ("desc".equalsIgnoreCase(direction)) {

            sort = sort.descending();

        } else {

            sort = sort.ascending();

        }

        return find(query.toString(), sort, parameters)
                .page(page, size)
                .list();

    }

    // count the asset by filter
    public long countSearchAssets (
            String status,
            Long categoryId,
            String keyword
    ) {

        StringBuilder query = new StringBuilder("1 = 1");

        Map<String, Object> parameters = new HashMap<>();

        // check status asset not null
        if (status != null && !status.isBlank()) {

            query.append(" and lower(status) = lower(:status)");

            parameters.put("status", status.trim());

        }

        // check category asset not null
        if (categoryId != null) {

            query.append(" and category.id = :categoryId");

            parameters.put("categoryId", categoryId);

        }

        // check keyword not null
        if (keyword != null && !keyword.isBlank()) {

            query.append(
                    " and (lower(name) like lower(:keyword)" +
                            " or lower(assetCode) like lower(:keyword))"
            );

            parameters.put("keyword", "%" + keyword.trim() + "%");

        }

        return count(query.toString(), parameters);

    }

}
