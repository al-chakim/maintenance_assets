CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    asset_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    category_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    description TEXT,
    location VARCHAR(150),
    purchase_date DATE,
    purchase_price NUMERIC(15,2),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_assets_category_id
        FOREIGN KEY (category_id)
        REFERENCES asset_categories(id),

    CONSTRAINT fk_assets_created_by
        FOREIGN KEY (created_by)
        REFERENCES users(id)
);