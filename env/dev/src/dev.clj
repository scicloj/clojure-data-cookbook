(ns dev
  (:require
   [aerial.hanami.common :as hc]
   [scicloj.clay.v2.api :as clay]))

(defn build []
  (swap! hc/_defaults
         assoc
         :BACKGROUND "white")

  (clay/make!
   {:show false
    :run-quarto false
    :format [:quarto :html]
    :book {:title "Clojure Data Cookbook"}
    :base-source-path "src"
    :base-target-path "docs"
    :subdirs-to-sync ["src" "data"]
    :source-path ["index.clj"
                  "book/why-clojure.md"
                  "book/introduction.md"]}))
