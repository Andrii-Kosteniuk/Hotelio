ALTER TABLE hotels
    ADD COLUMN approved_by UUID,
    ADD COLUMN approved_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

ALTER TABLE hotels
    ADD CONSTRAINT chk_hotel_status
        CHECK (status IN ('PENDING_REVIEW', 'ACTIVE', 'REJECTED', 'SUSPENDED'));

CREATE INDEX idx_hotels_status ON hotels (status);

CREATE INDEX idx_hotels_status_created_at ON hotels (status, created_at);

ALTER TABLE hotels
    ALTER COLUMN created_at SET DEFAULT now(),
    ALTER COLUMN updated_at SET DEFAULT now();
