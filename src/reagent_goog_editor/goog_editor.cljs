(ns reagent-goog-editor.goog-editor
    (:require [reagent.core :as reagent]
              [goog.dom :as dom]
              [goog.editor.Command :as editor-command]
              [goog.editor.ContentEditableField :as editor-field]
              [goog.editor.Field.EventType :as event-type]
              [goog.editor.plugins.BasicTextFormatter :as basic-text-formatter]
              [goog.editor.plugins.EnterHandler :as enter-handler]
              [goog.editor.plugins.HeaderFormatter :as header-formatter]
              [goog.editor.plugins.LinkBubble :as link-bubble]
              [goog.editor.plugins.LinkDialogPlugin :as link-dialog-plugin]
              [goog.editor.plugins.ListTabHandler :as list-tab-handler]
              [goog.editor.plugins.LoremIpsum :as lorem-ipsum]
              [goog.editor.plugins.RemoveFormatting :as remove-formatting]
              [goog.editor.plugins.SpacesTabHandler :as spaces-tab-handler]
              [goog.editor.plugins.UndoRedo :as undo-redo]
              [goog.ui.editor.DefaultToolbar :as default-toolbar]
              [reagent-goog-editor.editor-plugins.blockquote :as blockquote-plugin]
              [reagent-goog-editor.commands.separator :as separator]
              [reagent-goog-editor.toolbar.controller :as toolbar-controller]))

(def editor-plugins [goog.editor.plugins.BasicTextFormatter
                     goog.editor.plugins.EnterHandler
                     goog.editor.plugins.HeaderFormatter
                     goog.editor.plugins.LinkBubble
                     goog.editor.plugins.LinkDialogPlugin
                     goog.editor.plugins.ListTabHandler
                     goog.editor.plugins.RemoveFormatting
                     goog.editor.plugins.SpacesTabHandler
                     goog.editor.plugins.UndoRedo
                     blockquote-plugin/Blockquote])

(def buttons [goog.editor.Command.FONT_FACE
              goog.editor.Command.FONT_SIZE
              goog.editor.Command.BOLD
              goog.editor.Command.ITALIC
              goog.editor.Command.UNDERLINE
              goog.editor.Command.FONT_COLOR
              goog.editor.Command.BACKGROUND_COLOR
              goog.editor.Command.LINK
              separator/SEPARATOR
              goog.editor.Command.JUSTIFY_LEFT
              goog.editor.Command.JUSTIFY_CENTER
              goog.editor.Command.JUSTIFY_RIGHT
              separator/SEPARATOR
              goog.editor.Command.ORDERED_LIST
              goog.editor.Command.UNORDERED_LIST
              separator/SEPARATOR
              goog.editor.Command.OUTDENT
              goog.editor.Command.INDENT
              goog.editor.Command.BLOCKQUOTE
              separator/SEPARATOR
              goog.editor.Command.REMOVE_FORMAT])

(defn- create-field [node-id]
  (let [field (goog.editor.ContentEditableField. node-id)]
    (doseq [plugin editor-plugins]
      (.registerPlugin field (new plugin)))
    field))

(defn- create-toolbar [node-id items]
  (let [node (dom/getElement node-id)]
    (.makeToolbar goog.ui.editor.DefaultToolbar items node)))

(defn- calculate-base-z-index [node]
  (let [z-ancestor (goog.dom/getAncestor
                    node
                    (fn [ancestor] (and (not= "#document" (.-nodeName ancestor))
                                        (not= "auto" (aget (.getComputedStyle js/window ancestor) "z-index"))))
                    false)]
    (if z-ancestor
      (int (aget (.getComputedStyle js/window z-ancestor) "z-index"))
      0)))

(defn create-editor [field-node-id toolbar-node-id]
  (let [field (create-field field-node-id)
        toolbar-buttons buttons
        toolbar (create-toolbar toolbar-node-id (clj->js buttons))
        controller (toolbar-controller/constructor field toolbar)
        base-z-index (calculate-base-z-index (.-originalElement field))]

    (.setBaseZindex field base-z-index)

    ;; The field initial state is disabled, so set the controller/toolbar initial state to match.
    (.setEnabled controller false)
    (.setVisible controller false)

    {:controller controller
     :field field
     :toolbar toolbar}))

(defn detach-editor [{:keys [controller field toolbar]}]
  (doseq [disposable [controller field toolbar]]
    (when disposable (.dispose disposable))))

(defn read-only? [editor]
  (let [field (:field editor)]
    (.isUneditable field)))

(defn toggle-read-only
  ([editor]
   (toggle-read-only (not (read-only? editor))))
  ([editor make-read-only?]
    (let [field (:field editor)
          controller (:controller editor)]
      (when (not= (read-only? editor) make-read-only?)
        (if make-read-only?
          (do
            (.makeUneditable field)
            (.setEnabled controller false)
            (.setVisible controller false))
          (do
            (.makeEditable field)
            (.setEnabled controller true)
            ;; place cursor at start to avoid some edge cases related to getting
            ;; a handle on a range before the field has received focus.
            (.placeCursorAtStart field)
            (.setVisible controller true)))))))

(defn get-field-contents [field]
  (.getCleanContents field))

(defn get-editor-contents [editor]
  (-> editor
      :field
      get-field-contents))

(defn set-html [editor html]
  (when (not= html (get-editor-contents editor))
    (-> editor
         :field
        (.setHtml
         ;; Don't add paragraph tags
         false
         html
         ;; Suppress the delayedchange event
         true))))
