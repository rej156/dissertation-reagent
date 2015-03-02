(ns test.components.modules.goals
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.application :as application]
            [clojure.string :as str]))

(def existing-goals nil)

(def goal-atom
  (atom {:name ""
         :description ""
         :steps []
         }))

(defn save-goal []
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))  :goals]
         (conj (-> (get @application/core-values-state application/current-option)
                   (vals)
                   (first)
                   (:goals)) @goal-atom))
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))  :history]
         (str (-> (get @application/core-values-state application/current-option)
                  (vals)
                  (first)
                  (:history)) "c"))
  (swap! application/history-state conj (str "Added goal: "
                                             (str/capitalize
                                              (str (:name @goal-atom))))))

(defn commit-goal []
  (swap! application/core-values-state assoc-in [application/current-option (keyword (application/option-name
                                                                                      application/current-option))
                                                 :current-goal]
         (- (count (-> (get @application/core-values-state application/current-option)
                       (vals)
                       (first)
                       (:goals))) 1))
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option)) :current-goal-name] (:name @goal-atom))
  )

(defn commit-goal-option [option]
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option)) :current-goal] option)
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))
          :current-goal-name] (-> (get (-> (get @application/core-values-state
                                                application/current-option)
                                           (vals)
                                           (first)
                                           (:goals)) option)
                                  (:name)))
  (set! (.-location js/window) "#/application"))



(defn reset-goal-atom []
  (reset! goal-atom {}))

(defn try-move-next [option]
  (condp = option
    "commit" (do
               (save-goal)
               (commit-goal)
               (reset-goal-atom)
               (set! (.-location js/window) "#/application")
               (application/reset-vars!))
    "add another" (do
                    (save-goal)
                    (reset-goal-atom)
                    (set! (.-location js/window) (str
                                                  "#/modules/another-goal?current_option=" application/current-option)))
    "save" (do
             (save-goal)
             (reset-goal-atom)
             (set! (.-location js/window) "#/application")
             (application/reset-vars!))
    "go back" (do
                (reset-goal-atom)
                (set! (.-location js/window) "#/application")
                (application/reset-vars!))))

(defn add-a-goal-component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:div.card-panel.teal.introduction
      [:h3 "To get closer to your vision: "]
      [:h4 (str (-> (get @application/core-values-state
                         application/current-option)
                    (vals)
                    (first)
                    (:vision)))]
      [:h4 (str "in your " (application/option-name application/current-option) ".")]]
     [:div.card-panel.goal-commit
      [:h4 "Choose a goal to commit to and get closer to it!"]
      [:div.row
       [:div.input-field.col.s12
        [:textarea.materialize-textarea {:rows 4
                                         :cols 50
                                         :id "goal-name"
                                         :on-change #(swap! goal-atom assoc :name (.. % -target
                                                                                      -value))}]
        [:label {:for "goal-name"} "Goal name"]]]
      [:div.row
       [:div.input-field.col.s12
        [:textarea.materialize-textarea {:rows 4
                                         :cols 50
                                         :id "goal-description"
                                         :on-change #(swap! goal-atom assoc :description (.. % -target
                                                                                             -value))}]
        [:label {:for "goal-description"} "Goal description"]
        ]]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "commit")} "Going to commit to it
  now Liz!" [:i.mdi-action-accessibility.right]]
      [:br]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "add another")} "Add another goal" [:i.mdi-av-my-library-add.right]]
      [:br]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "save")}
       "Save for now" [:i.mdi-content-save.right]]
      [:br]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "go back")} "Go back!"]]
     ]]
   ])

(defn commit-a-goal-component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:div.card-panel.teal.add-another-goal
      [:h3 "To get closer to your vision: "]
      [:h4 (str (-> (get @application/core-values-state
                         application/current-option)
                    (vals)
                    (first)
                    (:vision)))]
      [:br]
      [:button.btn.waves-effect.waves-light {:on-click #(set! (.-location js/window) (str
                                                                                      "#/modules/another-goal?current_option=" application/current-option))} "Add
  another goal" [:i.mdi-av-my-library-add.right]]]
     [:div.divider]
     [:h4 "or"]
     [:div.divider]
     [:div.card-panel.blue.commit-goal-list
      [:h4 "Choose a goal to commit to and get closer to it!"]
      [:ul.collection.with-header.blue
       (for [goal (-> (get @application/core-values-state
                           application/current-option)
                      (vals)
                      (first)
                      (:goals))]
         [:li.collection-item {:key (:name goal)
                               :on-click #(commit-goal-option (application/index-of (-> (get @application/core-values-state
                                                                                             application/current-option)
                                                                                        (vals)
                                                                                        (first)
                                                                                        (:goals)) goal))}
          [:p (:name goal)]
          [:p (:description goal)]])]]
     ]]])

(defn goals-component [existing-goals]
  (if (= existing-goals 0)
    (add-a-goal-component)
    (commit-a-goal-component)))

(defn component []
  (goals-component existing-goals))
