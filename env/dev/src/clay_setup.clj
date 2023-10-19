(ns clay-setup
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [scicloj.clay.v2.api :as clay]))

(defn help []
  (println "Available commands are:")
  (println)
  (println "(start!)                  ;; Start Clay")
  (println "(show! <notebook-name>)    ;; Show the given notebook")
  (println "(build <dest-folder>)     ;; Build the book as a quarto project in the specified folder"))

(defn start! []
  (clay/start!)
  :ready)

(defn show! [notebook]
  (clay/show-namespace! notebook))

(defn generate-quarto-config [files]
  {:project {:type "book"}
   :format {:html {:theme {:light "flatly" :dark "darkly"}}}
   :book {:title "Clojure Data Cookbook"
          :chapters files}})

(defn build! [dest-folder]
  ;; render notebooks as md files in dest-folder
  (let [files (->> (file-seq (io/file "./book"))
                   (filter (fn [file] (or (str/ends-with? file ".clj")
                                          (str/ends-with? file ".md"))))
                   ;; (remove #(str/includes? % "experiments"))
                   (map #(str/replace % #"\.\/" ""))
                   ;; (remove #{"book/index.clj"})
                   sort
                   (map #(-> %
                             ;; (str/replace #"\.md$" ".html")
                             ;; (str/replace #"\.clj$" "")
                             ;; (str/replace #"^book/" "")
                             ))
                   ;; (group-by (fn [el] (-> el (str/split #"\/") first)))
                   )]

    ;; searches current dir for clay_setup.edn and uses those options, if nothing is found, assume {:target-dir "/qmd" :paths [<all-clojure-files>]}
    ;; (render-as-markdown)
    ;; (render-as-markdown opts)
    ;; (render-as-markdown path opts)

    ;; (render-as-markdown {:target-dir "qmd/"})

    ;; minimum
    ;; TODO, this fn doesn't actually exist
    ;; (render-as-markdown "notebook_1.clj" {:target-dir "qmd/"})

    ;; (map #(clay/generate-namespace-light-quarto! % {}) files)
    ;; (map render-as-quarto files)
    false)

  (help))

(clay/swap-options!
 assoc
 :remote-repo {:git-url "https://github.com/scicloj/clojure-data-cookbook"
               :branch "main"}
 :quarto {:format {:html {:toc true
                          :toc-depth 4
                          :theme :spacelab}}
          :highlight-style :solarized
          :code-block-background true
          :embed-resources false
          :execute {:freeze true}})
