(ns user)

(defn help []
  (println "Welcome to the Clojure Data Cookbook")
  (println)
  (println "Available commands are:")
  (println)
  (println "(start!)                  ;; Start Clerk")
  (println "(start-and-watch!)        ;; Start Clerk and reload on all changes to the `book` namespaces")
  (println "(show <notebook-name>)    ;; Show the given notebook"))

(defn dev
  "Load and switch to the 'dev' namespace."
  []
  (require 'dev)
  (help)
  (in-ns 'dev)
  :loaded)


(require '[libpython-clj2.python :as py])

(py/initialize! :python-executable "/opt/homebrew/Caskroom/miniconda/base/envs/cookbook/bin/python3.11"
                :library-path "/opt/homebrew/Caskroom/miniconda/base/envs/cookbook/lib/libpython3.11.dylib")
