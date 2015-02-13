(ns test.components.modules.another-goal
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.modules.goals :as goals]
            [test.components.application :as application]))

(defn component []
  [:div.another-goal
   [:h2 "To get closer to your vision: "]
   [:h3 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:vision)))]
   [:br]
   [:h4 "Add another potential goal to commit to and get closer to it!"]
   [:label "Goal name"]
   [:input.form-control {:type "text"
                         :key "another-input"
                         :on-change #(swap! goals/goal-atom assoc :name (.. % -target
                                                                      -value))}]
   [:label "Goal description"]
   [:br]
   [:textarea {:rows 4
               :cols 50
               :key "another-textarea"
               :on-change #(swap! goals/goal-atom assoc :description (.. % -target
                                                                   -value))}]
   [:br]
   [:button {:on-click #(goals/try-move-next "commit")} "Commit to it now"]
   [:button {:on-click #(do
                          (goals/try-move-next "add another")
                          (set! (.-location js/window) (str
  "#/modules/goals?current_option=" application/current-option "&existing_goals=0")))} "Add another goal"]
   [:button {:on-click #(goals/try-move-next "save")} "Save for now"]])
