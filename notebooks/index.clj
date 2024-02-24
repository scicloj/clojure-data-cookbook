(ns index
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [scicloj.kindly.v4.kind :as kind]))

;; # Preface



;; ## Note! all work here is in progress, subject to change, very messy, and partially done. Please bear with me as I work on through this project :D

^:kindly/hide-code?
(let [files (->> (file-seq (io/file "./book"))
                 (filter (fn [file] (or (str/ends-with? file ".clj")
                                        (str/ends-with? file ".md"))))
                 (remove #(str/includes? % "experiments"))
                 (map #(str/replace % #"\.\/" ""))
                 (remove #{"book/index.clj"})
                 sort
                 (map #(-> %
                           (str/replace #"\.md$" ".html")
                           (str/replace #"\.clj$" "")
                           (str/replace #"^book/" "")))
                 (group-by (fn [el] (-> el (str/split #"\/") first))))]
  (kind/hiccup
   [:div [:h2 "Contents"]
    (for [[chapter_name pages] files]
      [:section [:h3 chapter_name]
       (for [page pages]
         [:li [:a {:href page} (-> page str/capitalize)]])])]))


;; ## Recommended sections

;; [randomizing order](chapter_3_data_manipulation/3_data_manipulation#randomizing-order)
