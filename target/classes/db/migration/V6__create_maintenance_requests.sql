CREATE TABLE maintenance_requests (
    id BIGSERIAL PRIMARY KEY,
    request_code VARCHAR(50) NOT NULL UNIQUE,
    asset_id BIGINT NOT NULL,
    requested_id BIGINT NOT NULL,
    assigned_to BIGINT,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    requested_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    cost NUMERIC(15,2),
    resolution_note TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_maintenance_asset
        FOREIGN KEY (asset_id)
        REFERENCES assets(id),

    CONSTRAINT fk_maintenance_requested_by
        FOREIGN KEY (requested_id)
        REFERENCES users(id),

    CONSTRAINT fk_maintenance_assigned_to
        FOREIGN KEY (assigned_to)
        REFERENCES users(id)
);