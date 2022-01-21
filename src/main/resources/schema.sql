CREATE TABLE IF NOT EXISTS Exchange_Rate_Entity (id SERIAL PRIMARY KEY, timestamp long, exchange_Code VARCHAR(10), exchange_Rate double);

-- insert into Exchange_Rate_Entity
-- values (1000, 'USD', 10.0);