(ns reagent-goog-editor.editor-plugins.blockquote
  (:require [goog.editor.Plugin :as editor-plugin]
            [goog.dom :as dom])
  (:use-macros [reagent-goog-editor.macros :only [goog-extend]]))

(def supported-commands #{"+BLOCKQUOTE"})
(def css-class-name "gmail_quote")
(def ltr-style "margin:0 0 0 .8ex;border-left:1px #ccc solid;padding-left:1ex")
(def rtl-style "margin:0 .8ex;border-left:1px #ccc solid;border-right:1px #ccc solid;padding-left:1ex;padding-right:1ex")

(goog-extend Blockquote
  goog.editor.Plugin
  ([])

  (document []
    (this-as this
      (.. this getFieldDomHelper getDocument)))

  (execCommandInternal [command argv]
    (case command
      "+BLOCKQUOTE"
      (this-as this
        (.execCommand (.document this) "formatBlock" false "blockquote")
        (let [field-node (.. this getFieldObject -field)
              selection (.. this getFieldObject getRange)
              focus-node (if selection
                          (.getStartNode selection)
                          field-node)
              blockquote (or (goog.dom/getAncestor focus-node
                                                   (fn [node] (= (.-tagName node) "BLOCKQUOTE"))
                                                   true)
                             (and (not= focus-node field-node)
                                  (.-firstChild focus-node)))]
          (when (and blockquote
                     (goog.dom/contains field-node blockquote))
            (.add (.-classList blockquote) css-class-name)
            (aset (.-style blockquote) "cssText" ltr-style))))))

  (getTrogClassId []
    "RGE-BQ")

  (isSupportedCommand [command]
    (boolean (supported-commands command))))
