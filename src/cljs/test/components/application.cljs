(ns test.components.application
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [instaparse.core :as insta]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id]
  [:div.id
   [:label label]
   [:input.form-control {:field type :id id}]])


(defonce core-values-state (atom [
                                  {:career {:history ""
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
                                            }
                                   }
                                  {:finances {:history ""
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
                                              }
                                   }
                                  {:health {:history ""
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
                                            }
                                   }
                                  {:family {:history ""
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
                                            }
                                   }
                                  {:romance {:history ""
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
                                             }
                                   }
                                  {:personal-growth {:history ""
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
                                                     }
                                   }
                                  {:fun {:history ""
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
                                         }
                                   }
                                  {:physical-environment {:history ""
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
                                                          }
                                   }
                                  ]))


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

;;recursively iterate, if first-option isn't nil, set it as the first
;;core-value that has a nil score or the lowest score

(def mobile-parser
  (insta/parser
   "S = R* V* G*
    R = 'a'
    V = 'b'
    G = g* s*
    g = 'c'
    s = 'd'"))

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

(defn index-of [coll v]
  (let [i (count (take-while #(not= v %) coll))]
    (when (or (< i (count coll))
              (= v (last coll)))
      i)))

(defn populate-remaining-with-lowest-scores [scores]
  (loop [remaining-scores scores]
    (when (not-empty remaining-scores)
      (let [smallest-index (index-of remaining-scores (apply min remaining-scores))
            current-score (apply min remaining-scores)]
        (if-not (nil? current-score)
          (if (nil? first-option)
            (set! first-option smallest-index)
            (if (nil? second-option)
              (set! second-option smallest-index)
              (if (nil? third-option)
                (set! third-option smallest-index)
                (if (= first-option second-option third-option)
                  (do
                    (set! second-option 1)
                    (set! third-option 2))))))
          (if (nil? third-option)
            (set! third-option 0)
            (if (and (nil? second-option) (= third-option 0))
              (set! second-option 1)))))
      (recur (rest remaining-scores)))))

(defn initial-parsed-option-history [option]
  (if-not (nil? option)
    (last (mobile-parser (-> (get @core-values-state
                                  option)
                             (vals)
                             (first)
                             (:history))))))

(defn final-final-parsing [option-name option]
  (if (nil? (-> (get @core-values-state option)
               (vals)
               (first)
               (:current-goal)))
    (str "Add or commit to a goal")
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
                          (:current-goal))))))
    ;; Take me to the existing_goals = 1 page but add a link to add another
    ;; goal page


(defn final-parsing [parsed-option option-name option]
  (if-not (nil? parsed-option)
    (condp = (first parsed-option)
      :R (str "Add a vision for " option-name)
      :V (str "Add a goal for " option-name)
      :G (final-final-parsing option-name option)
      "Failed")))

(defn parse-option-history [option]
  (condp = (initial-parsed-option-history option)
    :S (str "Add a score for " (option-name option))
    (final-parsing (initial-parsed-option-history option) (option-name option) option)))

;; Do we populate first-options with the indexes of the core values with no
;; scores then the smallest scores?
;; ->> map-scores to vector
;; if current score is nil, populate option with index value of current score
;; recur loop again, if current score is the smallest, populate remaining nil
;; options with index value of the current smallest score
;; Render the names of the option indexes i.e. (name (first (keys (get @core-values-state first-option))))

;; Now that we've got the indexes of the nil scores and the smallest scores, we
;; want to work on them. Therefore, we should do the following:
;; 1) Present the next possible action for that core value to be presented to
;; the user BUT WITH A RE-RENDER AND PARSING ON EACH USER ACTION
;; Intermittently, the user is presented to work on the lowest scores at all times.
;; 2) In these cases, the possible states of action would be...
;; - [ ] Scoring core value -> Add a "a" string to its (:history (first (vals
;; current))) -> Set current option var back to nil
;; **** DO WE UPDATE THE PARSER TO ACCEPT zero or more so it can present the
;; correct key of the required state? i.e. A score without a goal can still be parsed.
;; ****
;; - [ ] Add a vision -> Check if there is a [:V "b"] vector, initiate the
;; vision adding module then add a "b" to the history string
;; - [ ] Add/Commit to a goal -> Check if there is a [:G "c" "d" "d"] vector or
;; a current goal string not being empty,
;; setting the current option value chosen,
;; initiate the adding/commiting to a goal module to prompt like the following:
;; 1) Add a goal to 'XOYO' core value -> Initiate the add a goal module,
;;  then add the "c" string to the history
;; 2) Commit to a goal -> Initiate the committing to a goal module, this
;; displays all the parsed goal names with numbered choice values, setting the
;; current goal value with the value-index of the chosen option ->
;; Initiate the add a step module, populate the step name for that chosen goal
;; and add a "d" to the history string, set the value of the current step as 0
;; Say that we will remind the user about completing that step periodically
;; - [ ] Confirm completion of current step name "XOYO" of current goal name
;; for 'XOYO' core-value->
;; Initiate the confirm step completion module, prompt the following:
;; 1) Ask user if they need to add another step, set current step name to new
;; one and add another "d", incrememnt current step value by 1
;; 2) Confirm goal completion -> Congratulate them, set current goal string to
;; empty, set the current step value as nil, ask them to score the core value again

;; ***** Then work on the following:
;; Recent actions
;; Mood evaluation and timer
;; History of actions for a given core value
;; List of all goals for a given core value

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
  (condp = (first parsed-option)
    :R (set! (.-location js/window) (str "#/modules/visions?current_option=" option))
    :V (set! (.-location js/window) (str "#/modules/goals?current_option="
                                         option "&existing_goals=" (option-existing-goals option)))
    :G (final-final-parsing-link option)
    (.log js/console "Failed!")))

(defn parse-option-history-link [option]
  (condp = (initial-parsed-option-history option)
    :S (set! (.-location js/window) (str "#/modules/scores?current_option=" option))
    (final-parsing-link (initial-parsed-option-history option) option)))

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
  (swap! core-values-state assoc-in [0 :career :goals] [{:name "Tits
  everywhere"
                                                         :description "Lick
  them titties"},
                                                        {:name "lol
  everywhere"
                                                         :description "huh
  them titties"}]))

(defn component []
  [:div.application
   ;; (.log js/console (pr-str mobile-parser))
   ;; (.log js/console (str "Parsed output:" (nth (get (mobile-parser "abcd") 1) 1)))
   [:p (pr-str @core-values-state)]
   [:p (str "First option: " first-option)]
   [:p (str "Second option: " second-option)]
   [:p (str "Third option: " third-option)]
   (populate-with-no-scores (map-scores-to-vec))
   (populate-remaining-with-lowest-scores (map-scores-to-vec))
   ;;(populate-when-equal-scores (map-scores-to-vec))
   [:div.form-template
    [:h1 "Options"]
    [:ol
     [:li {:on-click #(parse-option-history-link first-option)}
      [:label "First option"]
      [:p (parse-option-history first-option)]
      ]
     [:li {:on-click #(parse-option-history-link second-option)}
      [:label "Second option"]
      [:p (parse-option-history second-option)]
      ]
     [:li {:on-click #(parse-option-history-link third-option)}
      [:label "Third option"]
      [:p (parse-option-history third-option)]
      ]
     ]
    ]
   ;; [bind-fields form-template prefs]
   ])
