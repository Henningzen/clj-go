(ns no.jansenh.clj-go.terminal-board
  (:require [clojure.pprint :as pp]))

(defn- stsp
  ;; Keyword to symbol
  [s]
  (condp = s
    :white "o"
    :black "x"
    :nil "-"
    :else nil))

(defn- convert-to-symbols [v]
  (for [r v]
    (apply str (interpose " " (map stsp r)))))

(defn symbolic-board
  "Create a symbolic board 'o - x' for white, blank black values."
  [board]
  (apply str (interpose "\n" (convert-to-symbols board))))
