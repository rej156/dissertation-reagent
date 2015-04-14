(ns test.components.modules.edit-goals)

(def current-goal nil)

(defn component []
  [:div.edit-goals
   [:h1 (str "Current goal is " current-goal)]
   [:h1 (str "Current core value is ")]])
