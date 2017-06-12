(ns reagent-goog-editor.editor-plugins.chips
  (:require [goog.dom :as dom]
            [goog.dom.Range]
            [goog.editor.Plugin :as editor-plugin]
            [goog.editor.plugins.AbstractBubblePlugin :as abstract-bubble]
            [goog.editor.range]
            [goog.events.EventHandler]
            )
  (:use-macros [reagent-goog-editor.macros :only [goog-extend]]))

(def chip-class "gmail_chip")
(def KEYCODE_DELETE 46)
(def KEYCODE_BACKSPACE 8)
(def KEYCODE_ARROW_LEFT 37)
(def KEYCODE_ARROW_UP 38)
(def KEYCODE_ARROW_RIGHT 39)
(def KEYCODE_ARROW_DOWN 40)

(defn handleClick [event]
  ;; PD.prototype.Ha
  (js* "debugger")
  nil
  )

(defn- rediscover-chip-elems [chip]
  (let [event-register (.-chipsEventRegister chip)
        node-list (goog.dom.getElementsByClass (.-className chip))
        elems (map #(.item node-list %) (range (.-length node-list)))]
    (.removeAll event-register)
    (doseq [elem elems]
      (when (not= (.-contentEditable elem) "true")
        (aset elem "contentEditable" "false"))
      (.listen event-register elem (clj->js ["mousedown", "click"]) handleClick true))
  ))

(defn- reinit [chip]
  ;; SZb /TGa
  (.log js/console "reinit")
  (let [thing nil
        ]
    (rediscover-chip-elems chip)
  ))

(defn remove-chip [chip event forward?]
  ;; wMd function(chip event forward?)
  (js* "debugger")
  nil)

(defn- find-ancestor-chip [chip node _]
  (let [field-elem (.. chip -fieldObject getElement)]
    (loop [current-node node]
      (if (and current-node (not= current-node field-elem))
        (if (goog.dom.classes.has current-node chip-class)
          current-node
          (recur (.-parentNode current-node)))
        nil))))

(defn- find-left-child-chip [chip node offset]
  ;; pMd / fMd
  )

(defn- find-right-child-chip [chip node offset]
  ;; qMd / gMd
  )

(defn- find-chip-elem [chip use-anchor?]
  (let [selection (.. chip -fieldObject getRange)
        node (if use-anchor?
               (.getAnchorNode selection)
               (.getFocusNode selection))
        offset (if use-anchor?
                 (.getAnchorOffset selection)
                 (.getFocusOffset selection))]
    (some #(% chip node offset) [find-ancestor-chip
                                 find-left-child-chip
                                 find-right-child-chip])))

(defn- get-sibling-point [chip-elem to-left?]
  ;; jMd
  (let [parent (.-parentNode chip-elem)]
    (if to-left?
      (do
       )
      (do
       ))
    {:node nil
     :point nil}
    )
  )

(defn arrow-motion [chip forward-motion? shift-key? vertical-motion?]
  ;; PD.prototype.ra = function(forward-motion? shift-key vertical-motion?)
  (if vertical-motion?
    (let [selection (.. chip -fieldObject getRange)]
      (js* "debugger")
      nil)
    (let [other nil]
      (js* "debugger")
      nil))


    (if shift-key?
      (do
       nil)
      (let [chip-elem nil
            point (get-sibling-point chip-elem (not forward-motion?))
            caret (goog.dom.Range/createCaret (:node point) (:offset point))
            ]
        (.select caret)))
    nil)

(defn- update-selection [chip]
  ; nMd
  ;(js* "debugger")
  (.log js/console "update-selection")
  nil)

(goog-extend Chips
  goog.editor.plugins.AbstractBubblePlugin
  ([])

  (enable [field]
    (this-as this
      (abstract-bubble/base this "enable" field)
      (reinit this))
    (.log js/console "enabled")
    )

  (getBubbleTargetFromSelection [selected-element]
    ;(js* "debugger")
    nil)

  (handleKeyUp [event]
    (this-as this
      (condp = (.-keyCode event)
        KEYCODE_DELETE
        (remove-chip this event true)

        KEYCODE_BACKSPACE
        (remove-chip this event false)

        KEYCODE_ARROW_LEFT
        (do
         (arrow-motion this true (.-shiftKey event) false)
         false)

        KEYCODE_ARROW_UP
        (do
         (arrow-motion this true (.-shiftKey event) true)
         false)

        KEYCODE_ARROW_RIGHT
        (do
         (arrow-motion this false (.-shiftKey event) false)
         false)

        KEYCODE_ARROW_DOWN
        (do
         (arrow-motion this false (.-shiftKey event) true)
         false)

        false)))

  (handleSelectionChange [ & _]
    (this-as this
      (update-selection this)))

  )

(defn new-instance [{:keys [:class-name]}]
  (let [instance (Chips.)
        event-register (goog.events.EventHandler.)]
    (aset instance "chipsEventRegister" event-register)
    (aset instance "className" (or class-name chip-class))
    instance))
