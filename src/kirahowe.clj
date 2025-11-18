(ns kirahowe
(:require [tablecloth.api :as tc]
          [tablecloth.column.api :as tcc]))

(def ds 
  (tc/dataset "https://codewithkira.com/assets/penguins.csv"))

;; Studying scicloj tablecloth from Kira Howe's blog post

;; ## pivot->longer - Transform from wide to long format
;;
;; Takes a dataset and reshapes multiple measurement columns into a long format.
;;
;; Before (wide format):
;; species | bill_length_mm | bill_depth_mm | flipper_length_mm | body_mass_g
;; Adelie  | 39.1          | 18.7         | 181              | 3750
;;
;; After (long format):
;; species | measurement        | value
;; Adelie  | bill_length_mm    | 39.1
;; Adelie  | bill_depth_mm     | 18.7
;; Adelie  | flipper_length_mm | 181
;; Adelie  | body_mass_g       | 3750
;;
;; Parameters:
;; - ds: The input dataset
;; - Column vector: Which columns to pivot into rows
;; - :target-columns "measurement": Name for the new column holding original column names
;; - :value-column-name "value": Name for the new column holding the actual values
;;
;; Useful for:
;; - Plotting multiple measurements together (faceted plots)
;; - Grouped operations across different measurement types
;; - Preparing data for statistical modeling that expects long format
;;
;; Similar to tidyr::pivot_longer() in R or pandas.melt() in Python

(tc/pivot->longer ds
                  ["bill_length_mm" "bill_depth_mm"
                   "flipper_length_mm" "body_mass_g"]
                  {:target-columns "measurement" :value-column-name "value"})
