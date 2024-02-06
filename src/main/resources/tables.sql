CREATE TABLE IF NOT EXISTS Currencies (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHAR(10) UNIQUE NOT NULL,
    fullName VARCHAR(100) NOT NULL,
    sign VARCHAR(5) NOT NULL
);

PRAGMA foreign_keys = ON ;
PRAGMA foreign_keys;

CREATE TABLE IF NOT EXISTS ExchangeRate (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    baseCurrencyId INTEGER REFERENCES Currencies(id),
    targetCurrencyId INTEGER REFERENCES Currencies(id),
    rate REAL,
    UNIQUE (baseCurrencyId, targetCurrencyId)
)