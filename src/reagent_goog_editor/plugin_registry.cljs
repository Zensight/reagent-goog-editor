(ns reagent-goog-editor.plugin-registry
  (:require [reagent-goog-editor.plugins.events :as events]
            [reagent-goog-editor.plugins.event-log :as event-log]))

(def events events/plugin)
(def event-log event-log/plugin)
