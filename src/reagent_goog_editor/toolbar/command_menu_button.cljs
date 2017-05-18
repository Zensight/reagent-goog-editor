(ns reagent-goog-editor.toolbar.command-menu-button
  (:require [goog.ui.ToolbarMenuButton]
            [goog.ui.MenuRenderer]
            [goog.ui.editor.ToolbarFactory]
            [goog.ui.Component])
  (:use-macros [reagent-goog-editor.macros :only [goog-extend]]))

(goog-extend ToolbarCommandMenuButton
  goog.ui.ToolbarMenuButton
  ([])

  (getCommand []
    (this-as this
      (or (.-command_ this)
          (.getId this))))

  ;; This method is the whole reason this subclass exists. We need to catch the
  ;; event dispatched by MenuItems and redispatch that event from this control
  ;; to ensure that the toolbar receives the right command and arguments for
  ;; that command.
  (handleMenuAction [event]
    (.stopPropagation event)
    (this-as this
      (.setOpen this false)
      (.dispatchEvent this (clj->js {:command (.getCommand this)
                                     :type goog.ui.Component.EventType.ACTION
                                     :value (.getValue (.-target event))}))))

  (setCommand [command]
    (this-as this
      (aset this "command_" command))))

;; ClojureScript's multi-function handling of variadic arguments makes it hard
;; to make a constructor function that does the right thing. That is to say that
;; it's unclear how to ensure that the `this` value in the constructor function
;; is a new instance object. As such, this function is the real constructor.
(defn constructor [{:keys [content dom-helper menu renderer]}]
  (let [instance (ToolbarCommandMenuButton.)]
    (.call goog.ui.ToolbarMenuButton instance content menu renderer dom-helper)
    instance))

(defn make-menu-button [{:keys [caption class-names command dom-helper id menu renderer tooltip]}]
  (let [content (.createContent_ goog.ui.editor.ToolbarFactory caption class-names dom-helper)
        button (constructor {:content content
                             :dom-helper dom-helper
                             :menu menu
                             :renderer renderer})]
    (when command (.setCommand button command))
    (.setId button id)
    (.setTooltip button tooltip)
    button))
