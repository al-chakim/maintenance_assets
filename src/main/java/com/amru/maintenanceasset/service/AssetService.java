package com.amru.maintenanceasset.service;

import com.amru.maintenanceasset.dto.AssetRequest;
import com.amru.maintenanceasset.dto.AssetResponse;
import com.amru.maintenanceasset.entity.Asset;
import com.amru.maintenanceasset.entity.AssetCategory;
import com.amru.maintenanceasset.entity.AssetStatus;
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

        // 4. search user by id
        User user = userRepository.findById(request.getCreatedBy());

        if (user == null) {

            throw new ResourceNotFoundException(
                    "User with id " + request.getCreatedBy() + " not found"
            );

        }

        // 5. check user inactive
        if (!Boolean.TRUE.equals(user.getIsActive())) {

            throw new BusinessRuleException(
                    "Cannot assign an inactive user as asset creator"
            );

        }

        // 6. check user's role active
        if (user.getRole() == null || !Boolean.TRUE.equals(user.getRole().getIsActive())) {

            throw new BusinessRuleException(
                    "Cannot assign a user with an inactive role as asset creator"
            );

        }

        // (6). call validation status
        validateAssetStatus(request.getStatus());

        // 7. create asset entity
        Asset asset = new Asset();

        asset.setAssetCode(request.getAssetCode().trim());
        asset.setName(request.getName().trim());
        asset.setCategory(category);
        asset.setCreatedBy(user);
        asset.setDescription(request.getDescription());
        asset.setLocation(request.getLocation());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setStatus(request.getStatus().trim().toUpperCase());

        // 8. fill timestamp
        OffsetDateTime now = OffsetDateTime.now();

        asset.setCreatedAt(now);
        asset.setUpdatedAt(now);

        // 9. save asset
        assetRepository.persist(asset);

        return mapToResponse(asset);

    }

    // method update asset
    @Transactional
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

        // (4). call validation status
        validateAssetStatus(request.getStatus());

        // 5. update asset data
        asset.setAssetCode(request.getAssetCode().trim());
        asset.setName(request.getName().trim());
        asset.setCategory(category);
        asset.setDescription(request.getDescription());
        asset.setLocation(request.getLocation());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setStatus(request.getStatus().trim().toUpperCase());
        asset.setUpdatedAt(OffsetDateTime.now());

        // createdBy is intentionally not changed during update

        return mapToResponse(asset);

    }

    // method search asset
    @Transactional
    public List<AssetResponse> searchAssets(
            String status,
            Long categoryId,
            String keyword
    ) {

        // validate status if provided
        if (status != null && !status.isBlank()) {

            validateAssetStatus(status);
            status = status.trim().toUpperCase();

        }

        // validate category if provided
        if (categoryId != null) {

            AssetCategory category = assetCategoryRepository.findById(categoryId);

            if (category == null) {

                throw new ResourceNotFoundException(
                        "Asset category with id " + categoryId + " not found"
                );

            }

        }

        // search assets from repository
        return assetRepository.searchAssets(
                status,
                categoryId,
                keyword
        ).stream()
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

    // validation asset status
    private void validateAssetStatus(String status) {
        try {
            AssetStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessRuleException(
                    "Invalid asset status. Allowed values: AVAILABLE, IN_USE, UNDER_MAINTENANCE, DAMAGE, RETIRED"
            );
        }
    }

}
