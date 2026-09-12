CREATE TABLE schema_metadata (
    id BIGSERIAL PRIMARY KEY,
    application_name VARCHAR(100) NOT NULL,
    environment VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO schema_metadata (
    application_name,
    environment
) VALUES (
    'EventPilot',
    'development'
);