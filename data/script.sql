-- ========================
-- CATEGORY
-- ========================
INSERT INTO category (name, description) VALUES
                                             ('electronics', 'Electronic devices and gadgets'),
                                             ('fashion', 'Clothing and fashion accessories'),
                                             ('beauty', 'Cosmetics and skincare products'),
                                             ('fresh fruits', 'Fresh organic fruits');

-- ========================
-- USERS (Buyers & Sellers)
-- ========================
-- Sellers


-- Buyers


-- ========================
-- SELLER INFO
-- ========================
INSERT INTO seller (user_id, shop_name, rating)
VALUES
    (1, 'TechZone', 4.8);

-- ========================
-- BUYER INFO
-- ========================
INSERT INTO buyer (user_id)
VALUES
    (2);


-- ========================
-- PRODUCTS
-- ========================
INSERT INTO product (name, description, price, stock, imageURL, rating, category_id, seller_id)
VALUES
    ('iPhone 15 Pro', 'Apple smartphone 256GB', 1200.0, 15, 'iphone15.jpg', 4.9, 1, 1),
    ('MacBook Air M3', 'Apple laptop 13-inch', 1500.0, 10, 'macbook.jpg', 4.8, 1, 1),
    ('Lipstick Chanel', 'High-end matte lipstick', 45.0, 100, 'lipstick.jpg', 4.7, 3, 1),
    ('Fresh Mango', 'Sweet tropical mango', 3.5, 200, 'mango.jpg', 4.5, 4, 1);

-- ========================
-- CARTS
-- ========================
INSERT INTO cart (total_price, buyer_id)
VALUES
    (0, 2);

-- ========================
-- CART ITEMS
-- ========================
INSERT INTO cart_item (quantity, subtotal, cart_id, product_id)
VALUES
    (1, 1200.0, 1, 1),
    (2, 90.0, 1, 3),
    (3, 10.5, 1, 4);

-- ========================
-- ORDERS
-- ========================
INSERT INTO orders (order_date, total_amount, status, buyer_id)
VALUES
    (NOW(), 1290.0, 'PENDING', 2),
    (NOW(), 10.5, 'DELIVERED', 2);

-- ========================
-- ORDER ITEMS
-- ========================
INSERT INTO order_item (quantity, price, order_id, product_id)
VALUES
    (1, 1200.0, 1, 1),
    (2, 45.0, 1, 3),
    (3, 3.5, 2, 4);

-- ========================
-- PAYMENTS
-- ========================
INSERT INTO payment (method, amount, date, status, order_id)
VALUES
    ('CREDIT_CARD', 1290.0, NOW(), 'PENDING', 1),
    ('CASH_ON_DELIVERY', 10.5, NOW(), 'COMPLETED', 2);

-- ========================
-- REVIEWS
-- ========================
INSERT INTO review (rating, comment, date, buyer_id, product_id)
VALUES
    (5, 'Excellent phone!', NOW(), 2, 1),
    (4, 'Nice lipstick color.', NOW(), 2, 3),
    (5, 'Fresh and juicy mangoes.', NOW(), 2, 4);
