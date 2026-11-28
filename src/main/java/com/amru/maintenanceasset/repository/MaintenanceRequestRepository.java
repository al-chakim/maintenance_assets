package com.amru.maintenanceasset.repository;

import com.amru.maintenanceasset.entity.MaintenanceRequest;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

@ApplicationScoped
public class MaintenanceRequestRepository implements PanacheRepository<MaintenanceRequest>{

    // find by request code
    public MaintenanceRequest findByRequestCode(String requestCode) {

        return find("requestCode", requestCode).firstResult();

    }

    // find by asset id
    public List<MaintenanceRequest> findByAssetId(Long assetId) {

        return find("asset.id", assetId).list();

    }

    // find by requested user id
    public List<MaintenanceRequest> findByRequestById(Long userId) {

        return find("requestedBy", userId).list();

    }

}
