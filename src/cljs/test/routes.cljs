(ns test.routes
  (:require [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [test.session :refer [global-put!]]
            [test.components.pages :refer [pages]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

;; ----------
;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; need to run this after routes have been defined

;; ----------
;; Routes
(defn app-routes []
  (secretary/set-config! :prefix "#")
  (defroute "/" []
    (global-put! :current-page (pages :home-page)))

  (defroute "/introduction/second" []
    (global-put! :current-page (pages :introduction-second)))

  (defroute "/introduction/third" []
    (global-put! :current-page (pages :introduction-third)))
  (hook-browser-navigation!))
