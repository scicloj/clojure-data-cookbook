(ns book.chapter-1.whole-game
  (:require [clojure.edn :as edn]
            [tablecloth.api :as tc]
            [aerial.hanami.templates :as ht]
            [scicloj.noj.v1.vis.hanami :as hanami]
            ;; [data.generate-dataset :as dataset]
            [tech.v3.datatype.functional :as fun]
            [scicloj.noj.v1.stats :as stats]))

;; # London Clojurians Dec 12 2023

(-> "data/1000-repos.edn"
    slurp
    edn/read-string
    first)

(def clojure-repos
  (tc/dataset "data/1000-repos.edn"))

(-> clojure-repos tc/column-names sort)

(-> clojure-repos
    (tc/select-columns [:full_name :owner :watchers_count :created_at]))

(-> clojure-repos
    (tc/map-columns :owner_handle [:owner] :login)
    (tc/select-columns [:full_name :owner_handle :watchers_count :created_at]))

(-> clojure-repos
    (tc/map-columns :owner_handle [:owner] :login)
    (tc/select-columns [:full_name :owner_handle :watchers_count :created_at])
    (tc/group-by [:owner_handle])
    (tc/as-regular-dataset)
    (tc/select-rows 0)
    :data)

(-> clojure-repos
    (tc/map-columns :owner_handle [:owner] :login)
    (tc/select-columns [:full_name :owner_handle :watchers_count :created_at])
    (tc/group-by [:owner_handle])
    (tc/aggregate {:repos_count tc/row-count})
    (tc/order-by :repos_count :desc))

(-> clojure-repos
    (tc/map-columns :owner_handle [:owner] :login)
    (tc/select-columns [:full_name :owner_handle :watchers_count :created_at])
    (tc/group-by [:owner_handle])
    (tc/aggregate {:total_watchers (fn [ds]
                                     (reduce + (:watchers_count ds)))}))

;; Do newer repos have fewer watchers?

(-> clojure-repos
    (hanami/plot ht/point-chart
                 {:X     :created_at
                  :XTYPE "temporal"
                  :Y     :watchers_count}))

(-> clojure-repos
    (tc/select-rows #(< (:watchers_count %) 10000))
    (hanami/plot ht/point-chart
                 {:X     :created_at
                  :XTYPE "temporal"
                  :Y     :watchers_count}))

(-> clojure-repos
    (hanami/plot ht/point-chart
                 {:X     :created_at
                  :XTYPE "temporal"
                  :Y     :watchers_count
                  :YSCALE {:type "log"}}))

(-> clojure-repos
    (hanami/plot ht/point-chart
                 {:X      :created_at
                  :XTYPE  "temporal"
                  :Y      :stargazers_count
                  :YSCALE {:type "log"}
                  :MSIZE 60
                  :TOOLTIP [{:field "full_name"}
                            {:field "stargazers_count"}]}))

;; Is there a relationship between contributors and stargazers?

(-> clojure-repos
    (tc/select-columns [:contributors_url]))

(def commit-details
  (tc/dataset "data/commit-details-2023-12-07T20:57:26.289-00:00.csv.gz"
              {:key-fn keyword}))


(tc/info commit-details)
(tc/head commit-details)

(def repos-ds
  (->> {:python "data/python-repos2023-12-05T14:46:13.281-00:00.edn"
        :clojure "data/clojure-repos2023-12-05T14:46:12.990-00:00.edn"}
       (map (fn [[language path]]
              [language (-> path
                            slurp
                            edn/read-string)]))
       (into {})
       vals
       (mapcat (partial mapcat (comp :items :items)))
       (map #(-> %
                 (select-keys [:language :html_url :size :owner :stargazers_count])
                 (update :owner :login)))
       tc/dataset))

(-> commit-details
    (tc/group-by [:language :html_url])
    (tc/aggregate {:contributors_count (fn [ds]
                                         (-> ds :email distinct count))})
    (tc/left-join repos-ds [:html_url])

    (tc/group-by [:language])
    (hanami/plot ht/point-chart
                 {:X :contributors_count
                  :XSCALE {:type "log"}
                  :Y :stargazers_count
                  :YSCALE {:type "log"}}))

(-> commit-details
    (tc/group-by [:language :html_url])
    (tc/aggregate {:contributors_count (fn [ds]
                                         (-> ds :email distinct count))})
    (tc/left-join repos-ds [:html_url])
    (tc/group-by [:language])
    (tc/add-columns {:log-stargazers #(-> % :stargazers_count fun/log)
                     :log-contributors #(-> % :contributors_count fun/log)})
    (stats/add-predictions :log-stargazers
                           [:log-contributors]
                           {:model-type :smile.regression/ordinary-least-square})
    (hanami/linear-regression-plot
      :log-contributors
      :log-stargazers
      {:XSCALE {:zero false}
       :YSCALE {:zero false}
       :line-options {:MCOLOR "brown"}
       :point-options {:MSIZE 70}})
    (tc/map-columns :plot [:plot]
                    #(assoc-in %
                               [:encoding :tooltip]
                               {:field "html_url"}))
    )
