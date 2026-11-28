package com.amru.maintenanceasset.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class MaintenanceRequestRequest {

    @NotBlank(message = "Request code is required")
    @Size(max = 50, message = "Request code must not exceed 50 characters")
    private String requestCode;

    @NotNull(message = "Asset ID is required")
    @Positive(message = "Asset ID must be positive")
    private Long assetId;

    @NotNull(message = "Requested ID is Required")
    @Positive(message = "Requested ID must be positive")
    private Long requestedBy;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Priority is required")
    @Size(max = 20, message = "Priority must not exceed 20 characters")
    private String priority;

    // getter and setter
    // request code
    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    // asset id
    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    // requested id
    public Long getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Long requestedBy) {
        this.requestedBy = requestedBy;
    }

    // title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // priority
    public void setPriority(String priority) {
        this.priority = priority;
    }

}
