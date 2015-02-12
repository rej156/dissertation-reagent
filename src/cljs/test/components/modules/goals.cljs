(ns test.components.modules.goals
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.application :as application]))

(def existing-goals nil)

(defn goal-atom []
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
                                  (:name))))

(defn reset-goal-atom []
  (reset! goal-atom {:name ""
                     :description ""
                     :steps []
                     }))


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
                    (reset-goal-atom))
    "save" (do
             (save-goal)
             (reset-goal-atom)
             (set! (.-location js/window) "#/application")
             (application/reset-vars!))
    ))

(defn add-a-goal-component []
  [:div.goals
   [:h3 "To get closer to your vision: "]
   [:br]
   [:h4 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:vision)))]
   [:h4 (str "Write down an achievable goal that'll get your closer to your ideal
  future in your " (application/option-name application/current-option))]
   [:label "Goal name"]
   [:input.form-control {:type "text"
                         :on-change #(swap! goal-atom assoc :name (.. % -target
                                                                      -value))}]
   [:label "Goal description"]
   [:textarea {:rows 4
               :cols 50
               :on-change #(swap! goal-atom assoc :description (.. % -target
                                                                   -value))}]
   [:button {:on-click #(try-move-next "commit")} "Commit to it now"]
   [:button {:on-click #(try-move-next "add another")} "Add another goal"]
   [:button {:on-click #(try-move-next "save")} "Save for now"]
   ])

(defn commit-a-goal-component []
  [:div.commit-a-goal
   [:h3 "To get closer to your vision: "]
   [:br]
   [:h4 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:vision)))]

   [:h3 "Choose a goal to commit to and get closer to it!"]
   [:ol.list-group
    (for [goal (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:goals))]
      (let [current 0]
        [:li.list-group-item {:key goal
                              :on-click #(commit-goal-option current)}
         [:h5 (str (:name goal))]
         [:p (str "-> " (:description goal))]]
        (inc current)))]])

(defn goals-component [existing-goals]
  (if (= existing-goals 0)
    (add-a-goal-component)
    (commit-a-goal-component)))

(defn component []
  (goals-component existing-goals))
