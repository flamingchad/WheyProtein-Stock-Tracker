

CREATE TABLE IF NOT EXISTS product (
                                       id BIGSERIAL PRIMARY KEY,
                                       pincode VARCHAR(6) NOT NULL,
    name VARCHAR(64) NOT NULL,
    url TEXT NOT NULL,
    out_of_stock_selector TEXT,
    is_in_stock BOOLEAN,
    prev_stock_status BOOLEAN,
    last_checked TIMESTAMP
    );

-- DO NOT include `id`
INSERT INTO product (
    pincode,
    name,
    url,
    out_of_stock_selector,
    is_in_stock,
    prev_stock_status,
    last_checked
) VALUES (
             '500081',
             'Amul Whey Protein, 32 g | Pack of 60 Sachets',
             'https://shop.amul.com/en/product/amul-whey-protein-32-g-or-pack-of-60-sachets',
             '//div[contains(@class, ''alert-danger'') and text()=''Sold Out'']',
             false,
          false,
             CURRENT_TIMESTAMP
         );
