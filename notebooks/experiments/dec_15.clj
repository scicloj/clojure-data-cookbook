(ns experiments.dec-15
  (:require [tablecloth.api :as tc]
            [tech.v3.datatype.functional :as fun]
            [nextjournal.clerk :as clerk]))

;; render all tech.ml.Dataset as table

(def flights
  (-> "book/experiments/data/nycflights13.csv.gz"
      (tc/dataset {:key-fn keyword})))

^{::clerk/viewer :table}
flights

^{::clerk/viewer :table}
(-> flights
    (tc/add-columns {:gain #(fun/- (:dep_delay %) (:arr_delay %))
                     :speed #(-> (:distance %)
                                 (fun// (:air_time %))
                                 (fun/* 60))
                     :hours #(fun// (:air_time %) 60)
                     ;; using columns we just made
                     ;; note: order of keys is important, :gain_per_hour has
                     ;; to come after :hours
                     :gain_per_hour #(fun// (:gain %) (:hours %))})
    ;; (tc/add-column :gain_per_hour #(fun// (:gain %) (:hours %)))
    )

;; equivalent of transmute
(-> {:w [1 2 3]
     :x [3 1 4]}
    (#(tc/dataset
       {:y (fun/+ (:w %) (:x %))
        :z (fun/- (:w %) (:x %))})))

(-> flights
    (tc/add-columns {:gain #(fun/- (:dep_delay %) (:arr_delay %))
                     :speed #(-> (:distance %)
                                 (fun// (:air_time %))
                                 (fun/* 60))
                     :hours #(fun// (:air_time %) 60)
                     ;; using columns we just made
                     ;; note: order of keys is important, :gain_per_hour has
                     ;; to come after :hours
                     :gain_per_hour #(fun// (:gain %) (:hours %))})
    (tc/select-columns [:gain :speed :hours :gain_per_hour])
    ;; (tc/add-column :gain_per_hour #(fun// (:gain %) (:hours %)))
    )

(-> flights
    (tc/add-columns {:gain #(fun/- (:dep_delay %) (:arr_delay %))
                     :speed #(-> (:distance %)
                                 (fun// (:air_time %))
                                 (fun/* 60))
                     :hours #(fun// (:air_time %) 60)
                     ;; using columns we just made
                     ;; note: order of keys is important, :gain_per_hour has
                     ;; to come after :hours
                     :gain_per_hour #(fun// (:gain %) (:hours %))})
    (tc/select-columns [:gain :speed :hours :gain_per_hour])
    ;; (tc/add-column :gain_per_hour #(fun// (:gain %) (:hours %)))
    )

^{::clerk/viewer :table}
(-> flights
    ;; unnecessary with `fun/mean`
    ;; (tc/drop-missing :dep_delay)
    ;; just ignores rows with missing values
    (tc/aggregate #(fun/mean (% :dep_delay)))
    ;; (tc/aggregate #(reduce + (% :dep_delay)))
    )

(:dep_delay flights)

;; Find rows with missing values:
;; ^{::clerk/viewer :markdown}
;; Clerk table viewer that shows fewer decimal places
;; (clerk/md)
(-> flights
    (tc/group-by [:flights :year :month :day])
    (tc/aggregate #(fun/mean (% :dep_delay)))
    ;; (tc/convert-types {:summary :int})
    ;; print
    ;; with-out-str
    )


(def by-dest
  (-> flights
      (tc/group-by [:flights :dest])))

;; NOTE: wrong columns names fail silently!
(-> by-dest
    (tc/drop-missing :arr_delay)
      ;; NOTE: fails with rows missing arr_delay
    (tc/select-rows (fn [row]
                      (and
                       (> 20 (:arr_delay row))
                       (not= (:dest row) "HNL"))))
    (tc/aggregate {:avg-distance #(fun/mean (:distance %))
                   :avg-delay #(fun/mean (:arr_delay %))}))


;; by_dest <- group_by (flights, dest)
;; delay <- summarise (by_dest,
;;                     count = n (),
;;                     dist = mean (distance, na.rm = TRUE),
;;                     delay = mean (arr_delay, na.rm = TRUE))
;; delay <- filter (delay, count > 20, dest != "HNL")
