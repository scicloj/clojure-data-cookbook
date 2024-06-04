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
                  "book/introduction.md"
                  "book/part_1_data_import/0_data_import.md"
                  "book/part_1_data_import/1_tabular_data.clj"
                  "book/part_1_data_import/2_columnar_compressed_data.clj"
                  ]}))

(defn build-cli [_]
  (build)
  (System/exit 0))
