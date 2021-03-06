(set-env!
  :source-paths   #{"src"}
  :resource-paths #{"resources/public"}
  :dependencies '[
    [adzerk/boot-cljs            "1.7.48-1"        :scope "test"]
    [adzerk/boot-cljs-repl       "0.1.10-SNAPSHOT" :scope "test"]
    [crisptrutski/boot-cljs-test "0.1.0-SNAPSHOT"  :scope "test"]
    [mathias/boot-restart        "0.0.2"           :scope "test"]
    [org.clojure/clojure         "1.7.0"]
    [org.clojure/clojurescript   "1.7.58"]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[mathias.boot-restart  :refer [restart]]
  '[crisptrutski.boot-cljs-test :refer [test-cljs]])

(deftask auto-test []
  (set-env! :source-paths #{"src" "test"})
  (comp (watch)
        (speak)
        (test-cljs)))

(deftask build-dev []
  (set-env! :source-paths #{"src/"})
  (cljs :optimizations :none
        :source-map true))

(deftask restart-server []
  (restart :command "node app.js"))
