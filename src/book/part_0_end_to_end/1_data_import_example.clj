(ns book.part-0-end-to-end.1-data-import-example
  (:require
   [tablecloth.api :as tc]))

;; # Data Import Example {#sec-data-import-example}

;; TODO: Summary

;; To build a model that predict house prices, we need data about recent sales of similar houses. Luckily the organization that tracks all of this data in my province has released it for public use under a [very permissive open data license](https://www.pvsc.ca/sites/default/files/shared/Open%20Data%20and%20Information%20Government%20Licence%20-%20PVSC%20and%20Participating%20Municipalities.pdf), so we'll use that data to find what we're looking for.
;;
;; First we need the data about actual house sales. That's available in this [Parcel Sales History](https://www.thedatazone.ca/Assessment/Parcel-Sales-History/6a95-ppg4/about_data) dataset. Standard tabular file formats like `.csv` just work in tablecloth, so there's not much to worry about here with importing the data:

(def sales-history
  (tc/dataset "data/end_to_end/parcel_sales_history.csv"))

;; We can get a quick summary of the dataset with `tc/info`:

(tc/info sales-history)

;; Here we can see that this dataset includes information about the sale of the house (e.g. address, sale date, sale price, etc), but no features of the house itself (e.g. number of bedrooms, bathrooms, garages, etc.). We could build a model that predicts sale price based solely on a house's location, but basic common sense tells us there's more that goes into the value of the home. It's not obvious exactly what the biggest determinants of price are, but anyone who has shopped for a house before knows that things like the age of the home, its size and features matter. To get this information, we'll need a second dataset: [Residential Dwelling Characteristics](https://www.thedatazone.ca/Assessment/Residential-Dwelling-Characteristics/a859-xvcs/about_data).
;;
;; We can load it the same way:

(def characteristics
  (tc/dataset "data/end_to_end/residential_dwelling_characteristics.csv"))

;; Upon inspection we can see that this dataset includes the information about houses that we're looking for, and it also as a column for "Assessment Account Number", which we should be able to use to join it up to our other one.

(tc/info characteristics)

;; There's one other piece of information that I want for our model. In Nova Scotia, it's not uncommon to own relatively large plots of land, and this can make house prices seem surprising. A small or very old house might seem like it's priced too high, for example, if you don't notice the size of the lot that it's on. We can get this information, again indexed by the Assessment Account Number, from the [Parcel Land Sizes](https://www.thedatazone.ca/Assessment/Parcel-Land-Sizes/wg22-3ric/about_data)

(def land-sizes
  (tc/dataset "data/end_to_end/parcel_land_sizes.csv"))

(tc/info land-sizes)

;; Since loading the data is so easy here, we'll take a moment to introduce tablecloth. Tablecloth is a





;; Contains information licensed under the Open Data & Information Government Licence – PVSC & Participating Municipalities
