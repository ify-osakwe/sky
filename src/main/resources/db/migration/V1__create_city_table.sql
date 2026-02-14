CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(2),
    latitude DECIMAL(9, 6),
    longitude DECIMAL(9, 6),
    last_searched TIMESTAMP,
    search_count INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT uk_city_name_country UNIQUE (name, country)
);

CREATE INDEX idx_city_name ON cities(name);
CREATE INDEX idx_city_search_count ON cities(search_count DESC);