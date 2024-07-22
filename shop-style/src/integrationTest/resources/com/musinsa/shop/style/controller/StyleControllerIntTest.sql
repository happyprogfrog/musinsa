-- 카테고리 생성
--CREATE TABLE category (
--    id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    code VARCHAR(255) NOT NULL UNIQUE
--);

INSERT INTO category (code) VALUES ('002'), ('001');

-- 브랜드 생성
--CREATE TABLE brand (
--    id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    alias VARCHAR(50) NOT NULL UNIQUE,
--    name VARCHAR(50) NOT NULL,
--    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--);

INSERT INTO brand (alias, name, created_at, updated_at) VALUES
('branda', 'A', '2024-07-20 23:33:42.391384', '2024-07-20 23:33:42.391384'),
('brandb', 'B', '2024-07-20 23:33:42.412565', '2024-07-20 23:33:42.412565');

-- 제품 생성
--CREATE TABLE product (
--    id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    brand_id BIGINT NOT NULL,
--    category_code VARCHAR(255) NOT NULL,
--    name VARCHAR(50) NOT NULL,
--    price BIGINT NOT NULL,
--    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--    FOREIGN KEY (brand_id) REFERENCES brand(id),
--    FOREIGN KEY (category_code) REFERENCES category(code)
--);

INSERT INTO product (brand_id, category_code, name, price, created_at, updated_at) VALUES
(1, '001', '브랜드 A의 제품 1', 11200, '2024-07-20 23:33:42.517335', '2024-07-20 23:33:42.517335'),
(1, '002', '브랜드 A의 제품 2', 5500, '2024-07-20 23:33:42.518269', '2024-07-20 23:33:42.518269'),
(2, '001', '브랜드 B의 제품 1', 10500, '2024-07-20 23:33:42.509803', '2024-07-20 23:33:42.509803'),
(2, '002', '브랜드 B의 제품 2', 5900, '2024-07-20 23:33:42.510736', '2024-07-20 23:33:42.510736');



