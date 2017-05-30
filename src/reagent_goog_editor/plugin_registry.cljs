(ns reagent-goog-editor.plugin-registry
  (:require [reagent-goog-editor.plugins.events :as events]
            [reagent-goog-editor.plugins.event-log :as event-log]
            [reagent-goog-editor.plugins.onchange :as onchange]
            [reagent-goog-editor.plugins.read-only-ratom :as read-only-ratom]
            [reagent-goog-editor.plugins.snippets :as snippets]
            [reagent-goog-editor.plugins.value-ratom :as value-ratom]))

(def events events/plugin)
(def event-log event-log/plugin)
(def onchange onchange/plugin)
(def read-only-ratom read-only-ratom/plugin)
(def snippets snippets/plugin)
(def value-ratom value-ratom/plugin)
