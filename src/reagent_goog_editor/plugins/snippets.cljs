(ns reagent-goog-editor.plugins.snippets
  (:require [goog.ui.MenuItem]
            [goog.ui.Menu]
            [reagent-goog-editor.editor-plugins.insert-snippet :as insert-snippet]
            [reagent-goog-editor.plugin-api :as plugin-api]
            [reagent-goog-editor.toolbar.command-menu-button :as command-menu-button]))

(def plugin (namespace ::snippets))

(defn- populate-menu-with-snippets [menu snippets]
  (let [dom-helper (.getDomHelper menu)
        items (map (fn [{:keys [name value]}]
                     (goog.ui.MenuItem. name (or value name) dom-helper))
                   snippets)]
    (doseq [item items]
      (.addItem menu item))))

(defn snippet-menu-factory [{:keys [dom-helper caption class-names command id renderer snippets tooltip]}]
  (let [menu (goog.ui.Menu. dom-helper)
        opts {:caption caption
              :class-names class-names
              :command command
              :dom-helper dom-helper
              :id id
              :menu menu
              :renderer renderer
              :tooltip tooltip}]
    (populate-menu-with-snippets menu snippets)
    (command-menu-button/make-menu-button opts)))

(defn- attach-menu [{:keys [toolbar]}
                    {:keys [caption class-names command id snippets tooltip]}]
  (let [dom-helper (.getDomHelper toolbar)
        menu (snippet-menu-factory {:caption caption
                                    :class-names class-names
                                    :command "InsertSnippet"
                                    :dom-helper dom-helper
                                    :id id
                                    :snippets snippets
                                    :tooltip tooltip})]
    (.addChild toolbar menu true))
  nil)

(defn- initialize-plugin [{:keys [field] :as editor} {:keys [snippets] :as opts}]
  (.registerPlugin field (insert-snippet/constructor snippets))
  (attach-menu editor opts))

(defmethod plugin-api/attach plugin [_ editor plugin-opts]
  (initialize-plugin editor plugin-opts))
