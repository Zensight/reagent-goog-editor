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
    :factory (fn [_ _ _ _ opt_renderer opt_domHelper]
               (goog.ui.ToolbarSeparator. opt_renderer opt_domHelper))})
