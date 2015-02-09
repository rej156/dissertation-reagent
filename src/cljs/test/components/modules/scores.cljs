;; (swap! core-values-state assoc-in [first-option (keyword
;;   (option-name first-option)) :history] (str (option-history first-option) "a"))
(ns test.components.modules.scores
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]))

(defn component []
  [:div.scores
   [:p "Rendered!"]])
