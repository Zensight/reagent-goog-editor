(ns reagent-goog-editor.plugin-registry
  (:require [reagent-goog-editor.plugins.events :as events]
            [reagent-goog-editor.plugins.event-log :as event-log]
            [reagent-goog-editor.plugins.snippets :as snippets]))

(def events events/plugin)
(def event-log event-log/plugin)
(def snippets snippets/plugin)
