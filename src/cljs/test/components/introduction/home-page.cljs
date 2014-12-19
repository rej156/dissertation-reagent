(ns test.components.introduction.home-page
  (:require [test.session :as session :refer [global-put! global-state]]))

(defn home-page []
  [:div.home-page
   [:h1 "I'm Liz."]
        [:h2 "What is your name?*"]
        [:h3 "(first name please)"]
        [:input {:type "text"
                 :value "Please use react form to sync state to :first-name"}]
        [:br]
   [:a {:href "#/introduction/second"} "Continue"]])
