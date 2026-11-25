package com.amru.maintenanceasset.dto;

import java.util.List;

public class AssetPageResponse {

    private List<AssetResponse> data;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;

//    public AssetPageRespone() {
//
//    }

    public AssetPageResponse(
            List<AssetResponse> data,
            int currentPage,
            int pageSize,
            long totalItems,
            int totalPages
    ) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    // getter setter
    // data
    public List<AssetResponse> getData() {
        return data;
    }

    public void setData(List<AssetResponse> data) {
        this.data = data;
    }

    // current page
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    // page size
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    // total items
    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    // total pages
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
