(ns test.components.modules.another-step
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.modules.goals :as goals]
            [test.components.application :as application]
            [test.components.modules.steps :as steps]))

(defn try-move-next [option]
  (condp = option
    "commit" (do
               (steps/save-step)
               (steps/commit-step)
               (steps/reset-step-atom)
               (set! (.-location js/window) "#/application")
               ;; Make component page for stating the user will get reminded of
               ;; their step?
               (application/reset-vars!))
    "go back" (do
                (steps/reset-step-atom)
                (set! (.-location js/window) "#/application")
                (application/reset-vars!))))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12.add-another-step
     [:div.card-panel
      [:h2 "To complete your goal: "]
      [:h4 (str (-> (get @application/core-values-state
                         application/current-option)
                    (vals)
                    (first)
                    (:current-goal-name)))]
      [:h4 " you've already completed these steps: "]
      [:div.previous-steps
       [:ol.collection
        (for [step (get-in @application/core-values-state [application/current-option
                                                           (-> (get @application/core-values-state
                                                                    application/current-option)
                                                               (keys)
                                                               (first))
                                                           :goals
                                                           (-> (get @application/core-values-state
                                                                    application/current-option)
                                                               (vals)
                                                               (first)
                                                               :current-goal)
                                                           :steps])]
          [:li.collection-item {:key (:step step)}
           [:p (str (:step step))]])]]
      [:h4 "to get you closer to your vision: "]
      [:h4 (str (-> (get @application/core-values-state
                         application/current-option)
                    (vals)
                    (first)
                    (:vision)))]
      [:h3 "Commit to your next baby step with Liz!"]
      [:div.row
       [:div.input-field.col.s12

        [:textarea.materialize-textarea {:rows 4
                                         :cols 50
                                         :key "another-step"
                                         :on-change #(swap! steps/step-atom assoc :step (.. % -target
                                                                                            -value))}]
        [:label "You can do this with Liz!"]]]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "commit")} "I'm going to do this!"]
      [:br]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "go back")} "Go back"]]]]])
