CREATE TABLE cart (
                          id          BIGSERIAL PRIMARY KEY,
                          product_id  BIGINT NOT NULL,
                          user_id  BIGINT NOT NULL,
                          created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE orders (
                       id            BIGSERIAL PRIMARY KEY,
                       order_number  VARCHAR(50) NOT NULL UNIQUE,
                       user_id       BIGINT NOT NULL,
                       description   VARCHAR(500),
                       status        VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                       total_price   NUMERIC(10,2) NOT NULL DEFAULT 0,
                       address_id    BIGINT NOT NULL,
                       created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE order_item (
                            id          BIGSERIAL PRIMARY KEY,
                            order_id    BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                            product_id  BIGINT NOT NULL,
                            quantity    INT NOT NULL DEFAULT 1,
                            unit_price  NUMERIC(10,2) NOT NULL,
                            created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);