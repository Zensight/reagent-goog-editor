(ns reagent-goog-editor.core
    (:require [reagent.core :as reagent]
              [goog.dom :as dom]
              [goog.editor.Command :as editor-command]
              [goog.editor.Field :as editor-field]
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
              [goog.ui.editor.ToolbarController :as toolbar-controller]
              [reagent-goog-editor.plugins.blockquote :as blockquote-plugin]))

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

(def buttons [goog.editor.Command.BOLD
              goog.editor.Command.ITALIC
              goog.editor.Command.UNDERLINE
              goog.editor.Command.FONT_COLOR
              goog.editor.Command.BACKGROUND_COLOR
              goog.editor.Command.FONT_FACE
              goog.editor.Command.FONT_SIZE
              goog.editor.Command.LINK
              goog.editor.Command.UNDO
              goog.editor.Command.REDO
              goog.editor.Command.BLOCKQUOTE
              goog.editor.Command.UNORDERED_LIST
              goog.editor.Command.ORDERED_LIST
              goog.editor.Command.INDENT
              goog.editor.Command.OUTDENT
              goog.editor.Command.JUSTIFY_LEFT
              goog.editor.Command.JUSTIFY_CENTER
              goog.editor.Command.JUSTIFY_RIGHT
              goog.editor.Command.SUBSCRIPT
              goog.editor.Command.SUPERSCRIPT
              goog.editor.Command.STRIKE_THROUGH
              goog.editor.Command.REMOVE_FORMAT])

(defn build-compose [node-id]
  (let [compose (goog.editor.Field. node-id)]
    (doseq [plugin plugins]
      (.registerPlugin compose (new plugin)))
    compose))

(defn build-toolbar [node-id]
  (let [node (dom/getElement node-id)]
    (.makeToolbar goog.ui.editor.DefaultToolbar (clj->js buttons) node)))

(defn build-editor [compose-node-id toolbar-node-id]
  (let [compose (build-compose compose-node-id)
        toolbar (build-toolbar toolbar-node-id)]
    (goog.ui.editor.ToolbarController. compose toolbar)
    (.makeEditable compose)))
