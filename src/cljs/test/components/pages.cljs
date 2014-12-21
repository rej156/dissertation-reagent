(ns test.components.pages
  (:require [test.session :refer [global-state]]
            [test.components.introduction.home-page :refer [home-page]]
            [test.components.introduction.second :refer [second]]
            [test.components.introduction.third :refer [third]]))

(defn page []
  (condp = (global-state :current-page)
    :home-page home-page
    :introduction-second second
    :introduction-third third))
