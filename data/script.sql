-- ========================
-- CATEGORY
-- ========================
INSERT INTO category (name, description) VALUES
('electronics', 'Các thiết bị điện tử'),
('fashion', 'Quần áo và phụ kiện thời trang'),
('beauty', 'Sản phẩm mỹ phẩm và chăm sóc da'),
('fresh fruits', 'Trái cây tươi sạch');

-- ========================
-- USERS (Buyers & Sellers)
-- ========================
-- Seller (User ID 1)
INSERT INTO users (username, password, full_name, phone, address, avatarurl, role) VALUES
('techseller', 'hashed_pass_123', 'TechZone Admin', '0900000001', '123 Tech St, HCMC', 'https://randomuser.me/api/portraits/men/1.jpg', 'SELLER');
-- Buyer (User ID 2)
INSERT INTO users (username, password, full_name, phone, address, avatarurl, role) VALUES
('janedoe', 'hashed_pass_123', 'Jane Doe', '0900000002', '456 Buyer Ave, HCMC', 'https://randomuser.me/api/portraits/women/44.jpg', 'BUYER');
-- Buyer (User ID 3) - Dùng cho review
INSERT INTO users (username, password, full_name, phone, address, avatarurl, role) VALUES
('jevonray', 'hashed_pass_123', 'Jevon Raynor', '0900000003', '789 Review St, HCMC', 'https://randomuser.me/api/portraits/men/32.jpg', 'BUYER');
-- Buyer (User ID 4) - Dùng cho review
INSERT INTO users (username, password, full_name, phone, address, avatarurl, role) VALUES
('jasond', 'hashed_pass_123', 'Jason D.', '0900000004', '101 Comment Rd, HCMC', 'https://randomuser.me/api/portraits/men/33.jpg', 'BUYER');


-- ========================
-- SELLER INFO
-- ========================
INSERT INTO seller (user_id, shop_name, rating)
VALUES (1, 'TechZone', 4.8);

-- ========================
-- BUYER INFO
-- ========================
INSERT INTO buyer (user_id) VALUES (2), (3), (4);


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
    (NOW(), 10.5, 'DELIVERED', 2),
    (NOW(), 107.5, 'SHIPPED', 2);

-- ========================
-- ORDER ITEMS
-- ========================
INSERT INTO order_item (quantity, price, order_id, product_id)
VALUES
    (1, 1200.0, 1, 1),
    (2, 45.0, 1, 3),
    (3, 3.5, 2, 4),
    (1, 20.0, 3, 5),
    (1, 65.0, 3, 6),
    (1, 22.5, 3, 8);

-- ========================
-- PAYMENTS
-- ========================
INSERT INTO payment (method, amount, date, status, order_id)
VALUES
    ('CREDIT_CARD', 1290.0, NOW(), 'PENDING', 1),
    ('CASH_ON_DELIVERY', 10.5, NOW(), 'COMPLETED', 2)
-- ========================
-- PRODUCTS
-- ========================
-- ID 1: General (iPhone)
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (1, 'iPhone 15 Pro', 'Điện thoại Apple 256GB - hiệu năng mạnh mẽ', 1200.0, 15, 'iphone15.jpg', 4.9, 1, 1, NULL);

-- ID 2: General (MacBook)
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (2, 'MacBook Air M3', 'Laptop Apple 13-inch, chip M3, mỏng nhẹ', 1500.0, 10, 'macbook.jpg', 4.8, 1, 1, 'Free Mouse');

-- ID 3: Variant (Lipstick - Color only)
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (3, 'Lipstick Chanel', 'Son lì cao cấp Chanel, màu đỏ quyến rũ', 45.0, 100, 'lipstick.jpg', 4.7, 3, 1, 'Buy 3 for 2');

-- ID 4: Simple (Mango)
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (4, 'Fresh Mango', 'Xoài chín tự nhiên, ngọt thanh và mọng nước', 3.5, 200, 'mango.jpg', 4.5, 4, 1, NULL);

-- ID 5: Variant (T-Shirt - Color + Size)
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (5, 'Men T-Shirt', 'Áo thun nam cotton mềm mịn, thoáng mát', 20.0, 50, 'men_tshirt.jpg', 4.6, 2, 1, 'Buy 1 get 1');

-- ID 6: Variant (Running Shoes - Color + Size)
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (6, 'Men Running Shoes', 'Giày chạy bộ thể thao nam, đế êm ái', 85.0, 40, 'headphone1.png', 4.7, 2, 1, NULL); -- (Dùng tạm ảnh headphone1)

-- ID 7 & 8: Simple (Headphones - Dùng cho Relevant Products)
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (7, 'Sony WH-1000XM4', 'Tai nghe chống ồn chủ động cao cấp', 350.0, 25, 'headphone1.png', 4.9, 1, 1, NULL);
INSERT INTO product (product_id, name, description, price, stock, imageURL, rating, category_id, seller_id, offer)
VALUES (8, 'AirPods Max', 'Tai nghe Apple cao cấp, âm thanh không gian', 549.0, 15, 'headphone2.png', 4.8, 1, 1, NULL);


-- ========================
-- PRODUCT_IMAGE (Ảnh phụ cho sản phẩm biến thể)
-- (Sử dụng lại tên file ảnh local)
-- ========================

-- Ảnh phụ cho Lipstick (ID 3)
INSERT INTO product_image (imageURL, product_id) VALUES
('lipstick.jpg', 3), -- (Dùng lặp lại ảnh)
('facecream.jpg', 3), -- (Dùng tạm ảnh khác)
('lipstick.jpg', 3);

-- Ảnh phụ cho T-Shirt (ID 5)
INSERT INTO product_image (imageURL, product_id) VALUES
('men_tshirt.jpg', 5), -- (Dùng lặp lại ảnh)
('men_tshirt.jpg', 5),
('men_tshirt.jpg', 5);

-- Ảnh phụ cho Running Shoes (ID 6)
INSERT INTO product_image (imageURL, product_id) VALUES
('headphone1.png', 6), -- (Dùng lặp lại ảnh)
('headphone2.png', 6); -- (Dùng tạm ảnh khác)

-- ========================
-- PRODUCT_FEATURE (Tính năng cho sản phẩm General)
-- (Giữ nguyên)
-- ========================

-- Features cho iPhone (ID 1)
INSERT INTO product_feature (text, icon, product_id) VALUES
('Express', 'rocket-outline', 1),
('30-day free return', 'refresh-outline', 1),
('Authorized shop', 'shield-checkmark-outline', 1),
('Good review', 'star-outline', 1);

-- Features cho MacBook (ID 2)
INSERT INTO product_feature (text, icon, product_id) VALUES
('M3 Chip', 'hardware-chip-outline', 2),
('18hr Battery', 'battery-full-outline', 2),
('Liquid Retina', 'eye-outline', 2),
('Free Shipping', 'bus-outline', 2);

-- ========================
-- PRODUCT_OPTION (Loại biến thể: Color, Size)
-- (Giữ nguyên)
-- ========================

-- Options cho Lipstick (ID 3) - CHỈ CÓ MÀU
INSERT INTO product_option (name, product_id) VALUES ('Color', 3); -- Giả sử ID = 1

-- Options cho T-Shirt (ID 5) - CÓ MÀU VÀ SIZE
INSERT INTO product_option (name, product_id) VALUES ('Color', 5); -- Giả sử ID = 2
INSERT INTO product_option (name, product_id) VALUES ('Size', 5);  -- Giả sử ID = 3

-- Options cho Running Shoes (ID 6) - CÓ MÀU VÀ SIZE
INSERT INTO product_option (name, product_id) VALUES ('Color', 6); -- Giả sử ID = 4
INSERT INTO product_option (name, product_id) VALUES ('Size (US)', 6);  -- Giả sử ID = 5

-- ========================
-- PRODUCT_OPTION_VALUE (Giá trị của biến thể)
-- (Giữ nguyên)
-- ========================

-- Values cho "Color" của Lipstick (ID Option = 1)
INSERT INTO product_option_value (value, metadata, option_id) VALUES
('Ruby Red', '{"code": "#8B0000"}', 1),
('Nude Beige', '{"code": "#C19A6B"}', 1),
('Classic Red', '{"code": "#D22B2B"}', 1),
('Baby Pink', '{"code": "#FFC0CB"}', 1);

-- Values cho "Color" của T-Shirt (ID Option = 2)
INSERT INTO product_option_value (value, metadata, option_id) VALUES
('White', '{"code": "#FFFFFF"}', 2),
('Dark Blue', '{"code": "#00008B"}', 2),
('Black', '{"code": "#000000"}', 2);

-- Values cho "Size" của T-Shirt (ID Option = 3)
INSERT INTO product_option_value (value, metadata, option_id) VALUES
('S', NULL, 3),
('M', NULL, 3),
('L', NULL, 3),
('XL', NULL, 3);

-- Values cho "Color" của Running Shoes (ID Option = 4)
INSERT INTO product_option_value (value, metadata, option_id) VALUES
('Red/White', '{"code": "#FF0000"}', 4),
('Blue', '{"code": "#0000FF"}', 4),
('Black', '{"code": "#000000"}', 4);

-- Values cho "Size (US)" của Running Shoes (ID Option = 5)
INSERT INTO product_option_value (value, metadata, option_id) VALUES
('9', NULL, 5),
('9.5', NULL, 5),
('10', NULL, 5),
('11', NULL, 5);

-- ========================
-- RELEVANT_PRODUCTS (Sản phẩm liên quan)
-- (Giữ nguyên)
-- ========================

-- iPhone (ID 1) liên quan đến MacBook (ID 2) và AirPods (ID 8)
INSERT INTO relevant_products (product_id, relevant_product_id) VALUES
(1, 2),
(1, 8);

-- MacBook (ID 2) liên quan đến iPhone (ID 1) và Tai nghe Sony (ID 7)
INSERT INTO relevant_products (product_id, relevant_product_id) VALUES
(2, 1),
(2, 7);

-- T-Shirt (ID 5) liên quan đến Giày (ID 6)
INSERT INTO relevant_products (product_id, relevant_product_id) VALUES
(5, 6);

-- ========================
-- REVIEWS
-- (Giữ nguyên - Đã sửa lỗi cú pháp)
-- ========================
INSERT INTO review (rating, comment, date, buyer_id, product_id)
VALUES
    (5, 'Điện thoại tuyệt vời, chạy mượt và chụp hình siêu nét!', NOW(), 2, 1),
    (5, 'Deserunt minim incididunt cillum', NOW(), 3, 1),
    (4, 'Magna pariatur sit et ullamco paria', NOW(), 4, 1),

    (4, 'Màu son đẹp, hợp với mọi tông da, tuy nhiên hơi khô môi.', NOW(), 2, 3),
    (5, 'Màu Nude Beige rất tây, tôi rất thích!', NOW(), 3, 3),

    (5, 'Xoài rất ngọt và tươi, giao hàng nhanh.', NOW(), 4, 4),

    (5, 'Áo thun chất liệu tốt, mặc rất thoải mái.', NOW(), 2, 5),
    (4, 'Form áo ổn, nhưng màu xanh hơi tối hơn ảnh.', NOW(), 4, 5),

    (5, 'Giày chạy rất êm, nhẹ, đáng tiền.', NOW(), 3, 6);
    
-- ========================
-- PAYMENT_METHOD (Thẻ/Ví đã lưu của Buyer)
-- ========================

-- Giả sử Buyer ID 2 (Jane Doe) có 3 thẻ
cart_itemcart_item