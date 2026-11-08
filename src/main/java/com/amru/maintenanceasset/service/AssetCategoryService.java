package com.amru.maintenanceasset.service;

import com.amru.maintenanceasset.dto.AssetCategoryRequest;
import com.amru.maintenanceasset.dto.AssetCategoryResponse;
import com.amru.maintenanceasset.entity.AssetCategory;
import com.amru.maintenanceasset.exception.BusinessRuleException;
import com.amru.maintenanceasset.exception.DuplicateAssetCategoryException;
import com.amru.maintenanceasset.exception.ResourceNotFoundException;
import com.amru.maintenanceasset.repository.AssetCategoryRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class AssetCategoryService {

    @Inject
    AssetCategoryRepository assetCategoryRepository;

    // get all data
    @Transactional
    public List<AssetCategoryResponse> getAllCategories() {

        return assetCategoryRepository.listAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    // get data active only
    @Transactional
    public List<AssetCategoryResponse> getActiveCategories() {

        return assetCategoryRepository.findActiveCategory()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    // method get data by id
    @Transactional
    public AssetCategoryResponse getCategoryById(Long id) {

        AssetCategory category = assetCategoryRepository.findById(id);

        if (category == null) {

            throw new ResourceNotFoundException(
                    "Asset category with id " + id + " not found"
            );

        }

        return mapToResponse(category);

    }

    // method create data
    @Transactional
    public AssetCategoryResponse createCategory(AssetCategoryRequest request) {

        // 1. normalization of category names
        String normalizedName = request.getName().trim();

        // 2. check for duplicate names
        if (assetCategoryRepository.findByName(normalizedName) != null) {

            throw new DuplicateAssetCategoryException(
                    "Asset category name is already registered"
            );

        }

        // 3. create category entity
        AssetCategory category = new AssetCategory();

        category.setName(normalizedName);
        category.setDescription(request.getDescription());
        category.setActive(true);

        OffsetDateTime now = OffsetDateTime.now();

        category.setCreatedAt(now);
        category.setUpdatedAt(now);

        assetCategoryRepository.persist(category);

        return mapToResponse(category);

    }

    // method update data
    @Transactional
    public AssetCategoryResponse updateCategory(
            Long id,
            AssetCategoryRequest request
    ) {

        // 1. check whether category exists
        AssetCategory category = assetCategoryRepository.findById(id);

        if (category == null) {

            throw new ResourceNotFoundException(
                    "Asset category with id " + id + " not found"
            );

        }

        // 2. normalize category name
        String normalizedName = request.getName().trim();

        // 3. check for duplicate category names
        AssetCategory existingCategory = assetCategoryRepository.findByName(normalizedName);

        if (existingCategory != null && !existingCategory.getId().equals(id)) {

            throw new DuplicateAssetCategoryException(
                    "Asset category name is already registered"
            );

        }

        // 4. update category data
        category.setName(normalizedName);
        category.setDescription(request.getDescription());
        category.setUpdatedAt(OffsetDateTime.now());

        // 5. return update category
        return mapToResponse(category);

    }

    // method deactivate data
    @Transactional
    public AssetCategoryResponse deactivateCategory(Long id) {

        // 1. check whether category exists
        AssetCategory category = assetCategoryRepository.findById(id);

        if (category == null) {

            throw new ResourceNotFoundException(
                    "Assets category with " + id + " not found"
            );

        }

        // 2.check whether category is already inactive
        if (!category.getActive()) {
            throw new BusinessRuleException(
                    "Assets category is alraady inactive"
            );
        }

        // 3. deactivate category
        category.setActive(false);
        category.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(category);

    }

    // method activate
    @Transactional
    public AssetCategoryResponse activateCategory(Long id) {

        // 1. check whether category exists
        AssetCategory category = assetCategoryRepository.findById(id);

        if (category == null) {

            throw new ResourceNotFoundException(
                    "Asset category with id " + id + " not found"
            );

        }

        // 2. check whether category is already active
        if (category.getActive()) {

            throw new BusinessRuleException(
                    "Asset category is already active"
            );

        }

        // 3. activate category
        category.setActive(true);
        category.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(category);

    }

    private AssetCategoryResponse mapToResponse(AssetCategory category) {

        AssetCategoryResponse response = new AssetCategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setActive(category.getActive());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        return response;

    }

}
