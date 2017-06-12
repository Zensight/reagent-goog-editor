(ns ^:figwheel-no-load reagent-goog-editor.dev
  (:require [reagent.core :as reagent]
            [reagent-goog-editor.core :as reagent-goog-editor]
            [reagent-goog-editor.plugin-registry :as plugin-registry]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(def read-only (reagent/atom false))
(def visible (reagent/atom true))
(def value (reagent/atom ""))
(def snippet-counter (atom 0))

(defn dynamic-snippet [dom-helper]
  (swap! snippet-counter inc)
  (let [elem (.createDom dom-helper "span" nil (str "Snippet #" @snippet-counter))]
    (aset elem "contentEditable" "false")
    elem))

(def snippets-opts {:caption "Snippets"
                    :class-names "tr-snippet-menu"
                    :id "insert-snippet"
                    :snippets [{:name "Hello World"}
                               {:name "Insert `Hiya Pal`"
                                :value "Hiya Pal"}
                               {:name "Dynamic Snippet"
                                :value dynamic-snippet}]
                    :tooltip "Snippets"})

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
         "Show/remove editor"]]
       [:td
        [:button
         {:on-click #(reset! value "<h2>Hello, World!</h2>")}
         "Set content to `Hello, World!`"]]
       [:td
        [:button
         {:on-click #(reset! value "left &#8203;<span class=\"gmail_chip\" style=\"outline: solid 1px black;\">Chippy</span>&#8203; right")}
         "Insert chip"]] ]]]
      [:hr]
      (when @visible
        [reagent-goog-editor/component {:field {:class-name "composer"}
                                        :plugins [[plugin-registry/event-log "event-log"]
                                                  [plugin-registry/snippets snippets-opts]
                                                  [plugin-registry/value-ratom value]
                                                  [plugin-registry/read-only-ratom read-only]]}])
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
