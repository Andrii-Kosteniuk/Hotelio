CREATE TABLE hotels (
                        id UUID PRIMARY KEY,
                        name VARCHAR(200) NOT NULL,
                        description TEXT NOT NULL,

                        country VARCHAR(100) NOT NULL,
                        city VARCHAR(100) NOT NULL,
                        district VARCHAR(100) NOT NULL,
                        street VARCHAR(100) NOT NULL,
                        building_number INT NOT NULL,
                        zip_code VARCHAR(100) NOT NULL,

                        star_rating INTEGER NOT NULL,
                        status VARCHAR(30) NOT NULL,

                        created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                        updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

                        CONSTRAINT chk_hotel_star_rating
                            CHECK (star_rating BETWEEN 1 AND 5)
);