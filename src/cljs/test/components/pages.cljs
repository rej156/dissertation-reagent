(ns test.components.pages
  (:require [test.session :refer [global-state]]
            [test.components.introduction.home-page :as home-page]
            [test.components.introduction.second :as second]
            [test.components.introduction.third :as third]
            [test.components.introduction.fourth :as fourth]))

(defn page []
  (condp = (global-state :current-page)
    :home-page home-page/component
    :introduction-second second/component
    :introduction-third third/component
    :introduction-fourth fourth/component))
