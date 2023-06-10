(ns index
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [nextjournal.clerk :as clerk]))

;; # Clojure Data Cookbook
;;
;; Welcome to the Clojure Data Cookbook!

(let [files  (->> (file-seq (io/file "./book"))
                  (filter (fn [file] (or (str/ends-with? file ".clj")
                                         (str/ends-with? file ".md"))))
                  (remove #(str/includes? % "experiments"))
                  (map #(str/replace % #"\.\/book/" ""))
                  (remove #{"book/index.clj"}))]
  (clerk/html [:div
               [:h2 "Contents"]
               [:ul
                (for [file files]
                  (let [webpage (str/replace file #".md|.clj" ".html")]
                    [:li [:a {:href webpage} file]]))]]))
