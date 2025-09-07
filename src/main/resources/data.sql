INSERT INTO products (name, description, price, stock_quantity, image_url) VALUES ('Milk', 'Mother Dairy - 500 ml Pouch', 35.00, 50, 'https://placehold.co/600x400');
INSERT INTO products (name, description, price, stock_quantity, image_url) VALUES ('Bread', 'Harvest Gold 100% Whole Wheat - 500 g', 55.00, 50, 'https://placehold.co/600x400');
INSERT INTO products (name, description, price, stock_quantity, image_url) VALUES ('Cheese', 'Mother Dairy - 10 Slices', 100.00, 50, 'https://placehold.co/600x400');
INSERT INTO products (name, description, price, stock_quantity, image_url) VALUES ('Butter', 'Amul Salted - 200 g Brick', 35.00, 50, 'https://placehold.co/600x400');
INSERT INTO products (name, description, price, stock_quantity, image_url) VALUES ('Eggs', '1 Crate (30 eggs)', 35.00, 50, 'https://placehold.co/600x400');

-- Ensure version is non-null to keep Hibernate happy
UPDATE products SET version = 0 WHERE version IS NULL;

INSERT INTO users (id, username, password, email, role, is_super_admin, created_at) VALUES
+  ('00000000-0000-0000-0000-000000000001', 'superadmin', '$2a$12$4L3PlSSdbSXSkSJZIpeQl.San2p9Juw4EKF3/Tn0pcx3pKrdS1D.q', 'superadmin@example.com', 'ADMIN', TRUE, CURRENT_TIMESTAMP());
INSERT INTO users (userName, password, email, user_role) VALUES ('thisisamish', '$2a$12$4L3PlSSdbSXSkSJZIpeQl.San2p9Juw4EKF3/Tn0pcx3pKrdS1D.q', 'thisisamish@example.com', 'CUSTOMER');
