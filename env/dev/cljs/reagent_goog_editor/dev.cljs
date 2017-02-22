(ns ^:figwheel-no-load reagent-goog-editor.dev
  (:require [reagent-goog-editor.core :as core]
            [reagent.core :as reagent]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(def toolbar-node-id "toolbar")
(def compose-node-id "composer")

(defn demo-view []
  [:div
   [:div {:id toolbar-node-id}]
   [:div {:class "composer"
          :id compose-node-id}]])

(defn render-demo []
  (reagent/render [demo-view] (.getElementById js/document "app"))
  (core/build-editor compose-node-id toolbar-node-id))

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback render-demo)

(render-demo)
