(ns test.components.modules.steps
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.application :as application]
            [test.components.modules.goals :as goals]))

(def existing-step nil)

(def step-atom (atom {:step ""}))

(defn reset-step-atom []
  (reset! step-atom {:step ""}))

(def confirmation-atom (atom false))

(defn show-confirmation []
  (reset! confirmation-atom true))

(defn commit-step []
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))  :current-step]
         (:step @step-atom)))

(defn save-step []
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))
          :goals (- (count (-> (get @application/core-values-state
                                    application/current-option)
                               (vals)
                               (first)
                               (:goals))) 1) :steps]
         (conj (-> (get @application/core-values-state application/current-option)
                   (vals)
                   (first)
                   (:goals)
                   (last)
                   (:steps)) @step-atom))
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))  :history]
         (str (-> (get @application/core-values-state application/current-option)
                  (vals)
                  (first)
                  (:history)) "d")))

(defn try-move-next [option]
  (condp = option
    "commit" (do
               (save-step)
               (commit-step)
               (reset-step-atom)
               (set! (.-location js/window) "#/application")
               ;; Make component page for stating the user will get reminded of
               ;; their step?
               (application/reset-vars!))
    "go back" (do
                (reset-step-atom)
                (set! (.-location js/window) "#/application")
                (application/reset-vars!))
    "completed" (do
                  (save-step)
                  (reset-step-atom)
                  (set! (.-location js/window) (str
                                                "#/modules/scores?current_option=" application/current-option))
                  (swap! application/core-values-state assoc-in
                         [application/current-option (-> (get @application/core-values-state
                                                              application/current-option)
                                                         (keys)
                                                         (first)) :current-goal] nil)
                  (swap! application/core-values-state assoc-in
                         [application/current-option (-> (get @application/core-values-state
                                                              application/current-option)
                                                         (keys)
                                                         (first)) :current-goal-name] "")
                  (swap! application/core-values-state assoc-in
                         [application/current-option (-> (get @application/core-values-state
                                                              application/current-option)
                                                         (keys)
                                                         (first)) :current-step] ""))
    "add another" (do
                    (reset-step-atom)
                    (swap! application/core-values-state assoc-in
                           [application/current-option (-> (get @application/core-values-state
                                                                application/current-option)
                                                           (keys)
                                                           (first)) :current-step] "")
                    (set! (.-location js/window)
                          (str "#/modules/another-step?current_option=" application/current-option)))))

(defn add-a-step-component []
  [:div.add-a-step
   [:h3 "To complete your goal: "]
   [:h4 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:current-goal-name)))]
   [:h3 " and get one step closer to your vision: " ]
   [:h4 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:vision)))]
   [:h3 "Commit to your next baby step!"]
   [:p "Think hard about this, break your goal down into small easy steps; you
  can do this with Liz!"]
   [:textarea {:rows 4
               :cols 50
               :on-change #(swap! step-atom assoc :step (.. % -target
                                                            -value))}]
   [:br]
   [:button {:on-click #(try-move-next "commit")} "I'm going to do this!"]
   [:button {:on-click #(try-move-next "go back")} "Go back"]])

(defn complete-step-component []
  [:div.complete-step
   [:h3 "You said you would complete the step: "]
   [:h4 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:current-step)))]
   [:h3 "for the current goal: "]
   [:h4 (str (-> (get @application/core-values-state
                      application/current-option)
                 (vals)
                 (first)
                 (:goals)
                 (last)
                 (:name)))]
   [:br]
   (if-not (empty? (-> (get @application/core-values-state
                            application/current-option)
                       (vals)
                       (first)
                       (:goals)
                       (last)
                       (:steps)))
     [:div.previous-steps
      [:h4 "Listed steps:"]
      [:ol
       (for [step (-> (get @application/core-values-state
                           application/current-option)
                      (vals)
                      (first)
                      (:goals)
                      (last)
                      (:steps))]
         [:li {:key (:name step)}
          [:p (str (:step step))]])]])
   [:h3 "Have you completed this step yet?"]
   [:button {:on-click #(show-confirmation) } "Yes" ]
   [:button {:on-click #(try-move-next "go back") } "No, go back" ]
   [:br]
   (if (true? @confirmation-atom)
     [:div.goal-completion-confirmation
      [:h4 "Has your goal been completed because of this?"]
      [:button {:on-click #(try-move-next "completed") } "I've succeeded!"]
      [:button {:on-click #(try-move-next "add another") } "Not yet Liz, I need
  to complete another step first"]])])

(defn steps-component [existing-step]
  (if (= existing-step 0)
    (add-a-step-component)
    (complete-step-component)))

(defn component []
  [:div.steps
   (steps-component existing-step)])
