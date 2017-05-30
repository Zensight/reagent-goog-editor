(ns reagent-goog-editor.component
  (:require [reagent.core :as reagent]
            [reagent-goog-editor.goog-editor :as editor]
            [reagent-goog-editor.plugin-api :as plugin-api]
            [reagent-goog-editor.plugin-registry :as plugin-registry]))

(def display-name "reagent-goog-editor")
(def id-counter (atom 0))

(defn next-id []
  (swap! id-counter inc))

(defn- extract-props [prop-wrapper]
  (reagent.impl.component/extract-props prop-wrapper))

(defn component-did-mount [this]
  (let [props (extract-props (.. this -props -argv))
        field-node (.. this -refs -field)
        toolbar-node (.. this -refs -toolbar)
        state-atom (reagent/state-atom this)
        field-id (.-id field-node)
        toolbar-id (.-id toolbar-node)
        plugins (or (:plugins props) [])
        editor (editor/create-editor field-id toolbar-id)]

    (swap! state-atom assoc :editor editor :plugins plugins)

    (doseq [[plugin plugin-opts] plugins]
        (try
         (plugin-api/attach plugin editor plugin-opts)
         (catch js/Object err
           (.log js/console (str "Error attaching plugin "
                                 (plugin-api/name plugin)
                                 " with options "
                                 (.toString plugin-opts)
                                 ": "
                                 (.toString err))))))))

(defn component-will-unmount [this]
  (let [state (reagent/state this)
        editor (:editor state)]
    (when editor
      (when-let [plugins (:plugins state)]
        (doseq [[plugin plugin-opts] plugins]
          (try
           (plugin-api/detach plugin editor plugin-opts)
           (catch js/Object err
             (.log js/console (str "Error detaching plugin "
                                   (plugin-api/name plugin)
                                   " with options "
                                   (.toString plugin-opts)
                                   ": "
                                   (.toString err)))))))
      (editor/detach-editor editor))))

(defn render-wrapper [props]
  [:div
   {:id (:id props)
    :style (:style props)
    :className (:class-name props)}])

(defn render-field [props]
  (let [fprops (or (:field props) {})]
    [:div
     {:className (:class-name fprops)
      :id (str display-name "-" (:component-id props))
      :ref "field"}]))

(defn render-toolbar [props]
  (let [tprops (or (:toolbar props) {})]
    [:div
     {:className (:class-name tprops)
      :id (str display-name "-toolbar-" (:component-id props))
      :ref "toolbar"}]))

(defn reagent-render [props]
  (let [component-props (assoc props :component-id (next-id))
        wrapper (render-wrapper component-props)
        field (render-field component-props)
        toolbar (render-toolbar component-props)]
    (conj wrapper toolbar field)))

(def component-config
  {:component-did-mount component-did-mount
   :component-will-unmount component-will-unmount
   :display-name display-name
   :reagent-render reagent-render})

(def component
  (reagent/create-class component-config))
