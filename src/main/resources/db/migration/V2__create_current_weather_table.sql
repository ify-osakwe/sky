CREATE TABLE current_weather (
    id BIGSERIAL PRIMARY KEY,
    city_id BIGINT NOT NULL UNIQUE REFERENCES cities(id) ON DELETE CASCADE,
    timestamp TIMESTAMP NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    humidity INTEGER NOT NULL,
    wind_speed DOUBLE PRECISION,
    wind_direction INTEGER,
    pressure INTEGER NOT NULL,
    weather_main VARCHAR(50),
    weather_description VARCHAR(100),
    sunrise TIMESTAMP,
    sunset TIMESTAMP,
    last_updated TIMESTAMP NOT NULL
);

CREATE INDEX idx_current_weather_last_updated ON current_weather(last_updated);
