-- Таблица Limits
CREATE TABLE Limits (
                        id SERIAL PRIMARY KEY,
                        monthly_limitusd DECIMAL NOT NULL,
                        set_date_time TIMESTAMP NOT NULL,
                        category VARCHAR(255) NOT NULL
);

-- Таблица Transaction
CREATE TABLE Transaction (
                             id SERIAL PRIMARY KEY,
                             account_from VARCHAR(255) NOT NULL,
                             account_to VARCHAR(255) NOT NULL,
                             currency_short_name VARCHAR(255) NOT NULL,
                             amount DECIMAL NOT NULL,
                             date_time TIMESTAMP NOT NULL,
                             limit_exceeded BOOLEAN NOT NULL,
                             category VARCHAR(255) NOT NULL,
                             limit_id INT,
                             FOREIGN KEY (limit_id) REFERENCES Limits(id)
);

-- Таблица ExchangeRate
CREATE TABLE Exchange_rate (
                              id SERIAL PRIMARY KEY,
                              base_currency VARCHAR(255) NOT NULL,
                              target_currency VARCHAR(255) NOT NULL,
                              date DATE NOT NULL,
                              rate DECIMAL(10,5) NOT NULL
);
