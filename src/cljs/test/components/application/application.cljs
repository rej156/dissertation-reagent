(ns test.components.application
  (:require [test.session :refer [global-put! global-state prefs-state prefs merge-atoms]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id]
  [:div.id
   [:label label]
   [:input.form-control {:field type :id id}]])

(def first-option nil)
(def second-option nil)
(def third-option nil)

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
                              {:family {:history ""
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
                              {:romance {:history ""
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
                              {:physical-environment {:history ""
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
                              ]))

;;recursively iterate, if first-option isn't nil, set it as the first
;;core-value that has a nil score or the lowest score

(def mobile-parser
  (insta/parser
   "S = R V G+
    R = 'a'
    V = 'b'
    G = g s+
    g = 'c'
    s = 'd'"))

(defn populate-with-no-scores []
  (loop [core-values @core-values-state]
    (when (not-empty core-values)
      (let [current (first core-values)]
        (if (empty? (:history (first (vals current))))
          (if (nil? first-option)
            (set! first-option (name (first (keys current))))
            (if (nil? second-option)
              (set! second-option (name (first (keys current)))))
            (if (nil? third-option)
              (set! third-option (name (first (keys current))))))))
      (recur (rest core-values)))))

(defn map-scores-to-vec []
  (-> into []
      (map #(-> %
                (vals)
                (first)
                :score)
           @core-values-state)))

(defn populate-remaining-with-lowest-scores [scores]
  (loop [scores]
    (when (not-empty scores)
      (let [current (first scores)]
        (if ))
      (recur (rest scores))))
  )

;; Do we populate first-options with the indexes of the core values with no
;; scores then the smallest scores?
;; ->> map-scores to vector
;; if current score is nil, populate option with index value of current score
;; recur loop again, if current score is the smallest, populate remaining nil
;; options with index value of the current smallest score
;; Render the names of the option indexes i.e. (name (first (keys (get @core-values-state first-option))))

;; (print first-option)
;; (print second-option)
;; (set! first-option nil)
;; (set! second-option nil)

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/seventh"))

(def form-template
  [:div.form-template
   [:h1 "Options"]
   [:ul
    [:li
     [:label "First option"]
     ]
    [:li
     [:label "Second option"]
     ]
    [:li
     [:label "Third option"]
     ]
    ]
   ])

(defn component []
  [:div.application
   (.log js/console (pr-str
                     mobile-parser))
   (.log js/console (str "Parsed
  output:" (nth (get (mobile-parser "abcd") 1) 1)))
   (->> (populate-with-no-scores)
        (populate-remaining-with-lowest-scores (map-scores-to-vec)))
   [bind-fields form-template prefs]])
