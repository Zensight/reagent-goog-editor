(ns reagent-goog-editor.toolbar.controller
  (:require [goog.ui.editor.ToolbarController :as toolbar-controller])
  (:use-macros [reagent-goog-editor.macros :only [goog-extend]]))

(goog-extend Controller
  goog.ui.editor.ToolbarController
  ([])

  ;; Custom event handling to allow for different ways of determining command
  ;; and command value.
  (handleAction [event]
    (let [command (or (.-command event)
                      (.getId (.-target event)))
          value (or (.-value event)
                    (.getValue (.-target event)))]
      (this-as this
        (.execCommand (.-field_ this) command value)))))

;; ClojureScript's multi-function handling of variadic arguments makes it hard
;; to make a constructor function that does the right thing. That is to say that
;; it's unclear how to ensure that the `this` value in the constructor function
;; is a new instance object. As such, this function is the real constructor.
(defn constructor [field toolbar]
  (let [instance (Controller.)]
    (.call goog.ui.editor.ToolbarController instance field toolbar)
    instance))
