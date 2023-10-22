(ns dev
  (:require [scicloj.clay.v2.api :as clay]))


(clay/update-book!
 {:title "Clojure Data Cookbook"
  :base-source-path "book"
  :base-target-path "clean-book"
  :chapter-source-paths ["chapter_1_intro/1_1_welcome.md"
                         "chapter_4_data_visualisation/noj_examples.clj"]})
