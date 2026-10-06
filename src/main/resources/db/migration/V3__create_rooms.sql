CREATE TABLE rooms (
                       room_id UUID PRIMARY KEY,

                       hotel_id UUID NOT NULL,

                       type VARCHAR(30) NOT NULL,

                       capacity INTEGER NOT NULL,

                       bed_count INTEGER NOT NULL,

                       price_per_night NUMERIC(10, 2) NOT NULL,

                       status VARCHAR(30) NOT NULL,

                       CONSTRAINT fk_rooms_hotel
                           FOREIGN KEY (hotel_id)
                               REFERENCES hotels(id)
                               ON DELETE CASCADE,

                       CONSTRAINT chk_room_capacity
                           CHECK (capacity > 0),

                       CONSTRAINT chk_room_bed_count
                           CHECK (bed_count > 0),

                       CONSTRAINT chk_room_price
                           CHECK (price_per_night >= 0)
);

CREATE INDEX idx_rooms_hotel_id
    ON rooms(hotel_id);