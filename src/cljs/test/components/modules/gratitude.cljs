(ns test.components.modules.gratitude
  (:require [test.session :refer [merge-atoms global-put! global-state prefs-state prefs]]
            [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [alandipert.storage-atom :refer [local-storage]]
            [reagent-forms.core :refer [bind-fields]]))

(defonce gratitude-log (local-storage (atom {:mood-counter {:show-status 0
                                                            :time 0}
                                             :gratitude-log []}) :gratitude))
