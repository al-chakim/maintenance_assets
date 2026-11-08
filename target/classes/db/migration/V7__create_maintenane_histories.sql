CREATE TABLE maintenance_histories (
    id BIGSERIAL PRIMARY KEY,
    maintenance_id BIGINT NOT NULL,
    change_by BIGINT NOT NULL,
    old_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    note TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_maintenance_history_maintenance
        FOREIGN KEY (maintenance_id)
        REFERENCES maintenance_requests(id),

    CONSTRAINT fk_maintenance_history_change_by
        FOREIGN KEY (change_by)
        REFERENCES users(id)
);