(ns dev
  (:require [scicloj.clay.v2.api :as clay]))


(clay/update-book!
 {:title "Clojure Data Cookbook"
  :base-source-path "book"
  :base-target-path "clean-book"
  :chapter-source-paths ["index.clj"
                         "chapter_1_intro/1_1_welcome.md"
                         "chapter_1_intro/1_2_why_clojure.md"
                         "chapter_1_intro/1_3_set_up.md"
                         "chapter_2_input_output/2_1_loading_data.clj"
                         "chapter_2_input_output/2_2_messy_data.clj"
                         "chapter_2_input_output/2_3_exporting_data.clj"
                         "chapter_3_data_manipulation/3_data_manipulation.clj"
                         "chapter_4_data_visualisation/noj_examples.clj"
                         "chapter_4_data_visualisation/4_2_graphs.clj"]
  :page-options {:quarto {:format {:html {;; Quarto themes:
                                          ;; https://quarto.org/docs/output-formats/html-themes.html
                                          :theme :spacelab}}
                          ;; Quarto code highlighting:
                          ;; https://quarto.org/docs/output-formats/html-code.html#highlighting
                          :highlight-style :solarized}}})
