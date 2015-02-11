(ns test.components.modules.visions
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]
            [test.components.application :as application]))

(defn try-move-next []
  (set! (.-location js/window) "#/application")
  (swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                             (application/option-name
  application/current-option)) :history] (str (-> (get
                                                   @application/core-values-state application/current-option)
                                                  (vals)
                                                  (first)
                                                  (:history)) "b"))
  (application/reset-vars!))

(defn component []
  [:div.visions
   [:h3 (str "I want you to imagine your most ideal future in the "
             (application/option-name application/current-option) " area of
  your life.")]
   [:h4 "Make it as vivid as possible in your mind."]
   [:h4 "Can you describe it to me in full detail?"]
   [:textarea {:rows 4
               :cols 50
               :on-change #(swap! application/core-values-state assoc-in [application/current-option (keyword
                                                                                                      (application/option-name
                                                                                                       application/current-option)) :vision] (.. % -target -value))}]
   [:br]
   [:button {:on-click #(try-move-next)} "Save"]])
