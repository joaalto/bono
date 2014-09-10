(defproject bono "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.1"]
                 [om-sync "0.1.1"]
                 [prismatic/om-tools "0.3.2"]
                 [com.novemberain/monger "2.0.0"]
                 [compojure "1.1.8"]
                 [ring/ring-core "1.3.1"]
                 [ring-middleware-format "0.4.0"]
                 [prone "0.6.0"]
                 ;[fogus/ring-edn "0.2.0"]
                ]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [lein-ring "0.8.11"]]

  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]

  :cljsbuild {
    :builds [{:id "bono"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/js/out/bono.js"
                :output-dir "resources/public/js/out"
                :optimizations :none
                :source-map true}}]}

  :ring {:handler bono.handler/app}
)
