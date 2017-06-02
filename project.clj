(defproject reagent-goog-editor "1.0.1-SNAPSHOT"
  :description "Reagent wrapper around goog.editor"
  :url "https://github.com/Zensight/reagent-goog-editor"
  :license {:name "Copyright 2017 Zensight"
            :url "https://zensight.ai"}

  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.229" :scope "provided"]
                 [reagent "0.6.0"]]

  :repositories {"releases" {:url "s3p://zensight-builds/"
                                    :username :env/aws_access_key_id
                                    :passphrase :env/aws_secret_access_key}}

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.4-5"]
            [s3-wagon-private "1.2.0"]]

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :resource-paths ["public"]

  :figwheel {:http-server-root "."
             :nrepl-port 7002
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs ["public/css"]}

  :cljsbuild {:builds {:app
                       {:source-paths ["src" "env/dev/cljs"]
                        :compiler
                        {:main "reagent-goog-editor.dev"
                         :output-to "public/js/app.js"
                         :output-dir "public/js/out"
                         :asset-path   "js/out"
                         :source-map true
                         :optimizations :none
                         :pretty-print  true}
                        :figwheel
                        {:open-urls ["http://localhost:3449/index.html"]}}
                       :release
                       {:source-paths ["src" "env/prod/cljs"]
                        :compiler
                        {:output-to "public/js/app.js"
                         :output-dir "public/js/release"
                         :asset-path   "js/out"
                         :optimizations :advanced
                         :pretty-print false}}}}

  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.4-5"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [com.cemerick/piggieback "0.2.2-SNAPSHOT"]]}})
