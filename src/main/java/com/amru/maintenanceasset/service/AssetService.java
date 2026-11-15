package com.amru.maintenanceasset.service;

import com.amru.maintenanceasset.dto.AssetRequest;
import com.amru.maintenanceasset.dto.AssetResponse;
import com.amru.maintenanceasset.entity.Asset;
import com.amru.maintenanceasset.entity.AssetCategory;
import com.amru.maintenanceasset.entity.User;
import com.amru.maintenanceasset.exception.BusinessRuleException;
import com.amru.maintenanceasset.exception.ResourceNotFoundException;
import com.amru.maintenanceasset.repository.AssetRepository;
import com.amru.maintenanceasset.repository.AssetCategoryRepository;
import com.amru.maintenanceasset.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class AssetService {

    @Inject
    AssetRepository assetRepository;

    @Inject
    AssetCategoryRepository assetCategoryRepository;

    @Inject
    UserRepository userRepository;

    // method get all assets
    @Transactional
    public List<AssetResponse> getAllAssets() {

        return assetRepository.listAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    // mapper
    private AssetResponse mapToResponse(Asset asset) {

        AssetResponse response = new AssetResponse();

        response.setId(asset.getId());
        response.setAssetCode(asset.getAssetCode());
        response.setName(asset.getName());

        response.setCategoryId(asset.getCategory().getId());
        response.setCategoryByName(asset.getCategory().getName());

        response.setCreatedBy(asset.getCreatedBy().getId());
        response.setCreatedByName(asset.getCreatedBy().getName());

        response.setDescription(asset.getDescription());
        response.setLocation(asset.getLocation());
        response.setPurchaseDate(asset.getPurchaseDate());
        response.setPurchasePrice(asset.getPurchasePrice());
        response.setStatus(asset.getStatus());

        response.setCreatedAt(asset.getCreatedAt());
        response.setUpdatedAt(asset.getUpdatedAt());

        return response;

    }

}
