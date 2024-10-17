INSERT INTO customers (name, last_name, email, phone, birth_date) VALUES
('John', 'Doe', 'john.doe@example.com', 123456789, '1980-01-01');

INSERT INTO products (name, description, price, type) VALUES
('Laptop', 'High-end gaming laptop', 1200.00, 'ELECTRONIC'),
('Fiction Book', 'A thrilling fiction novel', 15.99, 'LIBRARY'),
('Headphones', 'Noise-cancelling headphones', 199.99, 'ELECTRONIC');

INSERT INTO carts (customer_id, status) VALUES
(1, 'DRAFT');