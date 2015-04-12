(ns test.components.modules.gratitude
  (:require [test.session :refer [merge-atoms global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [alandipert.storage-atom :refer [local-storage]]
            [test.components.introduction.mood-assessment :as assessment]
            [test.components.application :refer [core-values-state]]
            [reagent-forms.core :refer [bind-fields]]))

(defonce gratitude-log (local-storage (atom {:mood-counter {:show-status 0
                                                            :time 0}
                                             :gratitude-log []}) :gratitude))
