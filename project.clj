(defproject bono "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.1"]
                 [com.novemberain/monger "2.0.0"]
                ]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]

  :cljsbuild {
    :builds [{:id "bono"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/js/bono.js"
                :output-dir "resources/public/js/out"
                :optimizations :none
                :source-map true}}]})
