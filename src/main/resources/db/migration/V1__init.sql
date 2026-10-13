
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    event_date DATE,
    location VARCHAR(200),
    owner_id BIGINT NOT NULL,
    public_slug VARCHAR(255) NOT NULL UNIQUE,
    is_published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_events_owner
        FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE venue_elements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    label VARCHAR(150),
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    width DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    rotation DOUBLE DEFAULT 0,
    color VARCHAR(30),
    metadata JSON,
    CONSTRAINT fk_venue_elements_event
        FOREIGN KEY (event_id) REFERENCES events(id)
        ON DELETE CASCADE
);

INSERT INTO users (name, email, password_hash)
VALUES (
    'Admin User',
    'admin@example.com',
    'test-password'
);

INSERT INTO events (
    title, description, event_date,
    location, owner_id, public_slug
)
VALUES (
    'Tech Fest 2026',
    'Annual college technical festival',
    '2026-11-20',
    'College Campus',
    1,
    'tech-fest-2026'
);

INSERT INTO venue_elements (
    event_id, type, label, x, y,
    width, height, rotation, color
)
VALUES
(1, 'STAGE', 'Main Stage', 100, 80, 300, 100, 0, '#754d94'),
(1, 'SEATING', 'Student Seating', 100, 250, 400, 200, 0, '#CCCCCC'),
(1, 'FOOD', 'Food Stall', 600, 200, 150, 100, 0, '#FFCC00');
