-- :name save-message! :! :n
-- :doc creates a new message
INSERT INTO guestbook
(name, description, askingprice, producturl, image, timestamp)
VALUES (:name, :description, :askingprice, :producturl, :image, :timestamp)

-- :name get-messages :? :*
-- :doc selects all available messages
SELECT * from guestbook
