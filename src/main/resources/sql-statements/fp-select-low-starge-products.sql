SELECT DISTINCT p.name AS 'product_name'
FROM products p,
     storage_items item
WHERE item.product_id = p.id
  AND item.count < 1
UNION
SELECT p.name
FROM products p
WHERE p.id
          NOT IN
      (SELECT product_id FROM storage_items);
