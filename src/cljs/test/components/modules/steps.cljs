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

(defn reset-step-completion-drawer []
  (reset! confirmation-atom false))

(defn commit-step []
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))  :current-step]
         (:step @step-atom)))

(defn save-step []
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))
          :goals
          (-> (get @application/core-values-state
                   application/current-option)
              (vals)
              (first)
              (:current-goal))
          :steps]
         (conj (get-in @application/core-values-state [application/current-option
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
                                                       :steps]) @step-atom))
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))  :history]
         (str (-> (get @application/core-values-state application/current-option)
                  (vals)
                  (first)
                  (:history)) "d")))

(defn drop-nth
  [pos coll]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn complete-goal []
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name
                                               application/current-option))  :completed-goals]
         (conj (-> (get
                    @application/core-values-state application/current-option)
                   (vals)
                   (first)
                   (:completed-goals))
               (get-in @application/core-values-state [application/current-option
                                                       (keyword
                                                        (application/option-name application/current-option))
                                                       :goals
                                                       (-> (get @application/core-values-state
                                                                application/current-option)
                                                           (vals)
                                                           (first)
                                                           :current-goal)])))
  (swap! application/core-values-state assoc-in
         [application/current-option (keyword (application/option-name application/current-option))
          :goals]
         (drop-nth (-> (get @application/core-values-state application/current-option)
                       (vals)
                       (first)
                       (:current-goal))
                   (-> (get @application/core-values-state application/current-option)
                       (vals)
                       (first)
                       (:goals))))
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

(defn try-move-next [option]
  (condp = option
    "commit" (do
               (save-step)
               (commit-step)
               (reset-step-atom)
               (set! (.-location js/window) "#/application")
               ;; Make component page for stating the user will get reminded of
               ;; their step?
               (reset-step-completion-drawer)
               (application/reset-vars!))
    "completed" (do
                  (save-step)
                  (complete-goal)
                  (reset-step-completion-drawer)
                  (reset-step-atom)
                  (set! (.-location js/window) (str
                                                "#/modules/scores?current_option=" application/current-option)))
    "go back" (do
                (reset-step-atom)
                (set! (.-location js/window) "#/application")
                (reset-step-completion-drawer)
                (application/reset-vars!))
    "add another" (do
                    (reset-step-completion-drawer)
                    (reset-step-atom)
                    (swap! application/core-values-state assoc-in
                           [application/current-option (-> (get @application/core-values-state
                                                                application/current-option)
                                                           (keys)
                                                           (first)) :current-step] "")
                    (set! (.-location js/window)
                          (str "#/modules/another-step?current_option=" application/current-option)))))

(defn add-a-step-component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:div.card-panel.red.lighten-1
      [:h2 "To complete your goal: "]
      [:h4 (str (-> (get @application/core-values-state
                         application/current-option)
                    (vals)
                    (first)
                    (:current-goal-name)))]
      [:h5 " and get one step closer to your vision: " ]
      [:h4 (str (-> (get @application/core-values-state
                         application/current-option)
                    (vals)
                    (first)
                    (:vision)))]
      [:h3 "Commit to your next baby step!"]
      [:div.row
       [:div.input-field.col.s12
        [:textarea.materialize-textarea {:rows 4
                                         :cols 50
                                         :id "step"
                                         :on-change #(swap! step-atom assoc :step (.. % -target
                                                                                      -value))}]
        [:label {:for "step"} "You can do this with Liz!"]]]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "commit")} "I'm going to do this Liz!"]
      [:br]
      [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "go back")} "Go back"]]]]])

(defn complete-step-component []
  [:div.container
   [:div.row
    [:div.col.s12
     [:div.card-panel
      [:div.details
       [:h3 "You said you would complete the step: "]
       [:h4 (str (-> (get @application/core-values-state
                          application/current-option)
                     (vals)
                     (first)
                     (:current-step)))]
       [:h4 "for the current goal: "]
       [:h4 (str (-> (get @application/core-values-state
                          application/current-option)
                     (vals)
                     (first)
                     (:current-goal-name)))]
       (if-not (empty? (get-in @application/core-values-state [application/current-option
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
                                                               :steps]))
         [:div.previous-steps
          [:h5 "and all its listed steps: "]
          [:ol.collection
           (for [step (reverse (get-in @application/core-values-state [application/current-option
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
                                                              :steps]))]
             [:li.collection-item {:key (:name step)}
              [:p (str (:step step))]])]])]
      [:div.row
       [:div.col.s12
        [:div.card-panel.red
         [:h3 "Have you completed this step yet?"]
         [:button.btn.waves-effect.waves-light {:on-click #(show-confirmation)}
          "Yes I have Liz!" [:i.mdi-action-done.right]]
         [:br]
         [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "go back") } "Not yet, go back" ]
         [:br]
         (if (true? @confirmation-atom)
           [:div.goal-completion-confirmation
            [:h4 "Has your goal been completed because of this?"]
            [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "completed") } "I've succeeded!" [:i.mdi-action-done-all.right]]
            [:br]
            [:button.btn.waves-effect.waves-light {:on-click #(try-move-next "add another") } "Another step must be completed first!" [:i.mdi-av-my-library-add.right]]])]]]]
     ]]])

(defn steps-component [existing-step]
  (if (= existing-step 0)
    (add-a-step-component)
    (complete-step-component)))

(defn component []
  [:div.steps
   (steps-component existing-step)])
