(ns reagent-goog-editor.plugins.event-log
  (:require [goog.debug.DivConsole]
            [goog.dom]
            [goog.events]
            [goog.log]
            [goog.ui.Component]
            [goog.object]
            [reagent-goog-editor.plugin-api :as plugin-api]))

(def plugin (namespace ::event-log))

(defn attach-event-log [editor event-log-elem-id]
  (let [logger (.getLogger goog.log "event-log")
        console-elem (.getElement goog.dom event-log-elem-id)
        log-console (goog.debug.DivConsole. console-elem)
        log-event (fn [event]
                    (let [target (.-target event)
                          id (cond (.-getId target)
                                   (.getId target)

                                   (.-id target)
                                   (.-id target)
                                   :else "Unknown")
                          caption (if (and (= (type (.-getCaption target))
                                              js/Function)
                                           (.getCaption target))
                                    (str id " (" (.getCaption target) ")")
                                    id)]
                      (goog.log/info logger (str "'" caption "' dispatched: " (.-type event)))))
        events (.getValues goog.object (.-EventType goog.ui.Component))]

    (.setCapturing log-console true)
    (.setLevel (.getRoot goog.debug.LogManager) (.-ALL goog.log.Level))
    (.fine goog.log logger (str "Listening for: " (.join events ", ") "."))
    (.listen goog.events (:field editor) events log-event)
    (.listen goog.events (:toolbar editor) events log-event)))

(defmethod plugin-api/attach plugin [_ editor plugin-opts]
  (attach-event-log editor plugin-opts))
