(ns reagent-goog-editor.plugins.value-ratom
  (:require [reagent-goog-editor.plugin-api :as plugin-api]
            [reagent-goog-editor.plugins.onchange :as onchange]
            [reagent-goog-editor.goog-editor :as editor]
            [reagent.ratom :as ratom]))

(def plugin (namespace ::value-ratom))

(defn on-change [editor ratom evnt]
  (let [contents (editor/get-field-contents (.-target evnt))]
    (when-not (= contents @ratom)
      (reset! ratom contents))))

(defmethod plugin-api/attach plugin [_ editor ratom]
  (-> (fn [] (editor/set-html editor @ratom))
      (ratom/make-reaction :auto-run true)
      deref)
  (plugin-api/attach onchange/plugin editor (partial on-change editor ratom)))
