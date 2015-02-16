(ns test.components.modules.goals
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.application :as application]))

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
                  (:history)) "c")))

(defn commit-goal []
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option)) :current-goal] (- (count (-> (get @application/core-values-state application/current-option)
                                                                                                         (vals)
                                                                                                         (first)
                                                                                                         (:goals))) 1))
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option)) :current-goal-name] (:name @goal-atom)))

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
             (application/reset-vars!))))

(defn add-a-goal-component []
  [:div.goals
   [:h2 "To get closer to your vision: "]
   [:h3 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:vision)))]
   [:br]
   [:h4 "Choose a goal to commit to and get closer to it!"]
   [:label "Goal name"]
   [:input.form-control {:type "text"
                         :on-change #(swap! goal-atom assoc :name (.. % -target
                                                                      -value))}]
   [:label "Goal description"]
   [:br]
   [:textarea {:rows 4
               :cols 50
               :on-change #(swap! goal-atom assoc :description (.. % -target
                                                                   -value))}]
   [:br]
   [:button {:on-click #(try-move-next "commit")} "Commit to it now"]
   [:button {:on-click #(try-move-next "add another")} "Add another goal"]
   [:button {:on-click #(try-move-next "save")} "Save for now"]
   ])

(defn commit-a-goal-component []
  [:div.commit-a-goal
   [:h2 "To get closer to your vision: "]
   [:h3 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:vision)))]
   [:br]
   [:button {:on-click #(set! (.-location js/window) (str
  "#/modules/another-goal?current_option=" application/current-option))} "Add
  another goal"]
   [:h4 "or"]
   [:h4 "Choose a goal to commit to and get closer to it!"]
   [:ol
    (for [goal (-> (get @application/core-values-state
                        application/current-option)
                   (vals)
                   (first)
                   (:goals))]
      [:li {:key (:name goal)
            :on-click #(commit-goal-option (application/index-of (-> (get @application/core-values-state
                                                                          application/current-option)
                                                                     (vals)
                                                                     (first)
                                                                     (:goals)) goal))}
       [:p (:name goal)]
       [:p (:description goal)]])]])

(defn goals-component [existing-goals]
  (if (= existing-goals 0)
    (add-a-goal-component)
    (commit-a-goal-component)))

(defn component []
  (goals-component existing-goals))
