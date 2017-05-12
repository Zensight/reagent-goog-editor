(ns ^:figwheel-no-load reagent-goog-editor.dev
  (:require [reagent.core :as reagent]
            [reagent-goog-editor.core :as reagent-goog-editor]
            [reagent-goog-editor.plugin-registry :as plugin-registry]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(def read-only (reagent/atom false))
(def visible (reagent/atom true))
(def value (reagent/atom ""))

(defn component []
  [:div
   [:table
    [:tbody
      [:tr
       [:td
        [:button
         {:on-click #(swap! read-only not)}
         "Enable/disable editor"]]
       [:td
        [:button
         {:on-click #(swap! visible not)}
         "Show/remove editor"]]]]]
      [:hr]
      (when @visible
        [reagent-goog-editor/component {:field {:class-name "composer"}
                                        :plugins [[plugin-registry/event-log "event-log"]]
                                        :read-only @read-only
                                        :value-ratom value}])
      [:hr]
      [:div
       [:span "Editor Inner HTML"]
       [:div @value]]
      [:hr]
      [:div
       [:span "Event Log"]
       [:div {:id "event-log"}]]])

(defn render-demo []
  (reagent/render [component] (.getElementById js/document "app")))

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback render-demo)

(render-demo)
