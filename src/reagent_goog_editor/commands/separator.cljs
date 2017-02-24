(ns reagent-goog-editor.commands.separator
  (:require [goog.ui.editor.DefaultToolbar :as default-toolbar]
            [goog.ui.ToolbarSeparator :as toolbar-separator]))

(def separator-command "+SEPARATOR")
(def SEPARATOR separator-command)

(aset default-toolbar/buttons_ separator-command #js {
    :command separator-command
    :tooltip ""
    :caption ""
    :classes "tr-separator"
    :factory (fn [_ _ _ _ opt-renderer opt-dom-helper]
               (let [separator (goog.ui.ToolbarSeparator. opt-renderer opt-dom-helper)]
                  ;; Ensure the separator has a unique ID different from Google's uniq IDs by
                  ;; prefixing generated ID with command name.
                  (aset separator "id_" (str separator-command
                                             (.. separator -idGenerator_ getNextUniqueId)))
                  separator))})
