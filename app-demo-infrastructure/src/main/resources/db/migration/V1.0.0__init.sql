DROP TABLE IF EXISTS account;
CREATE TABLE account
(
    id          BIGSERIAL  PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    contract_id CHAR(15) NOT NULL,
    status      VARCHAR(20)  NOT NULL CHECK (status IN ('CREATED', 'ACTIVATED', 'DEACTIVATED')),
    info        VARCHAR(255) DEFAULT '',
    version     Integer NOT NULL DEFAULT 0,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_account_update_time ON account(update_time DESC);

DROP TABLE IF EXISTS card;
CREATE TABLE card
(
    id          BIGSERIAL  PRIMARY KEY,
    account_id  BIGINT      NULL,
    rfid        VARCHAR(255) NOT NULL,
    status      VARCHAR(20) NOT NULL CHECK (status IN ('CREATED', 'ASSIGNED', 'ACTIVATED', 'DEACTIVATED')),
    info        VARCHAR(255) DEFAULT '',
    version     Integer NOT NULL DEFAULT 0,
    create_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_card_account_update_time ON card(account_id, update_time DESC);
CREATE INDEX idx_card_update_time ON card(update_time DESC);