INSERT INTO admin (name,email_id,password)
SELECT 'Shashi Teja','shashiteja.gaddameedi@gmail.com','12345678'
WHERE NOT EXISTS (SELECT * FROM admin WHERE email_id='shashiteja.gaddameedi@gmail.com');
