(ns reagent-goog-editor.plugins.events
  (:require [goog.events :as events]
            [goog.editor.Field.EventType :as event-type]
            [reagent-goog-editor.plugin-api :as plugin-api]))

(def plugin (namespace ::events))

(def event-types
  {:before-change goog.editor.Field.EventType.BEFORECHANGE
   :before-selection-change goog.editor.Field.EventType.BEFORESELECTIONCHANGE
   :blur goog.editor.Field.EventType.BLUR
   :command-value-change goog.editor.Field.EventType.COMMAND_VALUE_CHANGE
   ;; delayed-change is the defacto change event according to goog.editor
   :change goog.editor.Field.EventType.DELAYEDCHANGE
   :focus goog.editor.Field.EventType.FOCUS
   :load goog.editor.Field.EventType.LOAD
   :selection-change goog.editor.Field.EventType.SELECTIONCHANGE
   :unload goog.editor.Field.EventType.UNLOAD})

(defn attach-event-handlers [{field :field} event-handlers]
  (doseq [[event-name callback] event-handlers]
    (if-let [goog-event (event-types event-name)]
      (events/listen field goog-event callback)
      (.warn js/console (str "Ignorning unknown event `" event-name "`")))))

(defmethod plugin-api/attach plugin [_ editor plugin-opts]
  (attach-event-handlers editor plugin-opts))
