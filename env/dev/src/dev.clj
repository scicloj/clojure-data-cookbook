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
    :base-target-path "book"
    :subdirs-to-sync ["src" "data"]
    :source-path ["index.clj"
                  "book/why-clojure.md"
                  "book/introduction.md"
                  ;; {:part "book/getting_started/0_getting_started_with_clojure.md"
                  ;;  :chapters ["book/getting_started/1_getting_started_with_other_languages.md"
                  ;;             "book/getting_started/2_workflow_basics.md"
                  ;;             "book/getting_started/3_idiomatic_clojure.md"]}
                  {:part "book/part_0_end_to_end/0_intro.md"
                   :chapters ["book/part_0_end_to_end/1_data_import_and_cleanup_example.clj"
                              "book/part_0_end_to_end/2_data_preparation_example.clj"]}
                  {:part "book/part_1_data_import/0_data_import.md"
                   :chapters ["book/part_1_data_import/1_tabular_data.clj"
                              "book/part_1_data_import/2_columnar_compressed_data.clj"]}]}))

(defn build-cli [_]
  (build)
  (System/exit 0))
