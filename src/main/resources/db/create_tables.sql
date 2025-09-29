DROP TABLE IF EXISTS jobs CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS locations CASCADE;

CREATE TABLE companies (
                           id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                           name VARCHAR(255) NOT NULL UNIQUE,
                           website VARCHAR(500)
);

CREATE TABLE locations (
                           id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                           city VARCHAR(255) NOT NULL,
                           state VARCHAR(255),
                           country VARCHAR(255),
                           CONSTRAINT unique_location UNIQUE(city, state, country)
);

CREATE TABLE jobs (
                      id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                      title VARCHAR(255),
                      company_id BIGINT NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
                      location_id BIGINT NOT NULL REFERENCES locations(id) ON DELETE CASCADE,
                      salary INTEGER,
                      type VARCHAR(50),
                      job_url VARCHAR(1000),
                      source VARCHAR(255)
);