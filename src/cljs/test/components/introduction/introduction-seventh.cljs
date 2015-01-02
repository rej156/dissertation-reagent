(ns test.components.introduction.seventh
  (:require [test.session :refer [global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent]
            [reagent-forms.core :refer [bind-fields]]))

(defn input [label type id alert]
  [:div.id
   [:label label]
   [:input.form-control {:default-value 3
                         :min 1
                         :max 7
                         :field type
                         :id id}]
   (if-not (nil? alert)
     [:div.alert.alert-danger
      {:field :alert :id id :event (if (= type :text) empty? nil?)}
      alert])])

(defn try-move-next []
  (set! (.-location js/window) "#/introduction/mood-assessment"))

(def form-template
  [:div.form-template
   [:h1 "User Profile:"]
   [:h2 (str (prefs-state :first-name) " " (prefs-state :last-name))]
   [:h2 (str (prefs-state :age) " years old" (prefs-state :sex))]
   [:h2 (name (prefs-state :relationship-status))]
   [:h2 (if-not (nil? (prefs-state :children-no))
          (str (prefs-state :children-no) " children")
          (str "No children"))]
   [:h1 "Coaching goals:"]
   [:ul
    (for [[k v] (prefs-state :coaching-goals)]
      (if (= v true)
        (condp = (name k)
          "purpose" [:li "I am looking for more meaning and purpose with my life"]
          "positivity" [:li "I'd like to adopt a more positive attitude"]
          "accountable" [:li "I want to identify goals and be held accountable to pursuing them"]
          "next-chapter" [:li "I want to figure the next chapter in my life"]
          "stress" [:li "I'd like to become calmer and reduce my stress"]
          "specific" [:li "There's a specific area in my life I want to improve"]
          "clarity" [:li "I need clarity - can't get my mind to focus"]
          "resilient" [:li "I'd like to be able to cope better with life, to be
  more resilient"]
          "liz" nil)))]

   (input (str "How happy do you feel now? " (prefs-state :first-name)) :numeric
          :mood.happy nil?)
   [:button {:on-click #(try-move-next)} "Submit"]])

(defn component []
  [:div.introduction-seventh
   [bind-fields form-template prefs]])
