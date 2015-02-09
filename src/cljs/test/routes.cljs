(ns test.routes
  (:require [secretary.core :as secretary :include-macros true :refer-macros [defroute]]
            [test.session :refer [global-put!]]
            [test.components.pages :refer [page]]
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
    (global-put! :current-page :home-page))

  (defroute "/introduction/second" []
    (global-put! :current-page :introduction-second))

  (defroute "/introduction/third" []
    (global-put! :current-page :introduction-third))

  (defroute "/introduction/fourth" []
    (global-put! :current-page :introduction-fourth))

  (defroute "/introduction/fifth" []
    (global-put! :current-page :introduction-fifth))

  (defroute "/introduction/sixth" []
    (global-put! :current-page :introduction-sixth))

  (defroute "/introduction/seventh" []
    (global-put! :current-page :introduction-seventh))

  (defroute "/introduction/mood-assessment-initial" []
    (global-put! :current-page :mood-assessment-initial))

  (defroute "/introduction/mood-assessment" []
    (global-put! :current-page :mood-assessment))

  (defroute "/introduction/mood-assessment-1" []
    (global-put! :current-page :mood-assessment-1))

  (defroute "/introduction/mood-assessment-2" []
    (global-put! :current-page :mood-assessment-2))

  (defroute "/introduction/mood-assessment-3" []
    (global-put! :current-page :mood-assessment-3))

  (defroute "/introduction/mood-assessment-4" []
    (global-put! :current-page :mood-assessment-4))

  (defroute "/introduction/mood-evaluation" []
    (global-put! :current-page :mood-evaluation))

  (defroute "/application" []
    (global-put! :current-page :application))

  (defroute "/modules/scores" [query-params]
    (.log js/console (pr-str "The query params are: " query-params))
    (global-put! :current-page :scores))

  (hook-browser-navigation!))
