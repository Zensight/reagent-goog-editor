(ns reagent-goog-editor.plugin-api
  (:refer-clojure :exclude [name]))

(defn- dispatch-fn [plugin-name editor plugin-opts] plugin-name)

(defmulti attach dispatch-fn)
(defmulti detach dispatch-fn)
(defmulti name identity)

(defmethod attach :default [_ _ _] nil)
(defmethod detach :default [_ _ _] nil)
(defmethod name :default [plugin-name] plugin-name)
