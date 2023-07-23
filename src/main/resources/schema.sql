CREATE TABLE IF NOT EXISTS users (
  id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS item_requests (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  description VARCHAR(255),
  requester_id INTEGER REFERENCES users (id),
  created TIMESTAMP WITHOUT TIME ZONE
);
CREATE TABLE IF NOT EXISTS items (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  is_available  BOOLEAN NOT NULL,
  owner_id INTEGER NOT NULL,
  request_id INTEGER REFERENCES item_requests (id),
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT fk_owner_id_items FOREIGN KEY (owner_id) REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS bookings (
  id INTEGER NOT NULL GENERATED BY DEFAULT AS  IDENTITY PRIMARY KEY UNIQUE,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id  INTEGER NOT NULL,
  booker_id INTEGER NOT NULL,
  status VARCHAR(64) NOT NULL,
  CONSTRAINT fk_item_id_bookings FOREIGN KEY (item_id) REFERENCES items (id),
  CONSTRAINT fk_booker_id_bookings FOREIGN KEY (booker_id) REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS comments (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(512) NOT NULL,
  item_id  INTEGER NOT NULL,
  author_id INTEGER NOT NULL,
  CONSTRAINT pk_comment PRIMARY KEY (id),
  CONSTRAINT fk_item_id_comments FOREIGN KEY (item_id) REFERENCES items (id),
  CONSTRAINT fk_author_id_comments FOREIGN KEY (author_id) REFERENCES users (id)
);