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


(def core-values-state (atom [
                              {:career {:history ""
                                        :score nil
                                        :vision ""
                                        :current-goal-name ""
                                        :current-step ""
                                        :goals [
                                                {:name ""
                                                 :description ""
                                                 :steps ["",""]
                                                 }]
                                        }
                               }
                              {:finances {:history ""
                                          :score nil
                                          :vision ""
                                          :current-goal-name ""
                                          :current-step ""
                                          :goals [
                                                  {:name ""
                                                   :description ""
                                                   :steps ["",""]
                                                   }]
                                          }
                               }
                              {:health {:history ""
                                        :score 5
                                        :vision ""
                                        :current-goal-name ""
                                        :current-step ""
                                        :goals [
                                                {:name ""
                                                 :description ""
                                                 :steps ["",""]
                                                 }]
                                        }
                               }
                              {:family {:history ""
                                        :score 3
                                        :vision ""
                                        :current-goal-name ""
                                        :current-step ""
                                        :goals [
                                                {:name ""
                                                 :description ""
                                                 :steps ["",""]
                                                 }]
                                        }
                               }
                              {:romance {:history ""
                                         :score 4
                                         :vision ""
                                         :current-goal-name ""
                                         :current-step ""
                                         :goals [
                                                 {:name ""
                                                  :description ""
                                                  :steps ["",""]
                                                  }]
                                         }
                               }
                              {:personal-growth {:history ""
                                                 :score 1
                                                 :vision ""
                                                 :current-goal-name ""
                                                 :current-step ""
                                                 :goals [
                                                         {:name ""
                                                          :description ""
                                                          :steps ["",""]
                                                          }]
                                                 }
                               }
                              {:fun {:history ""
                                     :score 2
                                     :vision ""
                                     :current-goal-name ""
                                     :current-step ""
                                     :goals [
                                             {:name ""
                                              :description ""
                                              :steps ["",""]
                                              }]
                                     }
                               }
                              {:physical-environment {:history ""
                                                      :score 3
                                                      :vision ""
                                                      :current-goal-name ""
                                                      :current-step ""
                                                      :goals [
                                                              {:name ""
                                                               :description ""
                                                               :steps ["",""]
                                                               }]
                                                      }
                               }
                              ]))


(def first-option nil)
(def second-option nil)
(def third-option nil)

(defn option-name [option]
  (-> (get @core-values-state option)
      (keys)
      (first)
      (name)))

(defn option-history [option]
  (-> (get @core-values-state option)
      (vals)
      (first)
      (:history)))

;;recursively iterate, if first-option isn't nil, set it as the first
;;core-value that has a nil score or the lowest score

(def mobile-parser
  (insta/parser
   "S = R* V* (G+)*
    R = 'a'
    V = 'b'
    G = g s+
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

(def map-scores-to-vec
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
                (set! third-option smallest-index))))))
      (recur (rest remaining-scores)))))

(defn test-option-history [option]
 (name (first (last (mobile-parser (-> (get @core-values-state
                                       option)
                                  (vals)
                                  (first)
                                  (:history)))))))

(defn initial-parsed-option-history [option]
  (last (mobile-parser (-> (get @core-values-state
                                option)
                           (vals)
                           (first)
                           (:history)))))

(defn final-parsing [parsed-option option-name]
  (condp = (first parsed-option)
    :R (str "Add a vision for " option-name)
    :V (str "Add a goal for " option-name)
    :G (str "Add a step for " option-name " goal XOYO")
    "Failed"))

(defn parse-option-history [option]
  (condp = (initial-parsed-option-history option)
      :S (str "Add a score for " (option-name option))
      (final-parsing (initial-parsed-option-history option) (option-name option))))

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

(defn final-parsing-link [parsed-option option]
  (condp = (first parsed-option)
    :R (set! (.-location js/window) (str "#/modules/visions?current_option=" option))
    :V (set! (.-location js/window) (str "#/modules/goals?current_option=" option))
    :G (set! (.-location js/window) (str "#/modules/steps?current_option="
  option "&current_goal=" (parsed-option-current-goal option)))
    (.log js/console "Failed!")))

(defn parse-option-history-link [option]
  (condp = (initial-parsed-option-history option)
    :S (set! (.-location js/window) (str "#/modules/scores?current_option=" option))
    (final-parsing-link (initial-parsed-option-history option) option)))

(defn component []
  [:div.application
   ;; (.log js/console (pr-str mobile-parser))
   ;; (.log js/console (str "Parsed output:" (nth (get (mobile-parser "abcd") 1) 1)))
   (populate-with-no-scores map-scores-to-vec)
   (populate-remaining-with-lowest-scores map-scores-to-vec)
   [:div.form-template
    [:h1 "Options"]
    [:ul
     [:li {:on-click #(parse-option-history-link first-option)}
      ;; Click -> Set current option to a var -> condp on history parse for
      ;; selected option -> call set location on current option to render the
      ;; next module
      ;; Next module reads from current option var and modifies its history
      ;; state string after
      [:label "First option"]
      [:p (parse-option-history first-option)]
      ]
     [:li
      [:label "Second option"]
      [:p (parse-option-history second-option)]
      ]
     [:li
      [:label "Third option"]
      [:p (parse-option-history third-option)]
      ]
     ]
    ]
   ;; [bind-fields form-template prefs]
   ])
