CREATE TABLE bookings
(
    id          UUID PRIMARY KEY,

    room_id     UUID           NOT NULL,

    check_in    DATE           NOT NULL,
    check_out   DATE           NOT NULL,
    guests      INT            NOT NULL,
    status      VARCHAR(30)    NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,

    CONSTRAINT fk_bookings_room
        FOREIGN KEY (room_id)
            REFERENCES rooms (room_id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_booking_dates
        CHECK (check_out > check_in)
);

CREATE INDEX idx_bookings_room_id
    ON bookings (room_id);

CREATE INDEX idx_bookings_room_dates
    ON bookings (room_id, check_in, check_out);

CREATE INDEX idx_bookings_room_status
    ON bookings (room_id, status);