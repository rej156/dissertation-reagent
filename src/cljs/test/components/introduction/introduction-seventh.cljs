(ns test.components.introduction.seventh
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id]
  [:div.id
   [:label label]
   [:input.form-control {:default-value 3
                         :min 1
                         :max 7
                         :field type
                         :id id}]])

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/mood-assessment-initial"))

(defn component []
  [:div.introduction-seventh
   [:h1 "User Profile:"]
   [:p (str (prefs-state :first-name) " " (prefs-state :last-name))]
   [:p (str (prefs-state :age) " years old" (prefs-state :sex))]
   [:p (str (name (prefs-state :relationship-status)))]
   [:p (if-not (nil? (prefs-state :children-no))
         (str (prefs-state :children-no) " children")
         (str "No children"))]
   [:h2 "Coaching goals:"]
   [:ul.list-group
    (for [[k v] (prefs-state :coaching-goals)]
      (if (= v true)
        (condp = (name k)
          "purpose" [:li.list-group-item {:key k} "I am looking for more meaning and purpose with my life"]
          "positivity" [:li.list-group-item {:key k} "I'd like to adopt a more positive attitude"]
          "accountable" [:li.list-group-item {:key k} "I want to identify goals and be held accountable to pursuing them"]
          "next-chapter" [:li.list-group-item {:key k} "I want to figure the next chapter in my life"]
          "stress" [:li.list-group-item {:key k} "I'd like to become calmer and reduce my stress"]
          "specific" [:li.list-group-item {:key k} "There's a specific area in my life I want to improve"]
          "clarity" [:li.list-group-item {:key k} "I need clarity - can't get my mind to focus"]
          "resilient" [:li.list-group-item {:key k} "I'd like to be able to cope better with life, to be
  more resilient"]
          "liz" [:li.list-group-item {:key k} "I am simply curious about you
  Liz and want to see how this thing works"])))]
   [:button {:on-click #(try-move-next)} "Submit"]])
