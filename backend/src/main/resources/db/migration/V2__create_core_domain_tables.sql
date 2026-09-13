CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venues (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    capacity INTEGER,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(30) NOT NULL,
    organizer_id BIGINT NOT NULL,
    venue_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_events_organizer
        FOREIGN KEY (organizer_id)
        REFERENCES users(id),

    CONSTRAINT fk_events_venue
        FOREIGN KEY (venue_id)
        REFERENCES venues(id)
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(12, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    available_quantity INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tickets_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    ticket_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bookings_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_bookings_ticket
        FOREIGN KEY (ticket_id)
        REFERENCES tickets(id)
);

CREATE INDEX idx_events_organizer_id
    ON events(organizer_id);

CREATE INDEX idx_events_venue_id
    ON events(venue_id);

CREATE INDEX idx_events_status
    ON events(status);

CREATE INDEX idx_tickets_event_id
    ON tickets(event_id);

CREATE INDEX idx_bookings_user_id
    ON bookings(user_id);

CREATE INDEX idx_bookings_ticket_id
    ON bookings(ticket_id);

CREATE INDEX idx_bookings_status
    ON bookings(status);