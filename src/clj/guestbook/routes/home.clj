(ns guestbook.routes.home
  (:require [guestbook.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [guestbook.db.core :as db]
            [struct.core :as st]
            [clojure.tools.logging :as log]))


(defn home-page [{:keys [flash]}]
  (let [_ (log/info "flash is" flash)
        _ (log/info "db/get-messages is " (db/get-messages))]
    (layout/render
     "home.html"
     (merge {:messages (db/get-messages)}
            (select-keys flash [:name :description :askingprice :producturl :image :errors])))))

(def message-schema
  [[:name st/required st/string]
   [:description
    st/required
    st/string
    {:message "description must contain at least 10 characters"
     :validate #(> (count %) 9)}]])

(defn validate-message [params]
  (first
    (st/validate params message-schema)))

(defn save-message! [{:keys [params]}]
  (if-let [errors (validate-message params)]
    (-> (response/found "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/save-message!
        (assoc params :timestamp (java.util.Date.)))
      (response/found "/"))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" request (home-page request))
  (POST "/" request (save-message! request))
  (GET "/about" [] (about-page)))
