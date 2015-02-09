;; (swap! core-values-state assoc-in [first-option (keyword
;;   (option-name first-option)) :history] (str (option-history first-option) "a"))
(ns test.components.modules.scores
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]
            [test.components.application :as application]))

(defn component []
  [:div.scores
   [:h1 (str "The current option is: " application/current-option)]
   [:h2 (str "The option name is: " (application/option-name application/current-option))]
   [:p "Rendered!"]])
