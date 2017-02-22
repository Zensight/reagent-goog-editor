(ns reagent-goog-editor.prod
  (:require [reagent-goog-editor.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))
