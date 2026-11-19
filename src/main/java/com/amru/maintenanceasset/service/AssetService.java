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

    // method get asset by id
    @Transactional
    public AssetResponse getAssetById(Long id) {

        Asset asset = assetRepository.findById(id);

        if (asset == null) {

            throw new ResourceNotFoundException(
                    "Asset with id " + id + " not found"
            );

        }

        return mapToResponse(asset);

    }

    // method create new asset
    @Transactional
    public AssetResponse createAsset(AssetRequest request) {

        // 1. check duplicate asset code
        if (assetRepository.findByAssetCode(request.getAssetCode()) != null) {

            throw new BusinessRuleException(
                    "Asset code is already registered"
            );

        }

        // 2. search category by id
        AssetCategory category = assetCategoryRepository.findById(request.getCategoryId());

        if (category == null) {

            throw new ResourceNotFoundException(
                    "Asset category with id " + request.getCategoryId() + " not found"
            );

        }

        // 3. check category active
        if (!Boolean.TRUE.equals(category.getActive())) {

            throw new BusinessRuleException(
                    "Cannot assign an inactive asset category"
            );

        }

        // search user by id
        User user = userRepository.findById(request.getCreatedBy());

        if (user == null) {

            throw new ResourceNotFoundException(
                    "User with id " + request.getCreatedBy() + " not found"
            );

        }

        // 6. check user's role active
        if (user.getRole() == null || !Boolean.TRUE.equals(user.getRole().getIsActive())) {

            throw new BusinessRuleException(
                    "Cannot assign a user with an inactive role as asset creator"
            );

        }

        // 7. create asset entity
        Asset asset = new Asset();

        asset.setAssetCode(request.getAssetCode());
        asset.setName(request.getName());
        asset.setCategory(category);
        asset.setCreatedBy(user);
        asset.setDescription(request.getDescription());
        asset.setLocation(request.getLocation());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setStatus(request.getStatus().trim());

        // 8. fill timestamp
        OffsetDateTime now = OffsetDateTime.now();

        asset.setCreatedAt(now);
        asset.setUpdatedAt(now);

        // 9. save asset
        assetRepository.persist(asset);

        return mapToResponse(asset);

    }

    // method update asset
    public AssetResponse updateAsset(Long id, AssetRequest request) {

        // 1. check whether asset exists
        Asset asset = assetRepository.findById(id);

        if (asset == null) {

            throw new ResourceNotFoundException(
                    "Asset with id " + id + " not found"
            );

        }

        // 2. check duplicate asset code
        Asset existingAsset = assetRepository.findByAssetCode(request.getAssetCode());

        if (existingAsset != null && !existingAsset.getId().equals(id)) {

            throw new BusinessRuleException(
                    "Asset code is already registered"
            );

        }

        // 3. search category by id
        AssetCategory category = assetCategoryRepository.findById(request.getCategoryId());

        if (category == null) {

            throw new ResourceNotFoundException(
                    "Asset category with id " + request.getCategoryId() + " not found"
            );

        }

        // 4. check category active
        if (!Boolean.TRUE.equals(category.getActive())) {

            throw new BusinessRuleException(
                    "Cannot assign an inactive asset category"
            );

        }

        // 5. update asset data
        asset.setAssetCode(request.getAssetCode().trim());
        asset.setName(request.getName().trim());
        asset.setCategory(category);
        asset.setDescription(request.getDescription());
        asset.setLocation(request.getLocation());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setStatus(request.getStatus().trim());
        asset.setUpdatedAt(OffsetDateTime.now());

        // createdBy is intentionally not changed during update

        return mapToResponse(asset);

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
