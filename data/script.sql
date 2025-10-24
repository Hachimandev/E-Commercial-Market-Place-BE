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
INSERT INTO users (username, password, full_name, phone, address, role)
VALUES
    ('seller1', '123456', 'Nguyen Van A', '0901111111', 'Hanoi', 'SELLER'),
    ('seller2', '123456', 'Tran Thi B', '0902222222', 'HCM City', 'SELLER');

-- Buyers
INSERT INTO users (username, password, full_name, phone, address, role)
VALUES
    ('buyer1', '123456', 'Le Van C', '0903333333', 'Danang', 'BUYER'),
    ('buyer2', '123456', 'Pham Thi D', '0904444444', 'Can Tho', 'BUYER');

-- ========================
-- SELLER INFO
-- ========================
INSERT INTO seller (user_id, shop_name, rating)
VALUES
    (1, 'TechZone', 4.8),
    (2, 'BeautyShop', 4.6);

-- ========================
-- BUYER INFO
-- ========================
INSERT INTO buyer (user_id)
VALUES
    (3),
    (4);

-- ========================
-- PRODUCTS
-- ========================
INSERT INTO product (name, description, price, stock, imageURL, rating, category_id, seller_id)
VALUES
    ('iPhone 15 Pro', 'Apple smartphone 256GB', 1200.0, 15, 'iphone15.jpg', 4.9, 1, 1),
    ('MacBook Air M3', 'Apple laptop 13-inch', 1500.0, 10, 'macbook.jpg', 4.8, 1, 1),
    ('Lipstick Chanel', 'High-end matte lipstick', 45.0, 100, 'lipstick.jpg', 4.7, 3, 2),
    ('Fresh Mango', 'Sweet tropical mango', 3.5, 200, 'mango.jpg', 4.5, 4, 2);

-- ========================
-- CARTS
-- ========================
INSERT INTO cart (total_price, buyer_id)
VALUES
    (0, 3),
    (0, 4);

-- ========================
-- CART ITEMS
-- ========================
INSERT INTO cart_item (quantity, subtotal, cart_id, product_id)
VALUES
    (1, 1200.0, 1, 1),
    (2, 90.0, 1, 3),
    (3, 10.5, 2, 4);

-- ========================
-- ORDERS
-- ========================
INSERT INTO orders (order_date, total_amount, status, buyer_id)
VALUES
    (NOW(), 1290.0, 'PENDING', 3),
    (NOW(), 10.5, 'DELIVERED', 4);

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
    (5, 'Excellent phone!', NOW(), 3, 1),
    (4, 'Nice lipstick color.', NOW(), 3, 3),
    (5, 'Fresh and juicy mangoes.', NOW(), 4, 4);
