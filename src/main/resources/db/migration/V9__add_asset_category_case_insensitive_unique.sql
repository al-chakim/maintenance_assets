CREATE UNIQUE INDEX uq_asset_categories_name_lower
ON asset_categories (LOWER(name));