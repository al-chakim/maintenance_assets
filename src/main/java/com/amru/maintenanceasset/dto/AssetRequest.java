package com.amru.maintenanceasset.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AssetRequest {

    @NotBlank(message = "Asset code is required")
    @Size(max = 50, message = "Asset code must not exceed 50 characters")
    private String assetCode;
    
    @NotBlank(message = "Asset name is required")
    @Size(max = 150, message = "Asset name must not exceed 150 characters")
    private String name;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be greater than zero")
    private Long categoryId;

    @NotNull(message = "Created by is required")
    @Positive(message = "Created by must be greater than zero")
    private Long createdBy;

    private String description;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @PastOrPresent(message = "Purchase date cannot be in the furture")
    private LocalDate purchaseDate;

    @DecimalMin(value = "0.0", message = "Purchase price cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Purchase price must have at most 13 integer digits and 2 decimal places")
    private BigDecimal purchasePrice;

    @NotBlank(message = "status is required")
    @Size(max = 30, message = "Status is required")
    private String status;

    // getter and setter
    // asset code
    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    // name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // category id
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // created by
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    // description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // purchase date
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    // purchase price
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    // status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
