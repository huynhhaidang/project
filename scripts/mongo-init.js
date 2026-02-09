db = db.getSiblingDB('shopdb');

db.categories.insertMany([
  { name: 'Điện thoại', description: 'Các dòng smartphone mới nhất' },
  { name: 'Laptop', description: 'Laptop cho học tập và làm việc' },
  { name: 'Phụ kiện', description: 'Tai nghe, sạc, pin dự phòng' }
]);

// Điện thoại (giá tham khảo từ CellphoneS, 02/2026)
db.products.insertMany([
  {
    name: 'iPhone 15 Pro 128GB',
    brand: 'Apple',
    categoryName: 'Điện thoại',
    price: NumberDecimal('23990000'),
    description: 'Phiên bản 128GB chính hãng VN/A.',
    imageUrl: '/theme/images/card-item1.jpg',
    stock: 20,
    active: true
  },
  {
    name: 'iPhone 15 Pro 256GB',
    brand: 'Apple',
    categoryName: 'Điện thoại',
    price: NumberDecimal('26290000'),
    description: 'Phiên bản 256GB chính hãng VN/A.',
    imageUrl: '/theme/images/card-item2.jpg',
    stock: 18,
    active: true
  },
  {
    name: 'iPhone 15 Pro 512GB',
    brand: 'Apple',
    categoryName: 'Điện thoại',
    price: NumberDecimal('29490000'),
    description: 'Phiên bản 512GB chính hãng VN/A.',
    imageUrl: '/theme/images/card-item3.jpg',
    stock: 12,
    active: true
  },
  {
    name: 'Samsung Galaxy S24 8GB 256GB',
    brand: 'Samsung',
    categoryName: 'Điện thoại',
    price: NumberDecimal('16790000'),
    description: 'Màn hình 6.2", Dynamic AMOLED 2X, 120Hz.',
    imageUrl: '/theme/images/card-item4.jpg',
    stock: 22,
    active: true
  },
  {
    name: 'Xiaomi 14 12GB 256GB',
    brand: 'Xiaomi',
    categoryName: 'Điện thoại',
    price: NumberDecimal('18990000'),
    description: 'Thiết kế nhỏ gọn, hiệu năng cao.',
    imageUrl: '/theme/images/card-item5.jpg',
    stock: 16,
    active: true
  },
  {
    name: 'Xiaomi 14T 12GB 512GB',
    brand: 'Xiaomi',
    categoryName: 'Điện thoại',
    price: NumberDecimal('11790000'),
    description: 'Phiên bản 14T 12GB/512GB.',
    imageUrl: '/theme/images/card-item6.jpg',
    stock: 14,
    active: true
  },
  {
    name: 'Xiaomi 14T Pro 12GB 512GB',
    brand: 'Xiaomi',
    categoryName: 'Điện thoại',
    price: NumberDecimal('14390000'),
    description: 'Phiên bản Pro cấu hình cao.',
    imageUrl: '/theme/images/card-item7.jpg',
    stock: 10,
    active: true
  },
  {
    name: 'Xiaomi 14 Ultra 5G 16GB 512GB',
    brand: 'Xiaomi',
    categoryName: 'Điện thoại',
    price: NumberDecimal('24090000'),
    description: 'Phiên bản Ultra camera cao cấp.',
    imageUrl: '/theme/images/card-item8.jpg',
    stock: 8,
    active: true
  },
  {
    name: 'OPPO Reno14 F 5G 8GB 256GB',
    brand: 'OPPO',
    categoryName: 'Điện thoại',
    price: NumberDecimal('10100000'),
    description: 'Phân khúc tầm trung, pin lớn.',
    imageUrl: '/theme/images/card-item9.jpg',
    stock: 20,
    active: true
  },
  {
    name: 'Xiaomi Redmi 14C 4GB 128GB',
    brand: 'Xiaomi',
    categoryName: 'Điện thoại',
    price: NumberDecimal('2850000'),
    description: 'Giá dễ tiếp cận, pin lớn 5160mAh.',
    imageUrl: '/theme/images/card-item10.jpg',
    stock: 30,
    active: true
  }
]);

// Laptop (giá tham khảo từ CellphoneS, 02/2026)
db.products.insertMany([
  {
    name: 'MacBook Air M1 256GB 2020',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('15990000'),
    description: 'Mỏng nhẹ, pin lâu, phù hợp học tập.',
    imageUrl: '/theme/images/card-large-item1.jpg',
    stock: 12,
    active: true
  },
  {
    name: 'MacBook Air M2 2024 16GB 256GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('19490000'),
    description: 'Hiệu năng mạnh hơn M1, thiết kế mới.',
    imageUrl: '/theme/images/card-large-item2.jpg',
    stock: 10,
    active: true
  },
  {
    name: 'MacBook Air M2 2024 16GB 512GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('26990000'),
    description: 'Dung lượng lớn, đa nhiệm tốt.',
    imageUrl: '/theme/images/card-large-item3.jpg',
    stock: 8,
    active: true
  },
  {
    name: 'MacBook Air M2 2024 16GB 256GB (Sạc 30W)',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('19690000'),
    description: 'Phiên bản tối ưu cho văn phòng.',
    imageUrl: '/theme/images/card-large-item4.jpg',
    stock: 9,
    active: true
  },
  {
    name: 'MacBook Air M3 13 inch 16GB 256GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('24190000'),
    description: 'Chip M3 thế hệ mới, siêu tiết kiệm pin.',
    imageUrl: '/theme/images/card-large-item5.jpg',
    stock: 10,
    active: true
  },
  {
    name: 'MacBook Air M3 13 inch 16GB 512GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('26990000'),
    description: 'Tùy chọn 512GB phù hợp đồ họa nhẹ.',
    imageUrl: '/theme/images/card-large-item6.jpg',
    stock: 7,
    active: true
  },
  {
    name: 'MacBook Air M3 15 inch 16GB 512GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('29990000'),
    description: 'Màn hình lớn 15 inch, trải nghiệm rộng.',
    imageUrl: '/theme/images/card-large-item7.jpg',
    stock: 6,
    active: true
  },
  {
    name: 'MacBook Air M3 15 inch 24GB 512GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('30490000'),
    description: 'RAM 24GB cho tác vụ nặng.',
    imageUrl: '/theme/images/card-large-item8.jpg',
    stock: 5,
    active: true
  },
  {
    name: 'MacBook Pro 14 inch M3 Max 96GB 512GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('89990000'),
    description: 'Hiệu năng cao cấp cho chuyên nghiệp.',
    imageUrl: '/theme/images/card-large-item9.jpg',
    stock: 3,
    active: true
  },
  {
    name: 'MacBook Air M3 15 inch 16GB 256GB',
    brand: 'Apple',
    categoryName: 'Laptop',
    price: NumberDecimal('26990000'),
    description: 'Tùy chọn tiết kiệm hơn của Air 15 inch.',
    imageUrl: '/theme/images/card-large-item10.jpg',
    stock: 6,
    active: true
  }
]);

// Phụ kiện (giá tham khảo từ CellphoneS)
db.products.insertMany([
  {
    name: 'Củ sạc nhanh Anker 2 cổng 1C1A 20W A2348',
    brand: 'Anker',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('190000'),
    description: 'Sạc nhanh 20W, 2 cổng USB-C/USB-A.',
    imageUrl: '/theme/images/collection-item1.jpg',
    stock: 50,
    active: true
  },
  {
    name: 'Củ sạc Anker Zolo 1C 20W A2699',
    brand: 'Anker',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('200000'),
    description: 'Sạc nhanh 20W, cổng USB-C.',
    imageUrl: '/theme/images/collection-item2.jpg',
    stock: 50,
    active: true
  },
  {
    name: 'Sạc Anker PowerPort III 1C 20W A2149',
    brand: 'Anker',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('135000'),
    description: 'Sạc nhanh 20W, thiết kế nhỏ gọn.',
    imageUrl: '/theme/images/collection-item4.jpg',
    stock: 60,
    active: true
  },
  {
    name: 'Củ sạc Anker PowerPort III Nano 20W A2633',
    brand: 'Anker',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('210000'),
    description: 'Sạc nhanh 20W, công nghệ MultiProtect.',
    imageUrl: '/theme/images/collection-item1.jpg',
    stock: 55,
    active: true
  },
  {
    name: 'Sạc Anker 1C 20W A2347',
    brand: 'Anker',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('140000'),
    description: 'Sạc nhanh 20W, PowerIQ.',
    imageUrl: '/theme/images/collection-item2.jpg',
    stock: 60,
    active: true
  },
  {
    name: 'Pin sạc dự phòng Anker 511 Powercore Fusion 5000mAh 20W',
    brand: 'Anker',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('430000'),
    description: 'Pin dự phòng tích hợp sạc 20W.',
    imageUrl: '/theme/images/collection-item4.jpg',
    stock: 40,
    active: true
  },
  {
    name: 'Pin sạc dự phòng Baseus Bipow 10000mAh 20W LCD',
    brand: 'Baseus',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('375000'),
    description: 'Pin 10000mAh, màn hình LCD, sạc nhanh 20W.',
    imageUrl: '/theme/images/collection-item1.jpg',
    stock: 45,
    active: true
  },
  {
    name: 'Pin sạc dự phòng Baseus Bipow 10000mAh 15W LCD',
    brand: 'Baseus',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('350000'),
    description: 'Pin 10000mAh, sạc nhanh 15W.',
    imageUrl: '/theme/images/collection-item2.jpg',
    stock: 45,
    active: true
  },
  {
    name: 'Pin sạc dự phòng Baseus Bipow 20000mAh 15W LCD',
    brand: 'Baseus',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('300000'),
    description: 'Pin 20000mAh, sạc 15W.',
    imageUrl: '/theme/images/collection-item4.jpg',
    stock: 35,
    active: true
  },
  {
    name: 'Pin sạc dự phòng Baseus Qpow Pro 20000mAh 20W',
    brand: 'Baseus',
    categoryName: 'Phụ kiện',
    price: NumberDecimal('690000'),
    description: 'Pin 20000mAh, sạc nhanh 20W, kèm cáp Type-C.',
    imageUrl: '/theme/images/collection-item1.jpg',
    stock: 30,
    active: true
  }
]);
