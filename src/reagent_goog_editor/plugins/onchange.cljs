(ns reagent-goog-editor.plugins.onchange
  (:require [reagent-goog-editor.plugin-api :as plugin-api]
            [reagent-goog-editor.plugins.events :as events]))

(def plugin (namespace ::onchange))

(defn attach-event-handler [editor event-handler]
  (when event-handler
    (plugin-api/attach events/plugin editor [[:change event-handler]])))

(defmethod plugin-api/attach plugin [_ editor plugin-opts]
  (attach-event-handler editor plugin-opts))
