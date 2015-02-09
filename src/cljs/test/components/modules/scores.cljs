(ns test.components.modules.scores
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]
            [test.components.application :as application]))

(defn try-move-next []
  (swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                 (application/option-name application/current-option)) :history] "a")
  (set! (.-location js/window) "#/application")
  (application/reset-vars!))

(defn component []
  [:div.scores
   [:h1 (str "The current option is: " application/current-option)]
   [:h2 (str "The option name is: " (application/option-name application/current-option))]
   [:div.form-template
    [:h3 (str "How would you rate your satisfaction in the "
              (application/option-name application/current-option) " area of your life?")]
    [:input.form-control {:type "number"
                          :min 0
                          :max 7
                          :on-change #(swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                                                                 (application/option-name
                                                                                                                  application/current-option)) :score] (js/parseInt (.. % -target -value)))
                          }]]
   [:button {:on-click #(try-move-next)} "Save"]])

;; Update history with string at the end
;; (swap! core-values-state assoc-in [first-option (keyword
;;   (option-name first-option)) :history] (str (option-history first-option) "a"))
