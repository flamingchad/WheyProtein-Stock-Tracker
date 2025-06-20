INSERT INTO product (
    id,
    name,
    url,
    use_selenium,
    out_of_stock_selector,
    is_in_stock,
    last_checked
) VALUES (
             1,
             'Amul Whey Protein, 32 g | Pack of 60 Sachets',
             'https://shop.amul.com/en/product/amul-whey-protein-32-g-or-pack-of-60-sachets',
             true,
             '//div[contains(@class, ''alert-danger'') and text()=''Sold Out'']',
             false,
             CURRENT_TIMESTAMP
         );
