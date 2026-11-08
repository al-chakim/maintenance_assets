-- Normalize SUPER ADMIN role name
UPDATE roles
SET name = 'SUPER_ADMIN'
WHERE name = 'SUPER ADMIN';

-- Rename maintenance request column
ALTER TABLE maintenance_requests
RENAME COLUMN requested_id TO requested_by;

-- Rename maintenance history column
ALTER TABLE maintenance_histories
RENAME COLUMN change_by TO changed_by;

-- Rename maintenance history foreign key constraint
ALTER TABLE maintenance_histories
RENAME CONSTRAINT fk_maintenance_history_change_by
TO fk_maintenance_history_changed_by;