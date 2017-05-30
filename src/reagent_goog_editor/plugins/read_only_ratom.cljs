(ns reagent-goog-editor.plugins.read-only-ratom
  (:require [reagent.ratom :as ratom]
            [reagent-goog-editor.plugin-api :as plugin-api]
            [reagent-goog-editor.goog-editor :as editor]))

(def plugin (namespace ::read-only-ratom))

(defmethod plugin-api/attach plugin [_ editor ratom]
  (-> (fn [] (editor/toggle-read-only editor @ratom))
      (ratom/make-reaction :auto-run true)
      deref))
