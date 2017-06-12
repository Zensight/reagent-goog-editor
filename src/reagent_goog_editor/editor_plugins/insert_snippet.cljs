(ns reagent-goog-editor.editor-plugins.insert-snippet
  (:require [goog.dom :as dom]
            [goog.editor.Plugin :as editor-plugin]
            [goog.editor.range])
  (:use-macros [reagent-goog-editor.macros :only [goog-extend]]))

(def supported-commands #{"InsertSnippet"})

(goog-extend InsertSnippet
  goog.editor.Plugin
  ([])

  ;; if argv is a Function, that function should return a node for insertion.
  ;; Otherwise a span is created with the value of argv as its content.
  (execCommandInternal [command argv]
    (this-as this
      (let [dom-helper (.. this getFieldDomHelper)
            field-obj (.getFieldObject this)
            selection (.getRange field-obj)]
        (when selection
          (let [focus-node (.getStartNode selection)
                field-node (.-field field-obj)]
            ;; Make sure the selection belongs to the field node!
            (when (dom/contains field-node focus-node)
              (let [content (if (= (type argv) js/Function)
                              (argv dom-helper)
                              (.createDom dom-helper "span" nil argv))
                    nodes (if (.-length content) content [content])]
                (.removeContents selection)
                (doseq [node nodes]
                  (.insertNode selection node true))
                (.placeCursorNextTo goog.editor.range (last nodes) false))))))))

  (getTrogClassId []
    "RGE-SNIP")

  (isSupportedCommand [command]
    (boolean (supported-commands command)))

  (setSnippets [snippets]
    (this-as this
      (aset this "snippets_" snippets))))

;; ClojureScript's multi-function handling of variadic arguments makes it hard
;; to make a constructor function that does the right thing. That is to say that
;; it's unclear how to ensure that the `this` value in the constructor function
;; is a new instance object. As such, this function is the real constructor.
(defn constructor [snippets]
  (let [instance (InsertSnippet.)]
    (.call goog.editor.Plugin instance)
    (.setSnippets instance snippets)
    instance))
