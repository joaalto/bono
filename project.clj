(defproject bono "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :aliases { "cljs-prod" ["cljsbuild" "once" "prod"]}

  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.1"]
                 [om-sync "0.1.1"]
                 [prismatic/om-tools "0.3.2" :exclusions [org.clojure/clojure]]
                 [com.novemberain/monger "2.0.0"]
                 [compojure "1.1.8"]
                 [ring/ring-core "1.3.1"]
                 [ring-middleware-format "0.4.0"]
                 [ring/ring-servlet "1.1.5"]
                 [prone "0.6.0"]
                 ]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [lein-ring "0.8.11"]]

  :hooks [leiningen.cljsbuild]
  :main bono.handler
  :uberjar-name "bono.jar"

  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]

  :cljsbuild {:builds {
                       :bono {:id "bono"
                              :source-paths ["src/cljs"]
                              :compiler {
                                         :output-to "resources/public/js/out/bono.js"
                                         :output-dir "resources/public/js/out"
                                         :optimizations :none
                                         :source-map true}}

                       :prod {
                              :compiler {
                                         :output-to "resources/public/js/out/bono-min.js"
                                         :preamble ["public/js/react/react-0.11.2.min.js"]
                                         :externs ["public/js/react/react-0.11.2.js"]
                                         :optimizations :advanced
                                         :pretty-print false
                                         :source-map  "resources/public/js/out/source.map"
                                         :closure-warnings {:externs-validation :off
                                                            :non-standard-jsdoc :off}
                                         }}
                       }}

  :ring {:handler bono.handler/app}

  )
