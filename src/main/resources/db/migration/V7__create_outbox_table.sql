CREATE TABLE outbox (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        aggregatetype VARCHAR(255) NOT NULL,
                        aggregateid VARCHAR(255) NOT NULL,
                        type VARCHAR(255) NOT NULL,
                        payload JSONB NOT NULL,
                        timestamp TIMESTAMPTZ NOT NULL DEFAULT now()
);