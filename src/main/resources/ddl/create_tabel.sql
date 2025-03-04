CREATE TABLE book (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    stock INT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE borrower (
    id SERIAL PRIMARY KEY,
    nik VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT null UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE loan (
    id SERIAL PRIMARY KEY,
    nik_borrower VARCHAR(255) NOT NULL,
    isbn_book VARCHAR(255) NOT NULL,
    loan_date DATE NOT NULL,
    loan_deadline DATE NOT NULL,
    return_date DATE,
    status_return VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);