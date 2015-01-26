(ns test.components.pages
  (:require [test.session :refer [global-state]]
            [test.components.introduction.home-page :refer [home-page]]))

(defn page []
  (condp = (global-state :current-page)
    :home-page home-page))
