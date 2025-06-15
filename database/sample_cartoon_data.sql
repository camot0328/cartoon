INSERT INTO category (id, name) VALUES (1, 'SF');
INSERT INTO category (id, name) VALUES (2, '추리');
INSERT INTO category (id, name) VALUES (3, '호러');

INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating) VALUES (
1, 'SF 우주전쟁', '허버트 조지 웰스', 'SF출판사', '2020-05-01', 12000, 'https://image.aladin.co.kr/img1.jpg', 'https://image.aladin.co.kr/detail1.jpg', 9.2
);
INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating) VALUES (
2, '시간여행자의 아내', '오드리 니페네거', '타임북스', '2018-08-21', 13500, 'https://image.aladin.co.kr/img2.jpg', 'https://image.aladin.co.kr/detail2.jpg', 8.7
);
INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating) VALUES (
3, '셜록 홈즈', '아서 코난 도일', '미스터리북스', '2010-02-01', 9800, 'https://image.aladin.co.kr/img3.jpg', 'https://image.aladin.co.kr/detail3.jpg', 9.5
);
INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating) VALUES (
4, '그리고 아무도 없었다', '애거서 크리스티', '황금가지', '2005-01-15', 11000, 'https://image.aladin.co.kr/img4.jpg', 'https://image.aladin.co.kr/detail4.jpg', 9.3
);
INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating) VALUES (
5, '그것', '스티븐 킹', '호러출판사', '2012-10-31', 15000, 'https://image.aladin.co.kr/img5.jpg', 'https://image.aladin.co.kr/detail5.jpg', 8.9
);
INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating) VALUES (
6, '셔터 아일랜드', '데니스 루헤인', '문학세계사', '2009-06-22', 12300, 'https://image.aladin.co.kr/img6.jpg', 'https://image.aladin.co.kr/detail6.jpg', 8.5
);

INSERT INTO product_category (product_id, category_id) VALUES (1, 1);
INSERT INTO product_category (product_id, category_id) VALUES (2, 1);
INSERT INTO product_category (product_id, category_id) VALUES (3, 2);
INSERT INTO product_category (product_id, category_id) VALUES (4, 2);
INSERT INTO product_category (product_id, category_id) VALUES (5, 3);
INSERT INTO product_category (product_id, category_id) VALUES (6, 3);

INSERT INTO stock (product_id, quantity, state) VALUES (1, 100, '판매중');
INSERT INTO stock (product_id, quantity, state) VALUES (2, 100, '판매중');
INSERT INTO stock (product_id, quantity, state) VALUES (3, 100, '판매중');
INSERT INTO stock (product_id, quantity, state) VALUES (4, 100, '판매중');
INSERT INTO stock (product_id, quantity, state) VALUES (5, 100, '판매중');
INSERT INTO stock (product_id, quantity, state) VALUES (6, 100, '판매중');