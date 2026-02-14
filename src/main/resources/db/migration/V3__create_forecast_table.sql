CREATE TABLE forecasts (
    id BIGSERIAL PRIMARY KEY,
    city_id BIGINT NOT NULL REFERENCES cities(id) ON DELETE CASCADE,
    forecast_date TIMESTAMP NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    wind_speed DOUBLE PRECISION,
    wind_direction INTEGER,
    pressure INTEGER NOT NULL,
    humidity INTEGER NOT NULL,
    weather_main VARCHAR(50),
    weather_description VARCHAR(100),
    rain_volume DOUBLE PRECISION,
    precipitation_probability DOUBLE PRECISION
);

CREATE INDEX idx_forecast_city_date ON forecasts(city_id, forecast_date);