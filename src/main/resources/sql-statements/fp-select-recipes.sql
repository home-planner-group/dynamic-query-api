SELECT r.name     AS 'recipe_name',
       r.category AS 'recipe_category',
       item.count AS 'item_count',
       p.unit     AS 'item_unit',
       p.name     AS 'product_name'
FROM recipes r
         JOIN recipe_items item ON r.id = item.recipe_id
         JOIN products p on item.product_id = p.id
WHERE r.category = 'Pasta';
