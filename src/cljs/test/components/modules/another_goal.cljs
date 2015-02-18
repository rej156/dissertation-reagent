(ns test.components.modules.another-goal
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.modules.goals :as goals]
            [test.components.application :as application]))

(defn component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:div.card-panel.teal
      [:h3 "To get closer to your vision: "]
      [:h4 (str (-> (get @application/core-values-state
                         application/current-option)
                    (vals)
                    (first)
                    (:vision)))]]
     [:br]
     [:div.card-panel.light-blue.accent-1
      [:h4 "Add another potential goal to commit to and get closer to it!"]
     [:div.row
      [:div.input-field.col.s12
       [:textarea.materialize-textarea {:row 4
                                        :col 50
                                        :key "another-input"
                                        :id "another-goal-name"
                                        :on-change #(swap! goals/goal-atom assoc :name (.. % -target
                                                                                           -value))}]
       [:label {:for "another-goal-name"} "Goal name"]]]
     [:div.row
      [:div.input-field.col.s12
       [:textarea.materialize-textarea {:rows 4
                                        :cols 50
                                        :key "another-textarea"
                                        :id "another-goal-description"
                                        :on-change #(swap! goals/goal-atom assoc :description (.. % -target
                                                                                                  -value))}]
       [:label {:for "another-goal-description"} "Goal description"]
       ]]
     [:button.btn.waves-effect.waves-light {:on-click #(goals/try-move-next "commit")} "Going to commit to
  it now Liz!" [:i.mdi-action-accessibility.right]]
     [:br]
     [:button.btn.waves-effect.waves-light {:on-click #(do
                                                         (goals/try-move-next "add another")
                                                         (set! (.-location js/window) (str
                                                                                       "#/modules/goals?current_option="
                                                                                       application/current-option "&existing_goals=0")))} "Add another goal" [:i.mdi-av-my-library-add.right]]
     [:br]
     [:button.btn.waves-effect.waves-light {:on-click #(goals/try-move-next "save")} "Save for now" [:i.mdi-content-save.right]]
     [:br]
     [:button.btn.waves-effect.waves-light {:on-click #(goals/try-move-next "go back")} "Go back!"]]
     ]]])
