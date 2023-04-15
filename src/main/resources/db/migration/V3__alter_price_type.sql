ALTER TABLE public.menu_item ALTER COLUMN price TYPE decimal USING price::decimal;
ALTER TABLE public."order" ALTER COLUMN price TYPE decimal USING price::decimal;
ALTER TABLE public.order_item ALTER COLUMN price TYPE decimal USING price::decimal;

