(ns test.components.application
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]
            [test.components.nav :as nav]))

(defn input [label type id]
  [:div.id
   [:label label]
   [:input.form-control {:field type :id id}]])

(def core-value-content
  {:history ""
   :score nil
   :vision ""
   :current-goal-name ""
   :current-goal nil
   :current-step ""
   :goals [
           ;; {:name ""
           ;;  :description ""
           ;;  :steps ["",""]
           ;;  }
           ]
   :completed-goals []
   })


(defonce core-values-state (atom [
                                  {:career core-value-content}
                                  {:finances core-value-content}
                                  {:health core-value-content}
                                  {:family core-value-content}
                                  {:romance core-value-content}
                                  {:personal-growth core-value-content}
                                  {:fun core-value-content}
                                  {:physical-environment core-value-content}
                                  ]))

(defonce history-state (atom []))

(def first-option nil)
(def second-option nil)
(def third-option nil)

(defn option-name [option]
  (if-not (nil? option)
    (-> (get @core-values-state option)
        (keys)
        (first)
        (name))))

(defn option-history [option]
  (if-not (nil? option)
    (-> (get @core-values-state option)
        (vals)
        (first)
        (:history))))

(defn option-goals [option]
  (if-not (nil? option)
    (-> (get @core-values-state option)
        (vals)
        (first)
        (:goals))))

(def mobile-parser
  (insta/parser
   "S = R* V* G*
    R = 'a'
    V = 'b'
    G = g* s*
    g = 'c'
    s = 'd'"))

(defn index-of [coll v]
  (let [i (count (take-while #(not= v %) coll))]
    (when (or (< i (count coll))
              (= v (last coll)))
      i)))

(defn populate-with-no-scores [scores]
  (loop [remaining-scores scores
         counter 0]
    (when (not-empty remaining-scores)
      (let [current (get @core-values-state counter)]
        (if (nil? (:score (first (vals current))))
          (if (nil? first-option)
            (set! first-option counter)
            (if (nil? second-option)
              (set! second-option counter)
              (if (nil? third-option)
                (set! third-option counter))))))
      (recur (rest remaining-scores) (inc counter)))))

(defn map-scores-to-vec []
  (mapv #(:score (first (vals %))) @core-values-state))

(defn populate-remaining-with-lowest-scores [scores]
  (loop [remaining-scores scores
         current-index 0]
    (when (not-empty remaining-scores)
      (let [first-index (first (apply min-key second (map-indexed vector remaining-scores)))
            second-index (second (apply min-key second (map-indexed vector remaining-scores)))
            current-smallest-score (apply min remaining-scores)]
        (if-not (or (nil? current-smallest-score) (contains? nil remaining-scores))
          (if (nil? first-option)
            (set! first-option first-index)
            (if (nil? second-option)
              (set! second-option second-index)
              (when (> (count remaining-scores) 2)
                (if (nil? third-option)
                  (try
                    (set! third-option (nth (apply min-key second (map-indexed
                                                                   vector
                                                                   remaining-scores)) 2))
                    (catch :default e
                      (if (>= first-option 6)
                        (do
                          (set! second-option 1)
                          (set! third-option 2))
                        (do
                          (set! second-option (+ first-option 1))
                          (set! third-option (+ first-option 2))))))))))
          (if (or (nil? second-option) (nil? third-option))
            (do
              (set! second-option 0)
              (set! third-option 1))))
        (recur (rest remaining-scores) (inc current-index))))))

(defn initial-parsed-option-history [option]
  (if-not (nil? option)
    (last (mobile-parser (-> (get @core-values-state
                                  option)
                             (vals)
                             (first)
                             (:history))))))

(defn final-final-parsing [option-name option]
  (if-not (nil? option)
    (if (nil? (-> (get @core-values-state option)
                  (vals)
                  (first)
                  (:current-goal)))
      (str "Add or commit to a goal for " option-name)
      (if (empty? (-> (get @core-values-state option)
                      (vals)
                      (first)
                      (:current-step)))
        (str "Add a step for " option-name " goal '" (-> (get @core-values-state
                                                              option)
                                                         (vals)
                                                         (first)
                                                         (:current-goal-name)) "'")
        (str "Confirm completion of the step " (-> (get @core-values-state option)
                                                   (vals)
                                                   (first)
                                                   (:current-step))
             " for goal " (-> (get @core-values-state
                                   option)
                              (vals)
                              (first)
                              (:current-goal-name)) " of "
                              (-> (get @core-values-state option)
                                  (keys)
                                  (first)
                                  (name)))))))

(defn final-parsing [parsed-option option-name option]
  (if-not (and (nil? parsed-option) (nil? option))
    (condp = (first parsed-option)
      :R (str "Add a vision for " option-name)
      :V (str "Add a goal for " option-name)
      :G (final-final-parsing option-name option)
      "Failed")))

(defn parse-option-history [option]
  (if-not (nil? option)
    (condp = (initial-parsed-option-history option)
      :S (str "Add a score for " (option-name option))
      (final-parsing (initial-parsed-option-history option) (option-name option) option))))

;; ***** Work on the following:
;; Recent actions
;; Mood evaluation and timer
;; History of actions for a given core value
;; List of all goals for a given core value
;; List of all visions

;; (print first-option)
;; (print second-option)
;; (set! first-option nil)
;; (set! second-option nil)

(def current-option nil)

(defn reset-vars! []
  (set! current-option nil)
  (set! first-option nil)
  (set! second-option nil)
  (set! third-option nil))

(defn option-existing-goals [option]
  (if (empty? (option-goals option))
    0
    1))

(defn final-final-parsing-link [option]
  (if (nil? (-> (get @core-values-state option)
                (vals)
                (first)
                (:current-goal)))
    (set! (.-location js/window) (str "#/modules/goals?current_option="
                                      option "&existing_goals=1"))
    (if (empty? (-> (get @core-values-state option)
                    (vals)
                    (first)
                    (:current-step)))
      (set! (.-location js/window) (str "#/modules/steps?current_option="
                                        option "&existing_step=0"))
      (set! (.-location js/window) (str "#/modules/steps?current_option="
                                        option "&existing_step=1")))))

(defn final-parsing-link [parsed-option option]
  (if-not (nil? parsed-option)
    (condp = (first parsed-option)
      :R (set! (.-location js/window) (str "#/modules/visions?current_option=" option))
      :V (set! (.-location js/window) (str "#/modules/goals?current_option="
                                           option "&existing_goals=" (option-existing-goals option)))
      :G (final-final-parsing-link option)
      (.log js/console "Failed!"))))

(defn parse-option-history-link [option]
  (if-not (nil? option)
    (condp = (initial-parsed-option-history option)
      :S (set! (.-location js/window) (str "#/modules/scores?current_option=" option))
      (final-parsing-link (initial-parsed-option-history option) option))))

(defn setup-visions []
  (loop [current @core-values-state
         counter 0]
    (when (not-empty current)
      (swap! core-values-state assoc-in [counter (first (keys (first current)))
                                         :history] "a")
      (swap! core-values-state assoc-in [counter (first (keys (first current)))
                                         :score] 5)
      (recur (rest current) (inc counter)))))

(defn setup-goals []
  (swap! core-values-state assoc-in [0 :career :score] 5)
  (swap! core-values-state assoc-in [0 :career :history] "abc")
  (swap! core-values-state assoc-in [0 :career :goals] [{:name "Become an
  expert in your field"
                                                         :description "Source
  of knowledge"},
                                                        {:name "Become a freelancer"
                                                         :description "Remote
  Clojure Worker"}]))

(defn setup-steps []
  (setup-visions)
  (setup-goals)
  (swap! core-values-state assoc-in [0 :career :vision] "Be able to work on
  your own projects with an inspiring vision.")
  (swap! core-values-state assoc-in [0 :career :current-goal-name] "Become a freelancer")
  (swap! core-values-state assoc-in [0 :career :current-goal] 0))

(defn history-style []
  (if (> (count @history-state) 5)
    {:overflow "scroll"
     :margin-top "10px"
     :height "200px"}
    {:overflow "scroll"
     :margin-top "10px"}))

(defn content-component []
  [:div.content
   [:div.row
    [:div.col.s12.intro
     [:h3 (str "Hi " (prefs-state :first-name) "!")]
     [:h4 "Would you like to?"]]]
   [:div.row
    [:div.section.actions
     [:div.col.s12.m4
      [:div.card-panel.teal {:on-click #(parse-option-history-link first-option)}
       [:h5 (parse-option-history first-option)]]]
     [:div.col.s12.m4
      [:div.card-panel.blue {:on-click #(parse-option-history-link second-option)}
       [:h5 (parse-option-history second-option)]]]
     [:div.col.s12.m4
      [:div.card-panel.yellow {:on-click #(parse-option-history-link third-option)}
       [:h5 (parse-option-history third-option)]]]]]])

(defn history-component []
  [:div.history-items
   (if-not (empty? @history-state)
     [:div.row {:id "history"
                :style (history-style)}
      [:div.section
       [:div.col.s12
        (into [:ul.collection] (map (partial vector
                                             :li.collection-item) @history-state))]]])])

(defn scaffolded-component []
  [:div.application
   (nav/component :main)
   [:div.container
    (history-component)
    [:div.divider]
    (content-component)]])

;; Mobile like notifications
;; (js/toast "I am a testing toast" 4000)

(defn component []
  (reagent/create-class
   {:component-will-mount #(do
                             (populate-with-no-scores (map-scores-to-vec))
                             (populate-remaining-with-lowest-scores
                              (map-scores-to-vec)))

    :component-did-mount #(try
                            (-> (js/$ "#history")
                              (.scrollTop (-> (js/$ "#history")
                                              (.prop "scrollHeight"))))
                            (catch :default e
                              ))
    :render scaffolded-component}))
