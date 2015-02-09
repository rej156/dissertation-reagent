(ns test.components.modules.visions
  (:require [reagent-forms.core :refer [bind-fields]]
            [test.session :as session :refer [prefs prefs-state]]
            [test.components.application :as application]))

(defn component []
  [:div.visions
   [:p "Visions page bitch!"]])
