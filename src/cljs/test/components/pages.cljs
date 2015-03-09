(ns test.components.pages
  (:require [test.session :refer [global-state]]
            [test.components.introduction.home-page :as home-page]
            [test.components.introduction.second :as second]
            [test.components.introduction.third :as third]
            [test.components.introduction.fourth :as fourth]
            [test.components.introduction.fifth :as fifth]
            [test.components.introduction.sixth :as sixth]
            [test.components.introduction.seventh :as seventh]
            [test.components.introduction.mood-assessment-initial :as mood-assessment-initial]
            [test.components.introduction.mood-assessment :as mood-assessment]
            [test.components.introduction.mood-assessment-1 :as
             mood-assessment-1]
            [test.components.introduction.mood-assessment-2 :as
             mood-assessment-2]
            [test.components.introduction.mood-assessment-3 :as
             mood-assessment-3]
            [test.components.introduction.mood-assessment-4 :as
             mood-assessment-4]
            [test.components.introduction.mood-evaluation :as mood-evaluation]
            [test.components.application :as application]
            [test.components.modules.scores :as scores]
            [test.components.modules.visions :as visions]
            [test.components.modules.goals :as goals]
            [test.components.modules.another-goal :as another-goal]
            [test.components.modules.steps :as steps]
            [test.components.modules.another-step :as another-step]
            [test.components.tabs.goal-tab :as goal-tab]))

(defn page []
  (condp = (global-state :current-page)
    :home-page home-page/component
    :introduction-second second/component
    :introduction-third third/component
    :introduction-fourth fourth/component
    :introduction-fifth fifth/component
    :introduction-sixth sixth/component
    :introduction-seventh seventh/component
    :mood-assessment-initial mood-assessment-initial/component
    :mood-assessment mood-assessment/component
    :mood-assessment-1 mood-assessment-1/component
    :mood-assessment-2 mood-assessment-2/component
    :mood-assessment-3 mood-assessment-3/component
    :mood-assessment-4 mood-assessment-4/component
    :mood-evaluation mood-evaluation/component
    :application application/component
    :scores scores/component
    :visions visions/component
    :goals goals/component
    :another-goal another-goal/component
    :steps steps/component
    :another-step another-step/component
    :tabs-goals goal-tab/component))
