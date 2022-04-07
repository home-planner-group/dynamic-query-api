SELECT c.name AS 'cart_name'
FROM carts c
         JOIN user_carts uc on c.id = uc.cart_id
WHERE uc.user_id = 'FelixDB2';
