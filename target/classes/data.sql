INSERT INTO product (id, name, price, stock) VALUES 
(1, 'Oreo', 1, 100),
(2, 'Pepsi', 3, 100),
(3, 'Hershey', 2, 100);

INSERT INTO users (id, username, password, full_name) VALUES
(1, 'edwin@gmail.com', '123', 'Edwin Aguilar');

INSERT INTO address (id, address, user_id) VALUES
(1, 'Calle A', 1),
(2, 'Avenida B', 1);

INSERT INTO payment_method (id, funds, user_id) VALUES
(1, 1000, 1),
(2, 500, 1);

INSERT INTO credit_card (id, credit_card_number, cvc, exp_date) VALUES
(1, '1234 5678 9876 5432', '123', '05/24');

INSERT INTO bank_account (id, account_number) VALUES
(2,'123 45678912345');

--INSERT INTO payment_method_users (payment_methods_id, users_id) VALUES
--(1, 1),
--(2, 1);