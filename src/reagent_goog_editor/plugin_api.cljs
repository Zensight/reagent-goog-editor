(ns reagent-goog-editor.plugin-api
  (:refer-clojure :exclude [name]))

(defn- dispatcher [plugin-name editor plugin-opts] plugin-name)

(defmulti attach dispatcher)
(defmulti detach dispatcher)
(defmulti name identity)

(defmethod attach :default [_ _ _] nil)
(defmethod detach :default [_ _ _] nil)
(defmethod name :default [plugin-name] plugin-name)
