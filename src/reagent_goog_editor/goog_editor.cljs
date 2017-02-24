(ns reagent-goog-editor.goog-editor
    (:require [reagent.core :as reagent]
              [goog.dom :as dom]
              [goog.editor.Command :as editor-command]
              [goog.editor.Field :as editor-field]
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
              [goog.events :as events]
              [goog.ui.editor.DefaultToolbar :as default-toolbar]
              [goog.ui.editor.ToolbarController :as toolbar-controller]
              [reagent-goog-editor.plugins.blockquote :as blockquote-plugin]
              [reagent-goog-editor.commands.separator :as separator]))

(def plugins [goog.editor.plugins.BasicTextFormatter
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

(def events
  {:before-change goog.editor.Field.EventType.BEFORECHANGE
   :before-selection-change goog.editor.Field.EventType.BEFORESELECTIONCHANGE
   :blur goog.editor.Field.EventType.BLUR
   :command-value-change goog.editor.Field.EventType.COMMAND_VALUE_CHANGE
   ;; delayed-change is the defacto change event according to goog.editor
   :change goog.editor.Field.EventType.DELAYEDCHANGE
   :focus goog.editor.Field.EventType.FOCUS
   :load goog.editor.Field.EventType.LOAD
   :selection-change goog.editor.Field.EventType.SELECTIONCHANGE
   :unload goog.editor.Field.EventType.UNLOAD})

(defn- create-field [node-id]
  (let [field (goog.editor.Field. node-id)]
    (doseq [plugin plugins]
      (.registerPlugin field (new plugin)))
    field))

(defn- create-toolbar [node-id]
  (let [node (dom/getElement node-id)]
    (.makeToolbar goog.ui.editor.DefaultToolbar (clj->js buttons) node)))

(defn create-editor
  ([field-node-id toolbar-node-id]
   (create-editor field-node-id toolbar-node-id {}))
  ([field-node-id toolbar-node-id options]
   (let [field (create-field field-node-id)
         toolbar (create-toolbar toolbar-node-id)
         controller (goog.ui.editor.ToolbarController. field toolbar)
         event-handlers (or (:events options) [])]

     (doseq [[event-name callback] event-handlers]
       (if-let [goog-event (events event-name)]
         (events/listen field goog-event callback)
         (.warn js/console (str "Ignorning unknown event `" event-name "`"))))

     ;; The field initial state is disabled, so set the controller/toolbar initial state to match.
     (.setEnabled controller false)
     (.setVisible controller false)

     {:controller controller
      :field field
      :toolbar toolbar})))

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
            (.setVisible controller true)))))))

(defn get-field-contents [field]
  (.getCleanContents field))

(defn set-html [editor html]
  (-> editor
       :field
      (.setHtml
       ;; Don't add paragraph tags
       false
       html
       ;; Suppress the delayedchange event
       true)))
