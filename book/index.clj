(ns book.index
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [nextjournal.clerk :as clerk]))

;; # Clojure Data Cookbook

;; Welcome to the Clojure Data Cookbook! This is the website for the work-in-progress that will become the Clojure Data Cookbook. The goal is to provide a reference for anyone who has data to work with and an interest in doing it in Clojure, documenting the current community recommendations and default stack for data science in Clojure.

;; ## Note! all work here is in progress, subject to change, very messy, and partially done. Please bear with me as I work on through this project :D

(let [files  (->> (file-seq (io/file "./book"))
                  (filter (fn [file] (or (str/ends-with? file ".clj")
                                         (str/ends-with? file ".md"))))
                  (remove #(str/includes? % "experiments"))
                  (map #(str/replace % #"\.\/" ""))
                  (remove #{"book/index.clj"}))]
  (clerk/html [:div "Chapters"
               [:ul
                (for [file files]
                  (let [webpage (str/replace file #".md|.clj" ".html")]
                    [:li [:a {:href webpage} (-> file str)]]))]]))
