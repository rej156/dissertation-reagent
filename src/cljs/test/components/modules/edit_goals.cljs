(ns test.components.modules.edit-goals
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [test.components.application :as application]
            [reagent-forms.core :refer [bind-fields]]))

(def current-goal nil)

(defn trim-newline [s]
  (clojure.string/trim (clojure.string/replace s #"\n" "")))

(defn component []
  [:div.edit-goals
   [:div.container
    [:div.row
     [:div.col.s12
      [:div.card-panel.light-blue.accent-1
       [:h4 (str "In the " (application/option-name application/current-option))
        " area of your life, this is your goal with Liz:"]
       [:div.row
        [:div.input-field.col.s12
         [:textarea.materialize-textarea {:row 4
                                          :col 50
                                          :value (trim-newline (get-in
                                                       @application/core-values-state [application/current-option (keyword
                                                                                                                   (application/option-name
                                                                                                                    application/current-option))
                                                                                       :goals current-goal :name]))
                                          :key "edit-goal-input"
                                          :id "edit-goal-name"
                                          :on-change #(swap!
  application/core-values-state assoc-in [application/current-option
  (keyword (application/option-name application/current-option)) :goals
  current-goal :name] (.. % -target -value))}]
         [:label {:for "edit-goal-name"} "Goal name"]]]
       [:div.row
        [:div.input-field.col.s12
         [:textarea.materialize-textarea {:rows 4
                                          :cols 50
                                          :value (trim-newline (get-in
                                                       @application/core-values-state [application/current-option (keyword
                                                                                                                   (application/option-name
                                                                                                                    application/current-option))
                                                                                       :goals current-goal :description]))
                                          :key "edit-goal-textarea"
                                          :id "edit-goal-description"
                                          :on-change #(swap!
                                                       application/core-values-state assoc-in
                                                      [application/current-option (keyword
                                                                                                                   (application/option-name
                                                                                                                    application/current-option))
                                                                                       :goals current-goal :description] (.. % -target
                                                                                     -value))}]
         [:label {:for "edit-goal-description"} "Goal description"]
         ]]
       [:div.edit-steps
        [:h5 "and its steps"]
        [:ol.collection
         (let [counter (atom 0)]
           (for [step (get-in @application/core-values-state
  [application/current-option (keyword (application/option-name
                                        application/current-option))
   :goals
   current-goal
   :steps])]
             ^{:key step}
             [:li.collection-item
              [:input {:type "text"
                       :default-value (:step step)}]
              [:div.counter {:style {:display "none"}} (swap! counter inc)]]))]]
       ;; Input doesn't persist step edits
       [:br]
       [:button.btn.waves-effect.waves-light {:on-click #(set! (.-location
  js/window) "#/application")} "Save for now Liz" [:i.mdi-content-save.right]]]
      ]]]])
