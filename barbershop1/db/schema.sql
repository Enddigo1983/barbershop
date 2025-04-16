CREATE TABLE services (
                          id INTEGER PRIMARY KEY,
                          name TEXT NOT NULL
);

CREATE TABLE masters (
                         id INTEGER PRIMARY KEY,
                         name TEXT NOT NULL
);

CREATE TABLE appointments (
                              id INTEGER PRIMARY KEY,
                              name TEXT NOT NULL,
                              service_id INTEGER,
                              master_id INTEGER,
                              FOREIGN KEY (service_id) REFERENCES services(id),
                              FOREIGN KEY (master_id) REFERENCES masters(id)
);

-- Optional: Insert some sample data
INSERT INTO services (name) VALUES ('Стрижка');
INSERT INTO services (name) VALUES ('Бритье');
INSERT INTO masters (name) VALUES ('Иван');
INSERT INTO masters (name) VALUES ('Алексей');