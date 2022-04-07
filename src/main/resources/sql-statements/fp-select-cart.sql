SELECT c.name     AS cart_name,
       item.count AS item_count,
       p.unit     AS item_unit,
       p.name     AS product_name,
       p.category AS product_category
FROM carts c
         JOIN cart_items item on c.id = item.cart_id
         JOIN products p on item.product_id = p.id
WHERE c.id = 12345678;
