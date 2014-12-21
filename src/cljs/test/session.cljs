(ns test.session
  (:require [reagent.core :as reagent :refer [atom]]
            [alandipert.storage-atom :refer [local-storage]]))

;; ----------
;; State
(defonce app-state (atom {}))
(defonce prefs (local-storage (atom {}) :prefs))

;; ----------
;; Helper Functions
(defn global-state [k & [default]]
  (get @app-state k default))

(defn prefs-state [k & [default]]
  (get @prefs k default))

(defn global-put! [k v]
  (swap! app-state assoc k v))

(defn local-put! [a k v]
  (swap! a assoc k v))

(defn merge-atoms [first-atom second-atom]
  (swap! first-atom merge @first-atom @second-atom))

(add-watch prefs
           :new
           (fn [_ _ _ v]
             (.log js/console "new preference" v)))
